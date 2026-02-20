/**
 * Copyright (C) 2024  Alfonso Gomajoa Achicanoy
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package application.client.panels;


import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import application.client.utils.ImageLabelUtil;

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
    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());
        imageLabel = imageLabelUtil;
        pPrincipal.add(imageLabel);

        return pPrincipal;
    }
    private void cutButtonHandler(JButton cutButton) {
        cutButton.addActionListener(e -> {
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
        });
    }
    private void drawButtonHandler(JButton drawButton) {
        drawButton.addActionListener(e -> imageLabelUtil.createMouseSelection());
    }
    private void undoButtonHandler(JButton undoButton) {
        undoButton.addActionListener(e -> {
            imageLabelUtil = new ImageLabelUtil(
                    imagePath,
                    myFrame
            );
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
        });
    }
    private void cancelButtonHandler(JButton cancelButton) {
        cancelButton.addActionListener(e -> {
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
        });
    }
    private void loadImageButtonHandler(JButton loadImageButton) {
        loadImageButton.addActionListener(e -> {
            myFrame.setEnabled(false);
            new SelectionPanel(myFrame);
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
            @Override
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
                    myFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        myFrame.addWindowStateListener(new WindowAdapter() {
            @Override
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

        myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
