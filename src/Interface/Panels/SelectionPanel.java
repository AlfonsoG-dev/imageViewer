package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectionPanel {

    private JFrame myFrame;
    private JPanel pPrincipal;
    private JFileChooser chooseImage;
    private File parentFile;

    public SelectionPanel() { 
        try {
            parentFile = new File("D:\\Default\\Documentos\\img pc");
            createUI(600, 400);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());

        chooseImage = new JFileChooser();
        chooseImage.setCurrentDirectory(parentFile);
        chooseImage.setFileSelectionMode(JFileChooser.FILES_ONLY);

        chooseImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooseImage.showOpenDialog(myFrame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    new PanelPrincipal(
                            1920,
                            1080,
                            chooseImage.getSelectedFile().getPath()
                    );
                    myFrame.dispose();
                }
            }
        });
        pPrincipal.add(chooseImage);

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
