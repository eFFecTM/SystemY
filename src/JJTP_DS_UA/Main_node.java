/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.net.*;
import java.lang.String;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;


// Onder: Node
public class Main_node
{
    static Node node;
    static GUI gui;

    public static void main(String[] args) throws SocketException, UnknownHostException
    {
        node = new Node();
        gui = new GUI(); // Zet dit in commentaar als men de GUI tijdelijk niet nodig heeft
        setNodeName();
        node.listenMC();

        gui.logOutButtonActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                node.shutDown();
            }
        });

        gui.tableCellMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int row,column;
                String fileName;
                //logica voor cel
                if(e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();
                    row = target.getSelectedRow();
                    column = target.getSelectedColumn();
                    System.out.println("Row: "+row+" Column: "+column);

                    if(column != 0)
                    {
                        fileName = gui.tableModel.getValueAt(row,0).toString();
                        node.manageFile(fileName, column); // column: 1=open, 2=delete, 3=delete local
                    }
                }
            }
        });

    }

    public static void addFileToTable(String fileName) //todo: bron van files is de filelijst dat de node van de agent krijgt
    {
        //gui.tableModel.addRow(new Object[]{fileName,"Open","Delete","Delete Local"});
    }

    public static void deleteFileFromTable(String fileName)
    {
//        int i;
//        for(i=0;i<gui.tableModel.getRowCount();i++)
//        {
//            if(fileName == gui.tableModel.getValueAt(i,0))
//            {
//                gui.tableModel.removeRow(i);
//            }
//        }
    }

    public static void refreshGUI(ConcurrentHashMap<String, Boolean> systemYfiles)
    {
        gui.refreshGUI(systemYfiles);
    }

    private static void setNodeName()
    {
        String name;
        name = gui.setNodeName();
        while(!node.checkName(name))
        {
            name = gui.setNodeName();
        }
        gui.openPanel();
        node.startUp(name); // bevat sendMC(), getStartupInfoFromNS() en testBootstrapDiscovery()
    }

    public static void showDeleteMessage(String fileName)
    {
        gui.showDeleteMessage(fileName);
    }

    public static void showDeleteNetworkMessage()
    {
        gui.showDeleteNetworkMessage();
    }

}
