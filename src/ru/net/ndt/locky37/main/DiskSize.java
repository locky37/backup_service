package ru.net.ndt.locky37.main;

import java.io.File;
import java.lang.management.ManagementFactory;

public class DiskSize {

    private long diskSize = new File("/").getTotalSpace();
    private String userName = System.getProperty("user.name");
    long maxMemory = Runtime.getRuntime().maxMemory();
    private long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();

    public void prn() {
        System.out.println("Size of C:=" + diskSize + " Bytes");
        System.out.println("User Name=" + userName);

        System.out.println("RAM Size=" + memorySize + " Bytes");
    }

}
