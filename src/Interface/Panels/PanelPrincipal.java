package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Color;
import java.awt.Point;

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelPrincipal {
    private JFrame myFrame;
    private JPanel pPrincipal;
    private ImageIcon myImage;
    private JLabel imageLabel;
    private Rectangle captureRect;
    private BufferedImage bufferedImage;
    private int imageWidth, imageHeight;
    private Image origen;
    private Point start, end;
    private String imagePath;
    public PanelPrincipal(int width, int height) {
        imagePath = ".\\docs\\astronaut-guardian.jpg";
        //imagePath = "D:/Default/Proyectos/Cursos/Curso-java/Tutorials/Joogle/docs/expected_output_lm.png";
        createUI(width, height);
    }
    private ImageIcon loadImage() {
        myImage = null;
        try {
            BufferedImage readImage = ImageIO.read(new File(imagePath));
            imageWidth = readImage.getWidth() > myFrame.getWidth() ?
                myFrame.getWidth()-200 : readImage.getWidth();

            imageHeight = readImage.getHeight() > myFrame.getHeight() ? 
                myFrame.getHeight()-100 : readImage.getHeight();

            Image newSize = readImage.getScaledInstance(
                    imageWidth,
                    imageHeight,
                    Image.SCALE_SMOOTH
            );
            myImage = new ImageIcon(newSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myImage;
    }
    private void drawShape() {
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(origen, 0, 0, null);
        g.setColor(Color.RED);
        if (captureRect == null) {
            return;
        }
        g.draw(captureRect);
        g.setColor(new Color(25, 25, 23, 10));
        g.fill(captureRect);
        g.dispose();
    }
    private void createMouseSelection() {
        drawShape();
        imageLabel.repaint();

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                drawShape();
                imageLabel.repaint();
            }
            @Override
            public void mouseDragged(MouseEvent me) {
                end = me.getPoint();
                captureRect = new Rectangle(
                        start,
                        new Dimension(end.x - start.x, end.y - start.y)
                );
                drawShape();
                imageLabel.repaint();
            }
        });

    }
    private void setImageState() {
        origen = loadImage().getImage();
        bufferedImage = new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.SCALE_SMOOTH
        );
    }
    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new GridLayout(1,1));
        
        imageLabel = new JLabel(new ImageIcon(bufferedImage));
        drawShape();
        pPrincipal.add(imageLabel);

        return pPrincipal;
    }
    private BufferedImage cropImageToSelection() throws IOException {

        int targetWidth = captureRect.width;
        int targetHeight = captureRect.height;
        // Crop
        BufferedImage croppedImage = bufferedImage.getSubimage(
                captureRect.x, 
                captureRect.y,
                targetWidth,
                targetHeight
        );
        return croppedImage;
    }
    private void cutButtonHandler(JButton cutButton) {
        cutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // TODO: change the save image to a JFileChooser
                    int options = JOptionPane.showConfirmDialog(
                            myFrame,
                            "Save the image?",
                            "Save",
                            JOptionPane.YES_NO_OPTION
                    );
                    if(options == JOptionPane.YES_OPTION) {
                        ImageIO.write(
                                cropImageToSelection(),
                                "jpg",
                                new File(".\\docs\\output.jpg")
                        );
                    }
                } catch(Exception er) {
                    er.printStackTrace();
                } finally {
                    captureRect = null;
                }
            }
        });
    }
    private void drawButtonHandler(JButton drawButton) {
        drawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createMouseSelection();
            }
        });
    }

    private void undoButtonHandler(JButton undoButton) {
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                captureRect = null;
            }
        });
    }
    private JPanel setOptionsContent() {
        JPanel optionsPane = new JPanel();
        optionsPane.setLayout(new FlowLayout());

        JButton cutButton = new JButton("cut");
        cutButtonHandler(cutButton);
        optionsPane.add(cutButton);

        JButton drawButton = new JButton("draw");
        drawButtonHandler(drawButton);
        optionsPane.add(drawButton);

        JButton undoButton = new JButton("undo");
        undoButtonHandler(undoButton);
        optionsPane.add(undoButton);

        return optionsPane;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame("Image Viewer");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(width, height);

        setImageState();

        myFrame.add(setPrincipalContent(), BorderLayout.CENTER);
        myFrame.add(setOptionsContent(), BorderLayout.SOUTH);

        myFrame.pack();
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
