import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class CompositionPanel extends JPanel {

    private List<CreationItem> items = new ArrayList<>();
    private CreationItem selectedItem = null;
    private Point initialDragPoint = null;
    private double rotationAngle = 0;

    public CompositionPanel() {
    setBackground(Color.LIGHT_GRAY);

    
        new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable t = dtde.getTransferable();
                    java.util.List<File> droppedFiles = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                    for (File file : droppedFiles) {
                        if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                            Image image = ImageIO.read(file);
                            addItem(new AnimalItem(100, 100, image)); // Default to AnimalItem; you may add logic to switch
                        }
                    }

                    repaint();
                } catch (UnsupportedFlavorException | IOException ex) {
                    JOptionPane.showMessageDialog(CompositionPanel.this, "Failed to load image: " + ex.getMessage());
                }
            }
        }, true);

        // Mouse listener: Selection + flip on click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialDragPoint = e.getPoint();

                for (int i = items.size() - 1; i >= 0; i--) {
                    CreationItem item = items.get(i);
                    if (item.contains(e.getX(), e.getY())) {
                        selectedItem = item;

                        if (item instanceof AnimalItem) {
                            ((AnimalItem) item).flip();
                        }

                        repaint();
                        return;
                    }
                }

                selectedItem = null;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                initialDragPoint = null;
            }
        });

        // Drag item around
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedItem != null && initialDragPoint != null) {
                    int dx = e.getX() - initialDragPoint.x;
                    int dy = e.getY() - initialDragPoint.y;

                    selectedItem.setX(selectedItem.getX() + dx);
                    selectedItem.setY(selectedItem.getY() + dy);

                    initialDragPoint = e.getPoint();
                    repaint();
                }
            }
        });

        // Flower scaling via scroll
        addMouseWheelListener(e -> {
            if (selectedItem instanceof FlowerItem flower) {
                double scale = flower.getScale();
                scale += e.getWheelRotation() * -0.1;
                scale = Math.max(0.1, scale); // prevent scale <= 0
                flower.setScale(scale);
                repaint();
            }
        });
    }


    public void addItem(CreationItem item) {
        items.add(item);
        repaint();
    }

    public CreationItem getSelectedItem() {
        return selectedItem;
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
        repaint();
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public BufferedImage getMergedImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g2.translate(centerX, centerY);
        g2.rotate(Math.toRadians(rotationAngle));
        g2.translate(-centerX, -centerY);

        for (CreationItem item : items) {
            item.draw(g2);
        }

        g2.dispose();
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g2.translate(centerX, centerY);
        g2.rotate(Math.toRadians(rotationAngle));
        g2.translate(-centerX, -centerY);

        for (CreationItem item : items) {
            item.draw(g2);

            if (item == selectedItem) {
                Image img = item.getImage();
                if (img != null) {
                    int x = item.getX();
                    int y = item.getY();
                    int w = img.getWidth(null);
                    int h = img.getHeight(null);
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRect(x, y, w, h);
                }
            }
        }

        g2.dispose();
    }
}
