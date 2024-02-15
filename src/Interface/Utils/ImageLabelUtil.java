package Interface.Utils;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetAdapter;

import java.util.List;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import Mundo.TreatImage;
import Interface.Panels.PanelPrincipal;

@SuppressWarnings({"serial", "unchecked"})
public class ImageLabelUtil extends JLabel {

    public TreatImage treatImage;
    private BufferedImage bufferedImage;
    private JFrame mainFrame;
    private Image origen;
    private Point start, end;
    public Rectangle captureRect;
    
    public ImageLabelUtil(String imagePath, JFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
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
    private void enableImageDrop() {
        this.setTransferHandler(new TransferHandler(){
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }
            @Override
            public boolean importData(TransferSupport support) {
                mainFrame.dispose();
                if(!canImport(support)) {
                    return false;
                }
                Transferable transferable = support.getTransferable();
                try{
                    List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if(fileList.size() > 0) {
                        File file = fileList.get(0);
                        new PanelPrincipal(
                                1900,
                                1050,
                                file.getPath()
                        );
                        return true;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        this.setDropTarget(new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent de) {
                mainFrame.dispose();
                de.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable t = de.getTransferable();

                if(t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    de.acceptDrop(DnDConstants.ACTION_COPY);
                    try {
                        List<File> fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        if(fileList.size() > 0) {
                            File file = fileList.get(0);
                            new PanelPrincipal(
                                    1900,
                                    1050,
                                    file.getPath()
                            );
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }
    public void createLabel() {
        setImageState();
        this.setIcon(new ImageIcon(bufferedImage));
        treatImage.drawShapeImage(
                origen,
                bufferedImage,
                captureRect
        );
        enableImageDrop();
    }
}
