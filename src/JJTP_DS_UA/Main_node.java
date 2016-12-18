/**
 * Created by JJTP on 25/10/2016.
 * This class is the Main class on a node (client computer). It initializes a node.
 */
package JJTP_DS_UA;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        /*
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        node.testFailure("192.168.1.2"); // om te testen?
        */
    }

    private static void fillTable()
    {
        DefaultTableModel tableModel = (DefaultTableModel) gui.table.getModel();
        tableModel.setColumnCount(4);
        for(int i=0;i<5;i++)
        {
            tableModel.addRow(new Object[]{"File Name","Open","Delete","Delete Local"});
        }

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
