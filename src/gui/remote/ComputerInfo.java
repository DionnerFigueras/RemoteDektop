package gui.remote;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ComputerInfo implements Serializable{
    private String osName;
    private Map<String, DriveInfo> driveMap;
    private CpuInfo cpuInfo;

    public ComputerInfo(String osName) {
        this.osName = osName;
        this.driveMap = new HashMap<>();
        this.cpuInfo = new CpuInfo("Intel Core I7", 0.3);
    }

    public String getOsName() {
        return this.osName;
    }

    public void addDrive(String driveName, DriveInfo driveInfo) {
        this.driveMap.put(driveName, driveInfo);
    }

    public DriveInfo getDrive(String driveName) {
        return this.driveMap.get(driveName);
    }

    public CpuInfo getCpuInfo(){
        return this.cpuInfo;
    }
}
