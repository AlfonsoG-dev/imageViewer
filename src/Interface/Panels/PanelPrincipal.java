package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.http.HttpResponse.BodyHandler;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelPrincipal {
    private JFrame myFrame;
    private JPanel pPrincipal;
    private ImageIcon myImage;
    private JLabel imageLabel;

    public PanelPrincipal() {
        createUI(1200, 900);
    }
    private ImageIcon loadImage() {
        myImage = null;
        try {
            String imagePath = ".\\docs\\astronaut-guardian.jpg";
            ImageIcon toIcon = new ImageIcon(imagePath);
            Image original = toIcon.getImage();
            int width = (int)(myFrame.getWidth()-200);
            int height = (int)(myFrame.getHeight()-200);
            Image newSize = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            myImage = new ImageIcon(newSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myImage;
    }
    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new GridLayout(1, 1));
        
        if(loadImage() == null) {
            imageLabel = new JLabel("cannot load image");
        } else {
            imageLabel = new JLabel(loadImage());
        }

        pPrincipal.add(imageLabel);

        return pPrincipal;
    }
    private JPanel setHeaderContent() {
        JPanel headerPane = new JPanel();
        headerPane.setLayout(new GridLayout(1, 1));
        headerPane.add(new JLabel("Image: "));
        return headerPane;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame("Image Viewer");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(width, height);

        myFrame.add(setHeaderContent(), BorderLayout.NORTH);
        myFrame.add(setPrincipalContent(), BorderLayout.CENTER);

        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
