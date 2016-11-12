package JJTP_DS_UA;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JJTP on 11/11/2016.
 */
public class GUI extends JFrame
{
    private JPanel viewTab,searchTab;
    private JTextArea file1,file2,file3,file4,file5;
    private JButton openButton,removeButton;
    private JTabbedPane tabbedPane1;
    private JButton logOutButton;
    private JPanel mainPanel;

    public GUI()
    {
        super("System Y");
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2); // set frame in the middle of the screen
        createUIComponents();
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here
    }
}
