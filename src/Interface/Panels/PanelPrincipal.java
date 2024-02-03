package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    public PanelPrincipal(int width, int height, String imageSelected) {
        imagePath = imageSelected;
        createUI(width, height);
    }
    private ImageIcon loadImage() {
        myImage = null;
        try {
            BufferedImage readImage = ImageIO.read(new File(imagePath));
            imageWidth = readImage.getWidth() > myFrame.getWidth() ?
                myFrame.getWidth()-100 : readImage.getWidth();

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
        g.setColor(
                new Color(
                    25,
                    25,
                    23,
                    10
                )
        );
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
                        new Dimension(
                            end.x - start.x,
                            end.y - start.y
                        )
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
                pPrincipal.remove(imageLabel);
                imageLabel = new JLabel(new ImageIcon(bufferedImage));
                drawShape();
                pPrincipal.add(imageLabel);
                pPrincipal.repaint();
                myFrame.pack();
            }
        });
    }
    private void cancelButtonHandler(JButton cancelButton) {
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        myFrame, 
                        "Are you sure?",
                        "Cancel",
                        JOptionPane.YES_OPTION
                );
                if(option == JOptionPane.YES_OPTION) {
                    myFrame.dispose();
                    new SelectionPanel();
                }
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

        JButton cancelButton = new JButton("cancel");
        cancelButtonHandler(cancelButton);
        optionsPane.add(cancelButton);

        return optionsPane;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame("Image Viewer");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(width, height);
        myFrame.setResizable(false);

        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                int option = JOptionPane.showConfirmDialog(
                        myFrame,
                        "Are you sure?",
                        "Closing",
                        JOptionPane.YES_OPTION
                );
                if(option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    myFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        setImageState();

        myFrame.add(setPrincipalContent(), BorderLayout.CENTER);
        myFrame.add(setOptionsContent(), BorderLayout.SOUTH);

        myFrame.pack();
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
