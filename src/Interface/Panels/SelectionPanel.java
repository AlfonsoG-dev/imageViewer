package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SelectionPanel extends JFileChooser {

    private JFrame myFrame;
    private JFrame mainFrame;
    private JPanel pPrincipal;
    private File parentFile;

    public SelectionPanel(JFrame mainFrame) { 
        super();
        this.mainFrame = mainFrame;
        try {
            parentFile = new File("D:\\Default\\Documentos\\img pc");
            createUI(600, 400);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void openImageViewer(String imagePath, String imageExtension) {
        switch(imageExtension) {
            case "jpg":
                new PanelPrincipal(
                        1900,
                        1050,
                        imagePath
                );
                myFrame.dispose();
            break;
            /*
             case "png":
                break;
            */
            default:
                JOptionPane.showMessageDialog(
                        myFrame,
                        String.format("cannot load format [%s] for imageViewer", imageExtension),
                        "Load error",
                        JOptionPane.ERROR_MESSAGE
                );
            break;
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
        pPrincipal = new JPanel();
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

        myFrame.add(setPrincipalContent());

        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setVisible(true);
    }
}
