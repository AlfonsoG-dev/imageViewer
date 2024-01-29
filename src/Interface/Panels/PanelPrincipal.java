package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelPrincipal {
    private JFrame myFrame;
    private JPanel pPrincipal;
    private ImageIcon myImage;
    private JLabel imageLabel;
    private Rectangle captureRect;
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
            Image newSize = original.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            myImage = new ImageIcon(newSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myImage;
    }
    public JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());
        
        Image origen = loadImage().getImage();
        BufferedImage bufferedImage = new BufferedImage(origen.getWidth(null), origen.getHeight(null), BufferedImage.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(bufferedImage));

        repaint(origen, bufferedImage);
        imageLabel.repaint();

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            Point start = new Point();
            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                repaint(origen, bufferedImage);
                imageLabel.repaint();
            }
            @Override
            public void mouseDragged(MouseEvent me) {
                Point end = me.getPoint();
                captureRect = new Rectangle(start, new Dimension(end.x - start.x, end.y
                            - start.y));
                repaint(origen, bufferedImage);
                imageLabel.repaint();
            }
        });

        pPrincipal.add(imageLabel);

        return pPrincipal;
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
    private void resizeButtonHandler(JButton resizeButton) {
        resizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(captureRect.height + "::" + captureRect.width);
            }
        });
    }
    private JPanel setOptionsContent() {
        JPanel optionsPane = new JPanel();
        optionsPane.setLayout(new FlowLayout());

        JButton resizeButton = new JButton("resize");
        resizeButtonHandler(resizeButton);
        optionsPane.add(resizeButton);

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
