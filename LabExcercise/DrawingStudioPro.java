import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawingStudioPro extends JFrame {

    private DrawingPanel drawingPanel;

    public DrawingStudioPro() {
        setTitle("Drawing Studio Pro");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        JButton penColorBtn = new JButton("Pen Color");
        JButton clearBtn = new JButton("Clear Drawing");

        toolbar.add(penColorBtn);
        toolbar.add(clearBtn);

        // Left Canvas (Composition Area)
        JPanel leftCanvas = new JPanel();
        leftCanvas.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Image Composition Area (Left Canvas)");
        leftCanvas.add(label);

        // Right Canvas (Drawing Area)
        drawingPanel = new DrawingPanel();

        // Split Pane for Dual Canvas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftCanvas, drawingPanel);
        splitPane.setDividerLocation(300);

        // Actions
        penColorBtn.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(this, "Choose Pen Color", drawingPanel.getPenColor());
            if (selectedColor != null) {
                drawingPanel.setPenColor(selectedColor);
            }
        });

        clearBtn.addActionListener(e -> drawingPanel.clear());

        // Layout
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
