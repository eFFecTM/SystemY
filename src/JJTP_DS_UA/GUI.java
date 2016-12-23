package JJTP_DS_UA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JJTP on 11/11/2016.
 */
public class GUI
{
    private JPanel mainPanel;
    private JButton logOutButton;
    JTable table;
    private JLabel label;

    public GUI()
    {}

    public void openPanel()
    {
        JFrame frame = new JFrame("System Y");
        $$$setupUI$$$(); // generated code bij IntelliJ GUI form (don't edit please!)
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(700, 300);
        frame.pack();
        frame.setVisible(true);
        table.setCellSelectionEnabled(true);

        table.setModel(tableModel);
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setColumnCount(4);
    }

    DefaultTableModel tableModel = new DefaultTableModel()
    {
        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };


    public String setNodeName()
    {
        String input = JOptionPane.showInputDialog("Welcome to System Y! Please enter your name.");
        String name;
        if (input != null)
        {
            name = input;
            while (name.equals(""))
            {
                System.out.println("Please enter a name.");
                JOptionPane.showMessageDialog(null, "Please enter a name.", "Authorization required", JOptionPane.INFORMATION_MESSAGE);
                name = JOptionPane.showInputDialog("Welcome to System Y! Please enter your name.");
            }
            return name;
        }
        else
        {
            System.exit(0);
        }
        return "noNameEntered";
    }

    public void refreshGUI(ConcurrentHashMap<String, Boolean> systemYfiles)
    {
        tableModel.getDataVector().removeAllElements();
        Set<String> keyset = systemYfiles.keySet(); //maak een set van keys van de map van de node van de bestanden waar hij eigenaar van is
        for (String key : keyset) //ga de map af (van de files waar de node eigenaar van is) en put alles in de systemYfiles
        {
            tableModel.addRow(new Object[]{key, "Open", "Delete", "Delete Local"});
        }
    }

    public void showDeleteMessage(String fileName)
    {
        JOptionPane.showMessageDialog(null, "You don't have permission to delete " + fileName + ".", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showDeleteNetworkMessage()
    {
        JOptionPane.showMessageDialog(null, "This will be featured in a later version of this program.");
    }

    void logOutButtonActionListener(ActionListener al)
    {
        logOutButton.addActionListener(al);
    }

    void tableCellMouseListener(MouseAdapter ma)
    {
        table.addMouseListener(ma);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setAutoscrolls(false);
        mainPanel.setBackground(new Color(-12828863));
        mainPanel.setForeground(new Color(-15850431));
        mainPanel.setMinimumSize(new Dimension(450, 450));
        mainPanel.setPreferredSize(new Dimension(450, 450));
        logOutButton = new JButton();
        logOutButton.setFont(new Font(logOutButton.getFont().getName(), logOutButton.getFont().getStyle(), 18));
        logOutButton.setHorizontalAlignment(0);
        logOutButton.setMargin(new Insets(3, 18, 3, 18));
        logOutButton.setMinimumSize(new Dimension(150, 41));
        logOutButton.setPreferredSize(new Dimension(400, 45));
        logOutButton.setText("Log out");
        mainPanel.add(logOutButton, BorderLayout.SOUTH);
        label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        label.setForeground(new Color(-2441684));
        label.setHorizontalAlignment(0);
        label.setHorizontalTextPosition(4);
        label.setIcon(new ImageIcon(getClass().getResource("/file-server.png")));
        label.setText("Network Files");
        mainPanel.add(label, BorderLayout.NORTH);
        table = new JTable();
        table.setAutoCreateRowSorter(false);
        table.setAutoResizeMode(4);
        table.setBackground(new Color(-7622478));
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setEnabled(true);
        table.setFont(new Font("Segoe UI", table.getFont().getStyle(), 14));
        table.setGridColor(new Color(-121837));
        table.setInheritsPopupMenu(false);
        table.setName("");
        table.setOpaque(false);
        table.setPreferredSize(new Dimension(400, 350));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(-9474193));
        table.setSelectionForeground(new Color(-1));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        mainPanel.add(table, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
