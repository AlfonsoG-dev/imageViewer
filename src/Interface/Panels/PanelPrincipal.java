package Interface.Panels;


import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.FlowLayout;
import java.awt.datatransfer.Transferable;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetAdapter;

import java.util.List;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import Interface.Utils.ImageLabelUtil;

@SuppressWarnings({"unchecked"})
public class PanelPrincipal {
    private JFrame myFrame;
    private JPanel pPrincipal;
    private JLabel imageLabel;
    private String imagePath;
    private ImageLabelUtil imageLabelUtil;
    public PanelPrincipal(int width, int height, String imageSelected) {
        imagePath = imageSelected;
        createUI(width, height);
    }
    public void enableImageDrop() {
        imageLabel.setTransferHandler(new TransferHandler(){
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }
            @Override
            public boolean importData(TransferSupport support) {
                myFrame.dispose();
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

        imageLabel.setDropTarget(new DropTarget(imageLabel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent de) {
                myFrame.dispose();
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
    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());
        imageLabel = imageLabelUtil;
        enableImageDrop();
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
                        imageLabelUtil.cropImage();
                    }
                } catch(Exception er) {
                    er.printStackTrace();
                }
            }
        });
    }
    private void drawButtonHandler(JButton drawButton) {
        drawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imageLabelUtil.createMouseSelection();
            }
        });
    }
    private void undoButtonHandler(JButton undoButton) {
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imageLabelUtil = new ImageLabelUtil(imagePath, myFrame);
                pPrincipal.remove(imageLabel);

                imageLabel = imageLabelUtil;
                pPrincipal.add(imageLabel);
                pPrincipal.repaint();
                myFrame.pack();
                myFrame.setSize(
                        new Dimension(
                            1900,
                            1050
                        )
                );
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
                    new SelectionPanel(null);
                }
            }
        });
    }
    private void loadImageButtonHandler(JButton loadImageButton) {
        loadImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myFrame.setEnabled(false);
                new SelectionPanel(myFrame);
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

        JButton loadImageButton = new JButton("loadImage");
        loadImageButtonHandler(loadImageButton);
        optionsPane.add(loadImageButton);

        JButton cancelButton = new JButton("cancel");
        cancelButtonHandler(cancelButton);
        optionsPane.add(cancelButton);

        return optionsPane;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame("Image Viewer");
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(
                new Dimension(
                    width,
                    height
                )
        );
        myFrame.setResizable(true);

        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                int option = JOptionPane.showConfirmDialog(
                        myFrame,
                        "Are you sure?",
                        "Closing",
                        JOptionPane.YES_NO_OPTION
                );
                if(option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    myFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        myFrame.addWindowStateListener(new WindowAdapter() {
            public void windowStateChanged(WindowEvent we) {
                pPrincipal.remove(imageLabel);
                imageLabelUtil = new ImageLabelUtil(
                        imagePath,
                        myFrame
                );
                imageLabel = imageLabelUtil;
                pPrincipal.add(imageLabel);
                pPrincipal.repaint();
            }
        });

        imageLabelUtil = new ImageLabelUtil(
                imagePath,
                myFrame
        );

        myFrame.add(setPrincipalContent(), BorderLayout.CENTER);
        myFrame.add(setOptionsContent(), BorderLayout.SOUTH);

        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
