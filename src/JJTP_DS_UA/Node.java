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

public class Node
{
    String name;
    Inet4Address ip;
    Node_NameServerRMI NScommunication;
    Node_nodeRMI_Receive NodeRMIReceive;
    Node_nodeRMI_Transmit NodeRMITransmit;
    int ownHash,prevHash,nextHash,newNodeHash; //newHash = van nieuwe node opgemerkt uit de multicast
    String newNodeIP;
    boolean firstNode,leftEdge,rightEdge;


    public Node(String name, Inet4Address ip)
    {
        this.name = name;
        this.ip = ip;
        NScommunication = new Node_NameServerRMI();
        bindNodeRMIReceive();
        ownHash = calcHash(name);

        startUp();
        listenMC();
        System.out.println("test: uit de thread");
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
        Integer hashCode = name.hashCode();
        return (int) Integer.toUnsignedLong(hashCode) % 32768;
    }

    public void getStartupInfoFromNS()
    {
        leftEdge = NScommunication.checkIfLeftEdge(ownHash);
        rightEdge = NScommunication.checkIfRightEdge(ownHash);

        if(NScommunication.checkAmountOfNodes() <= 1) //hij is de eerste <1 is normaal niet mogelijk
        {
            firstNode = true;
            prevHash = ownHash;
            nextHash = ownHash;
        }
    }

    public void recalcPosition()
    {
        if(firstNode)
        {
            firstNode=false;
            updateNewNodeNeighbours(newNodeIP);
            prevHash=newNodeHash;
            nextHash=newNodeHash;
        }
        else
        {
            if(leftEdge)
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
            else if(ownHash<newNodeHash && newNodeHash<nextHash)
            {
                updateNewNodeNeighbours(newNodeIP);
                nextHash=newNodeHash;
            }
            else if(ownHash>newNodeHash && newNodeHash>prevHash)
            {
                prevHash=newNodeHash;
            }
        }
    }

    public void updateNewNodeNeighbours(String ipAddr)
    {
        NodeRMITransmit = new Node_nodeRMI_Transmit(ipAddr);
        NodeRMITransmit.updateNewNodeNeighbours(ownHash,nextHash);
    }

    public void startUp()
    {
        sendMyMC();
        getStartupInfoFromNS();
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

    public void bindNodeRMIReceive()
    {
        try
        {
            NodeRMIReceive = new Node_nodeRMI_Receive(this); //RMIclass maken + referentie naar zichzelf doorgeven (voor buren te plaatsen)
            String bindLocation = "//localhost/NodeSet";
            LocateRegistry.createRegistry(1100);
            Naming.bind(bindLocation, NodeRMIReceive);
            System.out.println("Node is reachable at" + bindLocation);
            System.out.println("java RMI registry created.");
        } catch (MalformedURLException | AlreadyBoundException | RemoteException e) {
            e.printStackTrace();
            System.err.println("java RMI registry already exists.");
        }
    }
}
