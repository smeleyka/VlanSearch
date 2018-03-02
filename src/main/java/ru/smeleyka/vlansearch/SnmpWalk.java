package ru.smeleyka.vlansearch;

import java.util.ArrayList;
import java.util.List;

public class SnmpWalk {

    private static MyVector<Switch> switches;

    public static void main(String[] args) {
        parseArgs(args);
        getSwitches(getIpArr());
        printVlan(Integer.parseInt(args[0]));
    }

    //Парсим входные аргументы и решаем что делать
    private static void parseArgs(String[] ar) {
        int vlan = -1;
        try {
            if (ar.length == 0) {
                System.out.println(Resources.INCORRECT_ARG + Resources.SPACE + Resources.NO_ARGUMENTS);
                return;
            }

            vlan = Integer.parseInt(ar[0]);

            if (vlan < 1 || vlan > 4094) {
                System.out.println(Resources.INCORRECT_ARG + Resources.SPACE + Resources.OUT_OF_RANGE);
            }
        } catch (NumberFormatException name) {
            System.out.println(Resources.INCORRECT_ARG + Resources.SPACE + name.getMessage());
        }

    }

    //Получаем массив ip
    private static ArrayList getIpArr() {
        ArrayList<String> ipList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 254; j++) {
                ipList.add(Resources.NET + i + "." + j);
            }
        }
        return ipList;
    }

    //Создаем потоки с SNMP запросами и наполняем массив switches
    private static void getSwitches(List<String> ipList) {
        switches = new MyVector<>();
        List<Thread> snmpThreads = new ArrayList<>();   //Массив потоков
        for (String ip : ipList) {                      //пробегаем по массиву ip адресов
            snmpThreads.add(new Thread(new SnmpThread
                    (ip, Resources.COMMUNITY,
                    Resources.VLAN_OID, Resources.VERSION,
                    Resources.RETRIES, Resources.TIMEOUT))); //Создаем поток и добавляем его в массив
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

    private static void printSwitches() {
        for (Switch s : switches) {
            System.out.printf("%12s%7s",s.getIp(),s.getName());
        }
    }

    private static void printVlan(int vlan) {
        for (Switch s : switches) {
            if (s.isVlanExists(vlan)) {
                System.out.printf("%12s%7s",s.getIp(),s.getName());
            }
        }
    }
}
//На будущее
//ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
//executorService.shutdown();
//executorService.notifyAll();

