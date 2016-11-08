/**
 * Created by JJTP on 25/10/2016.
 * This class represents a nameserver. The name of a node is hashed and put in a TreeMap, together with it's IP-address.
 * This class has methods to add and delete nodes from the network, and lookup files across the network, using RMI.
 */
package JJTP_DS_UA;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.MulticastSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;
import java.util.TreeMap;

// Boven: Main_NameServer
// Onder: ServerRMI
public class NameServer
{
    TreeMap<Integer,Inet4Address> nodeMap;
    ServerRMI RMIclass;


    // NameServer constructor
    public NameServer()
    {
        nodeMap = new TreeMap<>(); //Treemap met <(hash)nodeNaam,ip>
        bindRMIclass();
        listenMC();
    }

    // Op registerpoort 1099 wordt de serverRMI klasse verbonden op een locatie
    public void bindRMIclass()
    {
        try
        {
            RMIclass = new ServerRMI(this); // RMIclass maken + referentie naar zichzelf doorgeven (voor lookup)
            String bindLocation = "FileServer";
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.bind(bindLocation, RMIclass);
            System.out.println("FileServer Server is ready at:" + bindLocation);
            System.out.println("java RMI registry created.");
        } catch (AlreadyBoundException | RemoteException e)
        {
            System.err.println("java RMI registry already exists.");
        }
    }

    // Berekenen van een hash van een naam (of filenaam)
    public int calcHash(String name)
    {
        return Math.abs(name.hashCode()%32768);
    }

    // Toevoegen van een node
    public void addNode(String name, Inet4Address IP)
    {
        int hash = calcHash(name);
        if(nodeMap.containsKey(hash))
            System.err.println("Node not added, name already taken.");
        else
            nodeMap.put(hash,IP);
    }

    // Verwijderen van een node
    public void deleteNode(String name)
    {
        int hash = calcHash(name);
        if(nodeMap.containsKey(hash))
            nodeMap.remove(hash);
        else
            System.err.println("No such Node.");

    }

    /**
     * Save the TreeMap of hashvalues and IP-addresses using XML.
     */
    public void saveNodeMapXML()
    {
        try
        {
            File file = new File("NodeMap.xml");
            //Marshalling
            JAXBContext context = JAXBContext.newInstance(TreeMap.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(nodeMap,file);
            System.out.println("Converted to XML.");
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

    // Luisteren naar / Ontvangen van een MultiCast
    public void listenMC()
    {
        new Thread(new Runnable() // Draait in een aparte thread
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
                        addNode(info[0],(Inet4Address) Inet4Address.getByName(info[1]));
                        testPrintTreemap();
                        //@FIXME: XML Marshaller fixen: @XmlRootElement
                    }
                } catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // TEST: Afprinten van de treemap na het ontvangen van een packet
    public void testPrintTreemap()
    {
        Set<Integer> keyset = nodeMap.keySet();
        for(int k : keyset)
        {
            System.out.println("ID: " + k + " - ip: " + nodeMap.get(k));
        }
    }
}
