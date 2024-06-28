package gui.remote;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class HardwareGraph extends JPanel{
    public final static int SPACE = 25;
    public final static int GRAPH_WIDTH = 300;
    public final static int GRAPH_HEIGHT = 150;

    private String title;
    private ArrayList<Integer> values;
    private Rectangle bounds;
    private int maxValue;

    public HardwareGraph(String title) {
        this();
        this.title = title;
    }

    public HardwareGraph() {
        this.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        this.setBackground(Color.decode("#F7F7F7")); // Blanco claro

        this.values = new ArrayList<>();
        this.bounds = new Rectangle(30, 20, GRAPH_WIDTH - 60, GRAPH_HEIGHT - 40);
        this.maxValue = 100; // Valor máximo del gráfico
    }

    public void addValue(double value) {
        if (value < 0) value = 0;
        if (value > 1) value = 1;
        int fixedValue = (int) Math.ceil(value * maxValue);
        this.values.add(fixedValue);
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo del gráfico
        g2d.setColor(Color.decode("#F2F2F2")); // Gris claro
        g2d.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Dibujar título del gráfico
        g2d.setColor(Color.decode("#333333")); // Negro
        g2d.setFont(new Font("Open Sans", Font.BOLD, 14));
        g2d.drawString(this.title, 10, g2d.getFontMetrics().getAscent());

        // Dibujar grid del gráfico
        g2d.setColor(Color.decode("#CCCCCC")); // Gris medio
        g2d.setStroke(new BasicStroke(1f));
        for (int i = 1; i < maxValue; i += 10) {
            g2d.drawLine(this.bounds.x, this.bounds.y + (maxValue - i) * this.bounds.height / maxValue, this.bounds.x + this.bounds.width, this.bounds.y + (maxValue - i) * this.bounds.height / maxValue);
        }

        // Dibujar valores del gráfico
        g2d.setColor(Color.decode("#4CAF50")); // Verde claro
        g2d.setStroke(new BasicStroke(2f));
        int x = this.bounds.x + this.bounds.width - (this.values.size() - 1) * SPACE;
        for (int i = 0; i < this.values.size(); i++) {
            int y = this.bounds.y + (maxValue - this.values.get(i)) * this.bounds.height / maxValue;
            g2d.drawLine(x, y, x + SPACE, y);
            x += SPACE;
        }

        // Dibujar última etiqueta del gráfico
        g2d.setColor(Color.decode("#4CAF50")); // Verde claro
        g2d.setFont(new Font("Open Sans", Font.PLAIN, 12));
        int lastValue = this.values.get(this.values.size() - 1);
        g2d.drawString(lastValue + "%", this.bounds.x + this.bounds.width + 10, this.bounds.y + (maxValue - lastValue) * this.bounds.height / maxValue);
    }
}
