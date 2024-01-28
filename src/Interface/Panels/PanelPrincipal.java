package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Image;

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
    public PanelPrincipal(int width, int height) {
        createUI(width, height);
    }
    public ImageIcon loadImage(int width, int height) {
        myImage = null;
        try {
            String imagePath = ".\\docs\\astronaut-guardian.jpg";
            ImageIcon toIcon = new ImageIcon(imagePath);
            Image original = toIcon.getImage();
            int imageWidth = width;
            int imageHeight = height;
            Image newSize = original.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            myImage = new ImageIcon(newSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myImage;
    }
    public JPanel setPrincipalContent(int width, int height) {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());
        
        if(loadImage(width, height) == null) {
            imageLabel = new JLabel("cannot load image");
        } else {
            imageLabel = new JLabel(loadImage(width, height));
        }

        pPrincipal.add(imageLabel);

        return pPrincipal;
    }
    private void resiseButtonHandler(JButton resiseButton) {
        resiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
    }
    private JPanel setOptionsContent() {
        JPanel optionsPane = new JPanel();
        optionsPane.setLayout(new FlowLayout());

        JButton resiseButton = new JButton("resise");
        resiseButtonHandler(resiseButton);
        optionsPane.add(resiseButton);

        optionsPane.add(new JButton("draw"));
        optionsPane.add(new JButton("undo"));

        return optionsPane;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame("Image Viewer");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(width, height);

        myFrame.add(setPrincipalContent(width - 200, height - 200), BorderLayout.CENTER);
        myFrame.add(setOptionsContent(), BorderLayout.SOUTH);

        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
