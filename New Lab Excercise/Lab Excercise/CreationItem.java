import java.awt.*;

public abstract class CreationItem {
    protected int x, y;
    protected Image image;
    private double rotationAngle = 0;

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }


    public CreationItem(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.rotationAngle = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public abstract void draw(Graphics2D g);

    public boolean contains(int mouseX, int mouseY) {
        if (image == null) return false;

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        return mouseX >= x && mouseX <= x + width &&
               mouseY >= y && mouseY <= y + height;
    }
}
