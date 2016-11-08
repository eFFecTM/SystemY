/**
 * Created by JJTP on 25-10-2016.
 * A Node has a node name (that will be hashed) and IP address.
 * This class contains methods to calculate a node's position in the network and to update it's neighbours
 * when the network changes.
 */
package JJTP_DS_UA;

import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Node
{
    String name;
    Inet4Address ip;
    Node_NameServerRMI NScommunication;
    Node_nodeRMI_ReceiveInterface NodeRMIReceive;
    Node_nodeRMI_Transmit NodeRMITransmit;
    int ownHash,prevHash,nextHash,newNodeHash; //newHash = van nieuwe node opgemerkt uit de multicast
    String newNodeIP;
    boolean onlyNode,leftEdge,rightEdge;


    public Node(Inet4Address ip)
    {
        this.ip = ip;
        //NScommunication = new Node_NameServerRMI();
        bindNodeRMIReceive();
        //startUp();
        //listenMC();
    }

    public void startUp()
    {
        setName();
        sendMyMC();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getStartupInfoFromNS();
        testBoodStrapDiscovery();
    }

    public void getStartupInfoFromNS()
    {
        leftEdge = NScommunication.checkIfLeftEdge(ownHash);
        rightEdge = NScommunication.checkIfRightEdge(ownHash);

        if(NScommunication.checkAmountOfNodes() <= 1) //Deze check is NA dat de node aan de map is toegevoegd
        {
            onlyNode = true;
            prevHash = ownHash;
            nextHash = ownHash;
        }
        else
            onlyNode=false;
    }

    public void recalcPosition()
    {
        if(onlyNode)
        {
            onlyNode=false;
            updateNewNodeNeighbours(newNodeIP);
            prevHash=newNodeHash;
            nextHash=newNodeHash;
            if(newNodeHash<ownHash)
                leftEdge=false;
            else
                rightEdge = false;
        }
        else
        {
            if(newNodeHash>ownHash && newNodeHash<nextHash)
            {
                updateNewNodeNeighbours(newNodeIP);
                nextHash=newNodeHash;
            }
            else if(newNodeHash<ownHash && newNodeHash>prevHash)
            {
                prevHash=newNodeHash;
            }
            else if(leftEdge)
            {
                if(newNodeHash<ownHash)
                {
                    prevHash=newNodeHash;
                    leftEdge=false;
                }
                else if (newNodeHash>prevHash)
                {
                    prevHash=newNodeHash;
                }
                else if (newNodeHash>ownHash && newNodeHash<nextHash)
                {

                }
            }
            else if(rightEdge)
            {
                if(newNodeHash<nextHash)
                {
                    updateNewNodeNeighbours(newNodeIP);
                    nextHash=newNodeHash;
                }
                else if(newNodeHash>ownHash)
                {
                    updateNewNodeNeighbours(newNodeIP);
                    nextHash=newNodeHash;
                    rightEdge=false;
                }
            }
        }
    }

    public void updateNewNodeNeighbours(String ipAddr)
    {
        NodeRMITransmit = new Node_nodeRMI_Transmit(ipAddr);
        NodeRMITransmit.updateNewNodeNeighbours(35,723);
    }

    public Inet4Address getIP()
    {
        return ip;
    }

    public String getName()
    {
        return name;
    }

    public int calcHash(String name)
    {
        return Math.abs(name.hashCode()%32768);
    }

    public void setName()
    {
        System.out.println("Choose a name for the node and press enter, fill in the correct ip-address and press enter.");
        Scanner s = new Scanner(System.in);
        name = s.nextLine();
        while(name.contains(" ") || NScommunication.checkIfNameExists(name))
        {
            System.out.println("Your name contains a white space or already exists, please choose another name.");
            name = s.nextLine();
        }
        ownHash = calcHash(name);
    }

    public void sendMyMC()
    {
        try
        {
            int portMC = 12345; // Multicast Port waarop men gaat sturen
            Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
            // Multicast IP range: 224.0.0.0 - 239.255.255.255
            byte[] msg = (name + " " + ip.getHostAddress()).getBytes(); // naam en adres zijn gescheiden door een spatie

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setAddress(IPMC);
            packet.setPort(portMC);
            socket.send(packet);

            System.out.println("Multicast message send.");
            socket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void listenMC()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    int portMC = 12345;
                    Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
                    MulticastSocket mcSocket;
                    mcSocket = new MulticastSocket(portMC);
                    mcSocket.joinGroup(IPMC);
                    DatagramPacket packet;

                    while(true)
                    {
                        packet = new DatagramPacket(new byte[1024], 1024);
                        System.out.println("Waiting for a  multicast message...");
                        mcSocket.receive(packet);
                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());;
                        String[] info = msg.split(" "); // het ontvangen bericht splitsen in woorden gescheiden door een spatie
                        newNodeHash = calcHash(info[0]);
                        newNodeIP = info[1];
                        recalcPosition();
                        testBoodStrapDiscovery();
                        System.out.println("Naam: " + info[0]);
                        System.out.println("IP: " + info[1]);
                    }
                } catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void bindNodeRMIReceive() {
        try {
            String name = "NodeSet";
            NodeRMIReceive = new Node_nodeRMI_Receive(this);
            Node_nodeRMI_ReceiveInterface stub =
                    (Node_nodeRMI_ReceiveInterface) UnicastRemoteObject.exportObject(NodeRMIReceive, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }

    public void testBoodStrapDiscovery()
    {
        System.out.println("PrevHash: " + prevHash);
        System.out.println("NextHash: " + nextHash);
        System.out.println("ownHash: " + ownHash);
        System.out.println("FirstNode: " + onlyNode);
        System.out.println("leftEdge: " + leftEdge);
        System.out.println("rightEdge: " + rightEdge);
    }
}
