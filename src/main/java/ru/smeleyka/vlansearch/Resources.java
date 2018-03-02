package ru.smeleyka.vlansearch;

import org.snmp4j.mp.SnmpConstants;

public class Resources {
    public static final String INCORRECT_ARG = "Incorrect Argument:";
    public static final String OUT_OF_RANGE = "Out of range 1-4096";
    public static final String NO_ARGUMENTS = "No arguments";
    public static final String SPACE = " ";
    public static final String NET = "10.10.";
    public static final String COMMUNITY = "public";
    public static final String VLAN_OID = ".1.3.6.1.2.1.17.7.1.4.3.1.1";
    public static final String NAME_OID = ".1.3.6.1.2.1.1.5.0";
    public static final int RETRIES = 1;
    public static final int TIMEOUT = 1500;
    public static final int VERSION = SnmpConstants.version2c;
}
