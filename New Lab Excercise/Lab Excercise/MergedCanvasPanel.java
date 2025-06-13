import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class MergedCanvasPanel extends JPanel {

    private BufferedImage mergedImage;
    private double rotationAngle = 0;

    public MergedCanvasPanel(BufferedImage image) {
        this.mergedImage = image;
        setBackground(Color.WHITE);
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
        repaint();
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mergedImage == null) return;

        Graphics2D g2 = (Graphics2D) g.create();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g2.translate(centerX, centerY);
        g2.rotate(Math.toRadians(rotationAngle));
        g2.translate(-mergedImage.getWidth() / 2, -mergedImage.getHeight() / 2);

        g2.drawImage(mergedImage, 0, 0, null);

        g2.dispose();
    }
}
