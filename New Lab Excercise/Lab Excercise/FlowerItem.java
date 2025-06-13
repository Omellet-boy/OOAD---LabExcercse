import java.awt.*;
import java.awt.geom.AffineTransform;

public class FlowerItem extends CreationItem {

    private double scale = 1.0;

    public FlowerItem(int x, int y, Image image) {
        super(x, y, image);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = Math.max(0.1, scale); // prevent too small/negative
    }

    @Override
    public void draw(Graphics2D g) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        Graphics2D g2 = (Graphics2D) g.create();

        AffineTransform transform = new AffineTransform();
        transform.translate(x + width / 2, y + height / 2);
        transform.rotate(Math.toRadians(getRotationAngle()));
        transform.scale(scale, scale);
        transform.translate(-width / 2, -height / 2);

        g2.drawImage(image, transform, null);
        g2.dispose();
    }
}
