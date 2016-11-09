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
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;

// Boven: Main_Node
// Onder: Node_NameServerRMI, Node_nodeRMI_Receive, Node_nodeRMI_Transmit
public class Node
{
    String name, newNodeIP;
    Inet4Address ip;
    Node_NameServerRMI NScommunication;
    Node_nodeRMI_Receive nodeRMIReceive;
    Node_nodeRMI_Transmit nodeRMITransmit;
    int ownHash, prevHash, nextHash, newNodeHash; //newHash = van nieuwe node opgemerkt uit de multicast
    boolean onlyNode, lowEdge, highEdge, shutdown = false;

    // Node constructor
    public Node(Inet4Address ip)
    {
        this.ip = ip;
        NScommunication = new Node_NameServerRMI();
        bindNodeRMIReceive(); // RMI Node-Node
        startUp(); // bevat setName() en sendMC()
        listenMC();
//      checkForShutdown();
    }

    // Op registerpoort 9876 wordt de Node_nodeRMI_Receive klasse verbonden op een locatie
    // FIXME: Elke node op een andere poort registreren (Jonas: http://i.imgur.com/XNV1bD1.png)
    public void bindNodeRMIReceive()
    {
        try
        {
            nodeRMIReceive = new Node_nodeRMI_Receive(this); //RMIclass maken + referentie naar zichzelf doorgeven (voor buren te plaatsen)
            String bindLocation = "NodeSet";
            Registry reg = LocateRegistry.createRegistry(9876);
            reg.bind(bindLocation, nodeRMIReceive);
            System.out.println("Node is reachable at" + bindLocation);
            System.out.println("java RMI registry created.");
        } catch (AlreadyBoundException | RemoteException e)
        {
            e.printStackTrace();
            System.err.println("java RMI registry already exists.");
        }
    }

    // Opstarten van de Node: Naam instellen, zijn eigen MultiCast sturen (anderen laten weten) en startup info ophalen
    public void startUp()
    {
        setName();
        sendMC();
        try {
            Thread.sleep(500); // Belangrijk: Andere Nodes moeten eerst de MC ontvangen
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        getStartupInfoFromNS();
        testBootstrapDiscovery();
        System.out.println("Type 0 if you want to shutdown Node.");
    }

    public void shutDown()
    {
        //Delete from server
        //recalc neighbors
    }

    // Initialisatie: Een naam kan men kiezen voor de Node
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

    // Nakijken of de Node op de laagste en/of hoogste rand zit en is Node de eerste Node in de cirkel?
    public void getStartupInfoFromNS()
    {
        lowEdge = NScommunication.checkIfLowEdge(ownHash);
        highEdge = NScommunication.checkIfHighEdge(ownHash);

        if(NScommunication.checkAmountOfNodes() <= 1) //Deze check is NA dat de node aan de map is toegevoegd
        {
            onlyNode = true;
            prevHash = ownHash;
            nextHash = ownHash;
        }
        else
            onlyNode = false;
    }

    // Berekenen van een hash van een naam (of filenaam)
    public int calcHash(String name)
    {
        return Math.abs(name.hashCode()%32768);
    }

    // Sturen van een MultiCast
    public void sendMC()
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

    // Luisteren naar / Ontvangen van een MultiCast
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

                    while(!shutdown)
                    {
                        packet = new DatagramPacket(new byte[1024], 1024);
                        System.out.println("Waiting for a  multicast message...");
                        mcSocket.receive(packet);
                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());;
                        String[] info = msg.split(" "); // het ontvangen bericht splitsen in woorden gescheiden door een spatie
                        newNodeHash = calcHash(info[0]);
                        newNodeIP = info[1];
                        recalcPosition();
                        testBootstrapDiscovery();
                        System.out.println("Naam: " + info[0]);
                        System.out.println("IP: " + info[1]);
                    }
                    //shutDown();
                } catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Positie (buren) wordt gehercalculeerd door volgend algoritme
    public void recalcPosition()
    {
        if(onlyNode) // Enigste Node in de cirkel
        {
            onlyNode = false;
            updateNewNodeNeighbours(newNodeIP);
            prevHash = newNodeHash;
            nextHash = newNodeHash;
            if(newNodeHash < ownHash)
                lowEdge = false;
            else
                highEdge = false;
        }
        else
        {
            if(newNodeHash > ownHash && newNodeHash < nextHash)
            {
                updateNewNodeNeighbours(newNodeIP);
                nextHash = newNodeHash;
            }
            else if(newNodeHash < ownHash && newNodeHash > prevHash)
            {
                prevHash = newNodeHash;
            }
            else if(lowEdge)
            {
                if(newNodeHash < ownHash)
                {
                    prevHash = newNodeHash;
                    lowEdge = false;
                }
                else if (newNodeHash > prevHash)
                {
                    prevHash = newNodeHash;
                }
            }
            else if(highEdge)
            {
                if(newNodeHash < nextHash)
                {
                    updateNewNodeNeighbours(newNodeIP);
                    nextHash = newNodeHash;
                }
                else if(newNodeHash > ownHash)
                {
                    updateNewNodeNeighbours(newNodeIP);
                    nextHash = newNodeHash;
                    highEdge = false;
                }
            }
        }
    }


    // Buren van de Nieuwe Node updaten
    public void updateNewNodeNeighbours(String ipAddr)
    {
        nodeRMITransmit = new Node_nodeRMI_Transmit(ipAddr);
        nodeRMITransmit.setNeighbours(ownHash,nextHash);
    }

    // TEST: gegevens weergeven van de Node
    public void testBootstrapDiscovery()
    {
        System.out.println("PrevHash: " + prevHash);
        System.out.println("NextHash: " + nextHash);
        System.out.println("ownHash: " + ownHash);
        System.out.println("FirstNode: " + onlyNode);
        System.out.println("lowEdge: " + lowEdge);
        System.out.println("highEdge: " + highEdge);
    }

    public void checkForShutdown()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while(true) {
                    Scanner s = new Scanner(System.in);
                    if (Objects.equals(s.nextLine(), "0")) {
                        shutdown = true;
                    }
                }
            }
        }).start();

    }
}
