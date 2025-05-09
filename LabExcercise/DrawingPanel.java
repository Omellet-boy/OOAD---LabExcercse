import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private BufferedImage image;
    private Graphics2D g2d;
    private int prevX, prevY;
    private Color penColor = Color.BLACK;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (g2d != null) {
                    int x = e.getX();
                    int y = e.getY();
                    g2d.setColor(penColor);
                    g2d.drawLine(prevX, prevY, x, y);
                    repaint();
                    prevX = x;
                    prevY = y;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.drawImage(image, 0, 0, null);
    }

    public void setPenColor(Color color) {
        this.penColor = color;
    }

    public Color getPenColor() {
        return this.penColor;
    }

    public void clear() {
        if (g2d != null) {
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            repaint();
        }
    }
}
