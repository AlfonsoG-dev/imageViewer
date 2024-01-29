package Interface.Panels;

import java.awt.BorderLayout;
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
    private Point start, end;
    public PanelPrincipal(int width, int height) {
        createUI(width, height);
    }
    public ImageIcon loadImage() {
        myImage = null;
        try {
            String imagePath = ".\\docs\\astronaut-guardian.jpg";
            ImageIcon toIcon = new ImageIcon(imagePath);
            Image original = toIcon.getImage();
            int imageWidth = myFrame.getWidth()-200;
            int imageHeight = myFrame.getHeight()-100;
            Image newSize = original.getScaledInstance(
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
    public void repaint(Image orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig, 0, 0, null);
        g.setColor(Color.BLACK);
        if (captureRect == null) {
            return;
        }
        g.draw(captureRect);
        g.setColor(new Color(25, 25, 23, 10));
        g.fill(captureRect);
        g.dispose();
    }
    public JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());
        
        Image origen = loadImage().getImage();
        bufferedImage = new BufferedImage(
                origen.getWidth(null),
                origen.getHeight(null),
                BufferedImage.SCALE_SMOOTH
        );
        imageLabel = new JLabel(new ImageIcon(bufferedImage));

        repaint(origen, bufferedImage);
        imageLabel.repaint();

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                repaint(origen, bufferedImage);
                imageLabel.repaint();
            }
            @Override
            public void mouseDragged(MouseEvent me) {
                end = me.getPoint();
                captureRect = new Rectangle(
                        start,
                        new Dimension(end.x - start.x, end.y - start.y)
                );
                repaint(origen, bufferedImage);
                imageLabel.repaint();
            }
        });

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
                targetWidth, // widht
                targetHeight // height
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
    private JPanel setOptionsContent() {
        JPanel optionsPane = new JPanel();
        optionsPane.setLayout(new FlowLayout());

        JButton cutButton = new JButton("cut");
        cutButtonHandler(cutButton);
        optionsPane.add(cutButton);

        optionsPane.add(new JButton("draw"));
        optionsPane.add(new JButton("undo"));

        return optionsPane;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame("Image Viewer");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(width, height);

        myFrame.add(setPrincipalContent(), BorderLayout.CENTER);
        myFrame.add(setOptionsContent(), BorderLayout.SOUTH);

        myFrame.pack();
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
