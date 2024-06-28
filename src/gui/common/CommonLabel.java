package gui.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class CommonLabel extends JLabel{
    public final static String HIGHLIGHT_COLOR = "0x646464";
    public final static int NORMAL_FONT_SIZE = 18;
    public final static int HIGHLIGHT_FONT_SIZE = 20;

    public CommonLabel() {
        this.setNormalFont();
        this.setForeground(Color.BLACK);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelMouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelMouseExited(e);
            }
        });
    }

    public void setNormalFont() {
        this.setFont(new Font("Arial", Font.PLAIN, CommonLabel.NORMAL_FONT_SIZE));
    }

    public void setHighlightFont() {
        this.setFont(new Font("Arial", Font.BOLD, CommonLabel.HIGHLIGHT_FONT_SIZE));
    }

    private void labelMouseEntered(MouseEvent e) {
        if (this.isEnabled()) {
            this.setForeground(Color.decode(CommonLabel.HIGHLIGHT_COLOR));
            this.setHighlightFont();
        }
    }

    private void labelMouseExited(MouseEvent e) {
        if (this.isEnabled()) {
            this.setForeground(Color.BLACK);
            this.setNormalFont();
        }
    }
}
