package Interface.Panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectionPanel {

    private JFrame myFrame;
    private JPanel pPrincipal;
    private JFileChooser chooseImage;

    public SelectionPanel() { 
        createUI(600, 400);
    }

    private JPanel setPrincipalContent() {
        pPrincipal = new JPanel();
        pPrincipal.setLayout(new FlowLayout());

        chooseImage = new JFileChooser();
        chooseImage.setFileSelectionMode(JFileChooser.FILES_ONLY);

        chooseImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooseImage.showOpenDialog(myFrame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    new PanelPrincipal(
                            myFrame,
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
