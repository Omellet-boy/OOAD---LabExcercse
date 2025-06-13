import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DrawingStudioPro extends JFrame {

    private DrawingPanel drawingPanel;
    private CompositionPanel compositionPanel;

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void saveImage(BufferedImage image) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "png", "jpg"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String format = "png";

            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                format = "jpg";
            } else if (!file.getName().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

            try {
                ImageIO.write(image, format, file);
                JOptionPane.showMessageDialog(this, "Image saved to: " + file.getAbsolutePath());
            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public DrawingStudioPro() {
        setTitle("Drawing Studio Pro");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        JButton penColorBtn = new JButton("Pen Color", loadScaledIcon("icons/PenColour.png", 20, 20));
        JButton penStroke = new JButton("Pen Stroke", loadScaledIcon("icons/PenStroke.png", 20, 20));
        JButton clearBtn = new JButton("Clear Drawing", loadScaledIcon("icons/Reload.png", 20, 20));
        JButton addAnimalBtn = new JButton("Add Animal", loadScaledIcon("icons/InsertAnimal.png", 20, 20));
        JButton addFlowerBtn = new JButton("Add Flower", loadScaledIcon("icons/InsertFlower.png", 20, 20));
        JButton rotateBtn = new JButton("Rotate Selected", loadScaledIcon("icons/Rotate.png", 20, 20));
        JButton rotateCanvasBtn = new JButton("Rotate Left Canvas", loadScaledIcon("icons/Rotate.png", 20, 20));
        JButton saveDrawingBtn = new JButton("Save Drawing", loadScaledIcon("icons/SaveImage.png", 20, 20));
        JButton mergeCanvasBtn = new JButton("Merge Canvas", loadScaledIcon("icons/Merge.png", 20, 20));
        JSlider rotationSlider = new JSlider(-180, 180, 0);


        toolbar.add(penColorBtn);
        toolbar.add(penStroke);
        toolbar.add(clearBtn);
        toolbar.add(addAnimalBtn);
        toolbar.add(addFlowerBtn);
        toolbar.add(rotateBtn);
        toolbar.add(rotateCanvasBtn);
        toolbar.add(saveDrawingBtn);
        toolbar.add(mergeCanvasBtn);

        // Composition panel with slider below it
        compositionPanel = new CompositionPanel();
        JPanel compositionWithSlider = new JPanel(new BorderLayout());
        compositionWithSlider.add(compositionPanel, BorderLayout.CENTER);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        sliderPanel.add(new JLabel(" Rotate Canvas: "), BorderLayout.WEST);
        sliderPanel.add(rotationSlider, BorderLayout.CENTER);

        compositionWithSlider.add(sliderPanel, BorderLayout.SOUTH);

        // Drawing panel (right side, no slider)
        drawingPanel = new DrawingPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, compositionWithSlider, drawingPanel);
        splitPane.setDividerLocation(600);

        // Button Actions
        penColorBtn.addActionListener(_ -> {
            Color selectedColor = JColorChooser.showDialog(this, "Choose Pen Color", drawingPanel.getPenColor());
            if (selectedColor != null) {
                drawingPanel.setPenColor(selectedColor);
            }
        });

        penStroke.addActionListener(e -> {
            String stroke = JOptionPane.showInputDialog(this, "Enter pen stroke size (1-10):", "Pen Stroke", JOptionPane.PLAIN_MESSAGE);
            if (stroke != null) {
                try {
                    int size = Integer.parseInt(stroke);
                    if (size >= 1 && size <= 10) {
                        drawingPanel.setPenStroke(size);
                    } else {
                        JOptionPane.showMessageDialog(this, "Stroke size must be between 1 and 10.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid stroke size.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        clearBtn.addActionListener(e -> drawingPanel.clear());

        addAnimalBtn.addActionListener(e -> {
            File folder = new File("assets/animals");
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

            if (files != null && files.length > 0) {
                String[] options = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    options[i] = files[i].getName();
                }

                String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select an animal to add:",
                    "Animal Library",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                if (selected != null) {
                    String path = "assets/animals/" + selected;
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage();
                    compositionPanel.addItem(new AnimalItem(50, 50, img));
                }
            } else {
                JOptionPane.showMessageDialog(this, "No animal images found in assets/animals.");
            }
        });

        addFlowerBtn.addActionListener(e -> {
            File folder = new File("assets/flower");
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

            if (files != null && files.length > 0) {
                String[] options = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    options[i] = files[i].getName();
                }

                String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a flower to add:",
                    "Flower Library",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                if (selected != null) {
                    String path = "assets/flower/" + selected;
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage();
                    compositionPanel.addItem(new FlowerItem(50, 50, img));
                }
            } else {
                JOptionPane.showMessageDialog(this, "No flower images found in assets/flower.");
            }
        });

        rotateBtn.addActionListener(e -> {
            CreationItem selected = compositionPanel.getSelectedItem();
            if (selected != null) {
                double current = selected.getRotationAngle();
                selected.setRotationAngle(current + 15);
                compositionPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "No item selected.");
            }
        });

        rotateCanvasBtn.addActionListener(e -> {
            double current = compositionPanel.getRotationAngle();
            compositionPanel.setRotationAngle(current + 15);
        });

        saveDrawingBtn.addActionListener(e -> {
            String[] options = {"Left Canvas (Composition)", "Right Canvas (Drawing)"};
            int choice = JOptionPane.showOptionDialog(
                this,
                "Which canvas would you like to save?",
                "Save Canvas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == 0) {
                saveImage(compositionPanel.getMergedImage());
            } else if (choice == 1) {
                saveImage(drawingPanel.getCanvasImage());
            }
        });

        mergeCanvasBtn.addActionListener(e -> {
            BufferedImage leftImage = compositionPanel.getMergedImage();
            drawingPanel.setTempImage(leftImage);  // Enables drag-and-drop placement
            JOptionPane.showMessageDialog(this, "You can drag the image to place it on the drawing canvas.");
        });



        // Slider config
        rotationSlider.setMajorTickSpacing(90);
        rotationSlider.setMinorTickSpacing(15);
        rotationSlider.setPaintTicks(true);
        rotationSlider.setPaintLabels(true);
        rotationSlider.addChangeListener(e -> {
            int angle = rotationSlider.getValue();
            compositionPanel.setRotationAngle(angle);
        });

        // Final layout
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawingStudioPro::new);
    }
}
