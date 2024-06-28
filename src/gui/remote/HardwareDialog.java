package gui.remote;

import java.awt.Dimension;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import bus.rmi.RemoteDesktopInterface;

public class HardwareDialog extends JDialog implements Runnable {

    public final static int WIDTH_DIALOG = 500;
    public final static int HEIGHT_DIALOG = 540;
    public final static int HEIGHT_PANEL = 160;

    private HardwareGraph cpu_graphics;
    private HardwareGraph memory_graphics;
    private DrivesInfoPanel process_info_panel;
    private JScrollPane process_scroll;

    private RemoteDesktopInterface remote_obj;
    private Thread update_thread;

    public HardwareDialog(JFrame owner, RemoteDesktopInterface remote_obj) throws RemoteException {
        super(owner);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("System Monitor");
        this.setResizable(false);
        this.setIconImage(
                new ImageIcon(this.getClass().getClassLoader().getResource("images/da")).getImage());
        this.getContentPane().setPreferredSize(new Dimension(HardwareDialog.WIDTH_DIALOG, HardwareDialog.HEIGHT_DIALOG));
        this.setLayout(null);
        this.pack();

        this.remote_obj = remote_obj;

        this.initComponents();

        this.update_thread = new Thread(this);
        this.update_thread.setDaemon(true);
        this.update_thread.start();
    }

    private void initComponents() {
        this.cpu_graphics = new HardwareGraph("CPU Usage");
        this.memory_graphics = new HardwareGraph("Memory Usage");
        this.process_info_panel = new DrivesInfoPanel();
        this.process_scroll = new JScrollPane();

        this.cpu_graphics.setLocation(0, 20);
        this.add(this.cpu_graphics);

        this.memory_graphics.setLocation(0, this.cpu_graphics.getLocation().y + HardwareDialog.HEIGHT_PANEL + 20);
        this.add(this.memory_graphics);

        this.process_scroll.setViewportView(this.process_info_panel);

        this.process_scroll.setLocation(0, this.memory_graphics.getLocation().y + HardwareDialog.HEIGHT_PANEL + 20);
        this.process_scroll.setSize(this.process_info_panel.getSize());
        this.add(this.process_scroll);
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.cpu_graphics.addValue(this.remote_obj.getCpuUsage());
                this.memory_graphics.addValue(this.remote_obj.getMemoryUsage());
                this.process_info_panel.updateInfo(this.remote_obj.getHardwareInfo("Windows 10"));
                Thread.sleep(500);
            }
        } catch (Exception e) {
            this.setVisible(false);
        }
    }

    @Override
    public void dispose() {
        super.setVisible(false);
        super.dispose();
        if (!this.update_thread.isInterrupted())
            this.update_thread.interrupt();
    }

}
