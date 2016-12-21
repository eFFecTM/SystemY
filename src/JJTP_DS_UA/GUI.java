package JJTP_DS_UA;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

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
    {
        $$$setupUI$$$();
        setNodeName(); // tijdelijk hier geplaatst om te zetten
        openPanel();
        table.setModel(tableModel);
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setColumnCount(4);
        for (int i = 1; i < 4; i++)
        {
            table.getColumnModel().getColumn(i).setPreferredWidth(30);
        }
    }

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
        if (input!=null)
        {
            name = input;
            while (name.equals(""))
            {
                System.out.println("Please enter a name.");
                JOptionPane.showMessageDialog(null, "Please enter a name.", "Authorization required", JOptionPane.INFORMATION_MESSAGE);
                name = JOptionPane.showInputDialog("Welcome to System Y! Please enter your name.");
            }
            return name;
        } else
        {
            System.exit(0);
        }
        return "noNameEntered";
    }


    void logOutButtonActionListener(ActionListener al)
    {
        logOutButton.addActionListener(al);
    }

    void tableCellMouseListener(MouseAdapter ma)
    {
        table.addMouseListener(ma);
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
        mainPanel.setBackground(new Color(-15850431));
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
        label.setForeground(new Color(-5653755));
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
        table.setSelectionBackground(new Color(-5298680));
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
