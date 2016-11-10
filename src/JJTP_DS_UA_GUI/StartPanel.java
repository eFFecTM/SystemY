package JJTP_DS_UA_GUI;

import JJTP_DS_UA.Main_node;
import JJTP_DS_UA.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by JJTP on 9/11/2016.
 */
public class StartPanel  extends JPanel
{
    JButton logOn;
    JTextField nameTextField;
    Main_node main_node;

    public StartPanel(Main_node main_node)
    {
        JFrame frame = new JFrame("Fileserver");
        new GridBagLayout();
        this.main_node = main_node;
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2,2,2,2);



        /**
         * name textfield
         */
        JLabel nameLabel = new JLabel("Give your node name.");
        g.gridx = 0;
        g.gridy = 1;
        this.add(nameLabel, g);
        nameTextField = new JTextField();
        //nameTextField.setColums(17);
        g.gridwidth = 2;
        g.gridx = 1;
        g.gridy = 1;
        this.add(nameTextField, g);

        /**
         * add button
         */
        JButton addButton = new JButton("add Node");
        g.gridwidth = 1;
        g.gridx = 2;
        g.gridy = 1;
        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(nameTextField.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Give your node name.");
                    return;
                }
                main_node.getNode().startup(nameTextField.getText());
            }
        });
    }

}
