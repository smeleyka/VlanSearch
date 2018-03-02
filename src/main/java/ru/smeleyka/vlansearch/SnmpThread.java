package ru.smeleyka.vlansearch;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by smeleyka on 08.09.17.
 */
public class SnmpThread implements Runnable {
    private CommunityTarget target;
    private String ip;
    private String oid;
    private Switch sw;
    private Map<String, String> snmpAnswer;


    public SnmpThread(String ip, String community, String oid, int version, int retries, int timeout) {
        this.ip = ip;
        this.oid = oid;
        this.target = new CommunityTarget();
        this.target.setCommunity(new OctetString(community));
        this.target.setRetries(retries);
        this.target.setTimeout(timeout);
        this.target.setVersion(version);
        this.target.setAddress(GenericAddress.parse("udp:" + ip + "/161"));
        this.sw = new Switch(ip);
    }

    @Override
    public void run() {
        snmpGetVlan(sw);
        snmpGetName(sw);
        sortLiveSwitch(sw);
    }

    private void snmpGetVlan(Switch sw) {

        snmpAnswer = doWalk(Resources.VLAN_OID, target);

        for (Map.Entry<String, String> entry : snmpAnswer.entrySet()) {
            //System.out.printf("%10s -- %24s     %s", ip, entry.getKey(), entry.getValue());
            //System.out.println();

            sw.addVlan(stringKeyToInt(entry.getKey()));  //adding vlan to switch object
            //System.out.printf("%10s -- %24s     %s" + "\n", ip, stringKeyToInt(entry.getKey()), entry.getValue());
        }

    }

    private void snmpGetName(Switch sw) {

        SimpleSnmpClient client = new SimpleSnmpClient("udp:" + ip + "/161");
        String sysDescr = null;
        try {
            sysDescr = client.getAsString(new OID(Resources.NAME_OID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sw.setName(sysDescr);

    }

    private void sortLiveSwitch(Switch sw) {

        if (!sw.getVlans().isEmpty()) {
            SnmpWalk.addSwitch(sw);

            //for (SnmpWalk)
        }
    }

    private Map<String, String> doWalk(String tableOid, Target target) {

        Map<String, String> result = new TreeMap<>();

        try {

            TransportMapping<? extends Address> transport = null;
            transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();

            TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
            List events = treeUtils.getSubtree(target, new OID(tableOid));
            if (events == null || events.size() == 0) {
                //System.out.println("Error: Unable to read table...");
                return result;
            }
            TreeEvent event;
            for (int i = 0; i < events.size(); i++) {
                event = (TreeEvent) events.get(i);
                if (event == null) {
                    continue;
                }
                if (event.isError()) {
                    //System.out.println(ip + " -- Error: table OID [" + tableOid + "] " + event.getErrorMessage());
                    continue;
                }
                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    continue;
                }
                for (VariableBinding varBinding : varBindings) {
                    if (varBinding == null) {
                        continue;
                    }

                    result.put("." + varBinding.getOid().toString(), varBinding.getVariable().toString());
                }
            }
            snmp.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // parse last element from key
    public int stringKeyToInt(String key) {
        int i = -1;
        String[] ints = key.split("\\.");
        if (ints.length < 2) {
            return i;
        }
        return i = Integer.parseInt(ints[ints.length - 1]);
    }
}
