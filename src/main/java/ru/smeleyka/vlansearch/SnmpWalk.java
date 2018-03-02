package ru.smeleyka.vlansearch;

import org.snmp4j.mp.SnmpConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SnmpWalk {
    private static final String INCORRECT_ARG = "Incorrect Argument:";
    private static final String OUT_OF_RANGE = "Out of range 1-4096";
    private static final String NO_ARGUMENTS = "No arguments";
    private static final String SPACE = " ";
    private static final String NET = "10.10.";
    private static final String COMMUNITY = "public";
    public static final String VLAN_OID = ".1.3.6.1.2.1.17.7.1.4.3.1.1";
    public static final String NAME_OID = ".1.3.6.1.2.1.1.5.0";
    private static final int RETRIES = 1;
    private static final int TIMEOUT = 1500;
    private static final int VERSION = SnmpConstants.version2c;
    private static Vector<Switch> switches;

    public static void main(String[] args) {
        parseArgs(args);
    }

    //Парсим входные аргументы и решаем что делать
    private static void parseArgs(String[] ar) {
        int vlan = -1;
        try {
            if (ar.length == 0) {
                System.out.println(INCORRECT_ARG + SPACE + NO_ARGUMENTS);
                return;
            }

            vlan = Integer.parseInt(ar[0]);

            if (vlan < 1 || vlan > 4094) {
                System.out.println(INCORRECT_ARG + SPACE + OUT_OF_RANGE);
                return;
            }
        } catch (NumberFormatException name) {
            System.out.println(INCORRECT_ARG + SPACE + name.getMessage());
            return;
        }
        findVlan(vlan);
    }

    //Получаем массив ip
    public static ArrayList getIpArr() {
        ArrayList<String> ipList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 254; j++) {
                ipList.add(NET + i + "." + j);
            }
        }
        return ipList;
    }

    //Создаем потоки с SNMP запросами и наполняем массив switches
    public static void getSwitches(List<String> ipList) {
        switches = new Vector<>();
        List<Thread> snmpThreads = new ArrayList<>();   //Массив потоков
        for (String ip : ipList) {                      //пробегаем по массиву ip адресов
            snmpThreads.add(new Thread(new SnmpThread(ip, COMMUNITY, VLAN_OID, VERSION, RETRIES, TIMEOUT))); //Создаем поток и добавляем его в массив
        }
        try {
            for (Thread t : snmpThreads) { //пробегаем по массиву потоков и запускаем их
                t.start();
            }
            for (Thread t : snmpThreads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addSwitch(Switch sw) {
        switches.add(sw);
    }

    public static void printSwitches() {
        for (Switch s : switches) {
            System.out.println(s);
        }
    }

    public static void findVlan(int vlan) {
        getSwitches(getIpArr());
        for (Switch sw : switches) {
            if (sw.isVlanExists(vlan)) {
                System.out.println(sw.getIp() + "   " + sw.getNumIp() + "   " + sw.getName());

                //System.out.println("Vlan "+vlan+" on "+sw);
            }
        }
    }
}
//На будущее
//ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
//executorService.shutdown();
//executorService.notifyAll();

