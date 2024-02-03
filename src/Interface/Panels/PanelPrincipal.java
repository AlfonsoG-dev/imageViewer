package Interface.Panels;


import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Mundo.TreatImage;

public class PanelPrincipal {
    private JFrame myFrame;
    private JPanel pPrincipal;
    private JLabel imageLabel;
    private Rectangle captureRect;
    private BufferedImage bufferedImage;
    private Image origen;
    private Point start, end;
    private TreatImage treatImage;
    public PanelPrincipal(int width, int height, String imageSelected) {
        treatImage = new TreatImage(
                imageSelected,
                width,
                height
        );
        createUI(width, height);
    }
    private void setImageState() {
        origen = treatImage.getOriginalImage();
        bufferedImage = treatImage.getBufferedImage();
    }
    private void createMouseSelection() {
        treatImage.drawShapeImage(
                origen,
                bufferedImage,
                captureRect
        );
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
    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new GridLayout(1,1));
        
        imageLabel = new JLabel(new ImageIcon(bufferedImage));
        treatImage.drawShapeImage(
                origen,
                bufferedImage,
                captureRect
        );
        pPrincipal.add(imageLabel);

        return pPrincipal;
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
                        if(captureRect != null) {
                            ImageIO.write(
                                    treatImage.getCropImageToSelection(
                                        captureRect,
                                        bufferedImage
                                    ),
                                    "jpg",
                                    new File(".\\docs\\images\\output.jpg")
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    myFrame,
                                    "cannot save when nothing is selected",
                                    "Error save",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
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
                treatImage.drawShapeImage(
                        origen,
                        bufferedImage,
                        captureRect
                );
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
