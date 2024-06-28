package gui.remote;

public class CpuInfo {
    private String name;
    private double usage;

    public CpuInfo(String name, double usage) {
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return this.name;
    }

    public double getUsage() {
        return this.usage;
    }
}
