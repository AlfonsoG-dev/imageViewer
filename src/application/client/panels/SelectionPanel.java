/**
 * Copyright (C) 2024  Alfonso Gomajoa Achicanoy
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package application.client.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

import javax.swing.WindowConstants;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings({"serial"})
public class SelectionPanel extends JFileChooser {

    private static final String FILE_IMAGES_PATH = "D:\\Descargas\\archivos\\imagenes";
    private JFrame myFrame;
    private JFrame mainFrame;
    private File parentFile;

    public SelectionPanel(JFrame mainFrame) { 
        super();
        this.mainFrame = mainFrame;
        try {
            parentFile = new File(FILE_IMAGES_PATH);
            createUI(600, 400);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void openImageViewer(String imagePath, String imageExtension) {
        if (imageExtension.equals("jpg")) {
                new PanelPrincipal(
                        1900,
                        1050,
                        imagePath
                );
                myFrame.dispose();
        } else {
                JOptionPane.showMessageDialog(
                        myFrame,
                        String.format("cannot load format [%s] for ImageViewer", imageExtension),
                        "Load error",
                        JOptionPane.ERROR_MESSAGE
                );
        }
    }
    @Override
    public void cancelSelection() {
        int option = JOptionPane.showConfirmDialog(
                myFrame,
                "Are you sure?",
                "Cancel",
                JOptionPane.YES_NO_OPTION
        );
        if(option == JOptionPane.YES_OPTION) {
            if(mainFrame == null) {
                System.exit(0);
            } else {
                mainFrame.setEnabled(true);
                myFrame.dispose();
            }
        }
    }
    @Override
    public void approveSelection() {
        String imagePath = this.getSelectedFile().getPath();
        String imageExtension = imagePath.endsWith(".jpg") ? "jpg" : "";
        if(mainFrame == null) {
            openImageViewer(imagePath, imageExtension);
        } else {
            mainFrame.dispose();
            myFrame.dispose();
            new PanelPrincipal(
                1900,
                1050,
                imagePath
            );
        }
    }
    private JPanel setPrincipalContent() {
        JPanel pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());

        this.setCurrentDirectory(parentFile);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);

        pPrincipal.add(this);

        return pPrincipal;
    }
    public void createUI(int width, int height) {
        myFrame = new JFrame();
        myFrame.setLayout(new BorderLayout());
        myFrame.setSize(width, height);

        myFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int option = JOptionPane.showConfirmDialog(
                        myFrame,
                        "Are you sure?",
                        "Closing",
                        JOptionPane.YES_NO_OPTION
                );
                if(option == JOptionPane.NO_OPTION) {
                    myFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
                if(option == JOptionPane.YES_OPTION && mainFrame == null) {
                    System.exit(0);
                } else if(option == JOptionPane.YES_OPTION) {
                    mainFrame.setEnabled(true);
                    myFrame.dispose();
                }
            }
        });

        myFrame.add(setPrincipalContent());

        myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
