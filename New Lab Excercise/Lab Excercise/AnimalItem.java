import java.awt.*;
import java.awt.geom.AffineTransform;

public class AnimalItem extends CreationItem {

    private boolean flipped = false;

    public AnimalItem(int x, int y, Image image) {
        super(x, y, image);
    }

    public void flip() {
        flipped = !flipped;
    }

    @Override
    public void draw(Graphics2D g) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        Graphics2D g2 = (Graphics2D) g.create();

        AffineTransform transform = new AffineTransform();

        // Move to item's position
        transform.translate(x + width / 2, y + height / 2);

        // Apply rotation
        transform.rotate(Math.toRadians(getRotationAngle()));


        // Flip if needed
        if (flipped) {
            transform.scale(-1, 1); // horizontal flip
        }

        // Draw image centered at (0,0)
        transform.translate(-width / 2, -height / 2);
        g2.drawImage(image, transform, null);
        g2.dispose();
    }
}
