package Interface.Utils;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Mundo.TreatImage;

public class ImageLabelUtil extends JLabel {

    public TreatImage treatImage;
    private BufferedImage bufferedImage;
    private Image origen;
    private Point start, end;
    public Rectangle captureRect;
    
    public ImageLabelUtil(String imagePath, JFrame mainFrame) {
        super();
        treatImage = new TreatImage(
                imagePath,
                mainFrame.getWidth(),
                mainFrame.getHeight()
        );
        createLabel();
    }
    private void setImageState() {
        origen = treatImage.getOriginalImage();
        bufferedImage = treatImage.getBufferedImage();
    }
    public void cropImage() {
        try {
            if(captureRect != null) {
                ImageIO.write(
                        treatImage.getCropImageToSelection(
                            captureRect,
                            bufferedImage
                        ),
                        "jpg",
                        new File("./docs/images/output.jpg")
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "selection cannot be null",
                        "Error save",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void createMouseSelection() {
        treatImage.drawShapeImage(
                origen,
                bufferedImage,
                captureRect
        );
        JLabel imageLabel = this;
        imageLabel.repaint();

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                treatImage.drawShapeImage(
                        origen,
                        bufferedImage,
                        captureRect
                );
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
                treatImage.drawShapeImage(
                        origen,
                        bufferedImage,
                        captureRect
                );
                imageLabel.repaint();
            }
        });

    }
    public void createLabel() {
        setImageState();
        this.setIcon(new ImageIcon(bufferedImage));
        treatImage.drawShapeImage(
                origen,
                bufferedImage,
                captureRect
        );
    }
}
