package ru.smeleyka.vlansearch;

import java.util.ArrayList;

/**
 * Created by smeleyka on 28.12.17.
 */
public class Switch implements Comparable {
    private String name;
    private String ip;
    private long numIp;
    private ArrayList<Integer> vlans;

    public Switch(String ip) {
        this.name = "name " + ip;
        this.ip = ip;
        this.vlans = new ArrayList<Integer>();
        this.numIp = aToN(ip);
    }

    private long aToN(String ip) {
        String[] arr = ip.split("\\.");
        int[] iArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            iArr[i] = Integer.parseInt(arr[i]);
        }
        long number = iArr[0];
        for (int i = 1; i < iArr.length; i++) {
            number <<= 8;
            number |= iArr[i];
        }

        return number;
    }

    public Switch(String ip, ArrayList<Integer> vlans) {
        this.ip = ip;
        this.vlans = vlans;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getNumIp() {
        return numIp;
    }

    public void addVlan(int vlan) {
        this.vlans.add(vlan);
    }

    public String getIp() {
        return ip;
    }

    public ArrayList<Integer> getVlans() {
        return vlans;
    }

    public boolean isVlanExists(int vlan) {
        return this.vlans.contains(vlan);
    }

    private String strVlans() {
        //"NULL" if no vlan or
        //"1, 2, 5 , 6" for print all vlans

        String s = "NULL";
        StringBuilder sb = new StringBuilder();
        if (!vlans.isEmpty()) {
            for (Integer i : vlans) {
                sb.append(i);
                sb.append(" ");
            }
            s = sb.toString();
        }

        return s;
    }

    @Override
    public String toString() {
        return "Switch {" + "ip='" + ip + '\'' + ", vlans= '" + strVlans() + "'}";
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
