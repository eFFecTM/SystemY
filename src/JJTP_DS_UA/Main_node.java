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


// Onder: Node
public class Main_node
{
    static Node node;
    static GUI gui;

    public static void main(String[] args) throws SocketException, UnknownHostException
    {
        //node = new Node();
        gui = new GUI(); // Zet dit in commentaar als men de GUI tijdelijk niet nodig heeft
        fillTable();
        //setNodeName();
        //node.listenMC();
        //node.updateFiles();
        //node.receiveFiles();


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
                        //node.manageFile(String fileName, int column); // column: 1=open, 2=delete, 3=delete local
                    }
                }
            }
        });


    }

    private static void fillTable()
    {
        DefaultTableModel tableModel = (DefaultTableModel) gui.table.getModel();
        tableModel.setColumnCount(4);
        for(int i=1;i<4;i++)
        {
            gui.table.getColumnModel().getColumn(i).setPreferredWidth(30);
        }

        for(int i=0;i<5;i++)
        {
            tableModel.addRow(new Object[]{"File Name","Open","Delete","Delete Local"});
        }

        tableModel.addRow(new Object[]{"z","Open","Delete","Delete Local"});
        //System.out.println("Rijen: " +tableModel.getRowCount() + " Kolommen:" + tableModel.getColumnCount());
    }



    private static void setNodeName()
    {
        String name;
        name = gui.setNodeName();
        while(!node.checkName(name))
        {
            name = gui.setNodeName();
        }
        node.startUp(name); // bevat sendMC(), getStartupInfoFromNS() en testBootstrapDiscovery()
        gui.openPanel();
    }
}
