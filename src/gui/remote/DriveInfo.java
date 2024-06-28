package gui.remote;

import java.io.Serializable;

public class DriveInfo implements Serializable{
    private final String[] info;

    public DriveInfo(String name, long free, long total) {
        this.info = new String[] {name, String.valueOf(free), String.valueOf(total)};
    }

    public String getName() {
        return info[0];
    }

    public long getFreeSpace() {
        return Long.parseLong(info[1]);
    }

    public long getTotalSpace() {
        return Long.parseLong(info[2]);
    }
}
