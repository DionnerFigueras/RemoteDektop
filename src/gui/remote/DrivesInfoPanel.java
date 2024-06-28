package gui.remote;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.*;

public class DrivesInfoPanel extends JPanel{
    public final static String FOREGROUND = "0x26A0DA";

    private GroupLayout group_layout;
    private GroupLayout.ParallelGroup h_parallel;
    private GroupLayout.ParallelGroup v_parallel;

    public DrivesInfoPanel() {
        this.setSize(HardwareDialog.WIDTH_DIALOG, HardwareDialog.HEIGHT_PANEL);
        this.setBackground(Color.WHITE);

        this.initComponents();
    }

    private void initComponents() {
        this.group_layout = new GroupLayout(this);
        this.v_parallel = this.group_layout.createParallelGroup();
        this.h_parallel = this.group_layout.createParallelGroup();
        this.group_layout.setHorizontalGroup(this.h_parallel);
        this.group_layout.setVerticalGroup(this.v_parallel);
        this.setLayout(this.group_layout);
    }

    public void updateInfo(ComputerInfo pc_info) {
        EventQueue.invokeLater(() -> {
            this.removeAll();

            JLabel cpu_name_label = new JLabel();
            cpu_name_label.setText("CPU: " + pc_info.getOsName());
            cpu_name_label.setFont(new Font("segoe ui", Font.BOLD | Font.ITALIC, 17));
            cpu_name_label.setForeground(Color.BLUE);
            cpu_name_label.setBounds(30, 0, HardwareDialog.WIDTH_DIALOG - 100, 30);
            this.add(cpu_name_label);

            HardwareGraph cpu_usage_graph = new HardwareGraph("CPU Usage");
            cpu_usage_graph.addValue(pc_info.getCpuInfo().getUsage());
            cpu_usage_graph.setBounds(cpu_name_label.getX(), cpu_name_label.getY() + cpu_name_label.getHeight() + 10,
                    200, 100);
            this.add(cpu_usage_graph);

            this.validate();
            this.revalidate();
            this.repaint();
        });
    }

    @Override
    public Component add(Component comp) {
        super.add(comp);
        this.h_parallel.addGroup(this.group_layout.createSequentialGroup()
                .addContainerGap(comp.getX(), comp.getX())
                .addComponent(comp, comp.getWidth(), comp.getWidth(), comp.getWidth()));
        this.v_parallel.addGroup(this.group_layout.createSequentialGroup()
                .addContainerGap(comp.getY(), comp.getY())
                .addComponent(comp, comp.getHeight(), comp.getHeight(), comp.getHeight()));
        return comp;
    }
}
