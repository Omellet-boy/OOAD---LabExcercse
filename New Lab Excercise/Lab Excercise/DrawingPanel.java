import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class DrawingPanel extends JPanel {
    private BufferedImage image;
    private Graphics2D g2d;
    private int prevX, prevY;
    private Color penColor = Color.BLACK;
    private int penStrokeSize = 2; 

    // Drag-to-position fields
    private BufferedImage tempImage = null;
    private int tempX = 0, tempY = 0;
    private boolean draggingTempImage = false;
    private int dragOffsetX, dragOffsetY;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();

                if (tempImage != null) {
                    int x = e.getX();
                    int y = e.getY();
                    if (x >= tempX && x <= tempX + tempImage.getWidth() &&
                        y >= tempY && y <= tempY + tempImage.getHeight()) {
                        draggingTempImage = true;
                        dragOffsetX = x - tempX;
                        dragOffsetY = y - tempY;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingTempImage) {
                    draggingTempImage = false;

                    // Merge temp image into the drawing
                    if (g2d != null && tempImage != null) {
                        g2d.drawImage(tempImage, tempX, tempY, null);
                        tempImage = null;
                        repaint();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingTempImage) {
                    tempX = e.getX() - dragOffsetX;
                    tempY = e.getY() - dragOffsetY;
                    repaint();
                } else if (g2d != null && tempImage == null) {
                    int x = e.getX();
                    int y = e.getY();
                    g2d.setColor(penColor);
                    g2d.setStroke(new BasicStroke(penStrokeSize)); 
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

        if (image == null || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
            BufferedImage newImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D newG2d = newImage.createGraphics();
            newG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            newG2d.setPaint(Color.WHITE);
            newG2d.fillRect(0, 0, getWidth(), getHeight());

            if (image != null) {
                newG2d.drawImage(image, 0, 0, null);
                g2d.dispose();
            }

            image = newImage;
            g2d = newG2d;
        }

        g.drawImage(image, 0, 0, null);

        if (tempImage != null) {
            g.drawImage(tempImage, tempX, tempY, null);
        }
    }

    public void setPenColor(Color color) {
        this.penColor = color;
    }

    public Color getPenColor() {
        return this.penColor;
    }

    public void setPenStroke(int size) {
        this.penStrokeSize = size;
    }

    public void clear() {
        if (g2d != null) {
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            repaint();
        }
    }

    public BufferedImage getCanvasImage() {
        return image;
    }

    public void setTempImage(BufferedImage img) {
        this.tempImage = img;
        this.tempX = 0;
        this.tempY = 0;
        repaint();
    }
}
