/**
 * Created by JJTP on 25-10-2016.
 * A Node has a node name (that will be hashed) and IP address.
 * This class contains methods to calculate a node's position in the network and to update it's neighbours
 * when the network changes.
 */
package JJTP_DS_UA;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Boven: Main_Node
// Onder: Node_NameServerRMI, Node_nodeRMI_Receive, Node_nodeRMI_Transmit
public class  Node
{
    Node thisNode = this;
    String name, newNodeIP, prevNodeIP, nextNodeIP;
    Inet4Address ip;
    Node_NameServerRMI NScommunication;
    Node_nodeRMI_Receive nodeRMIReceive;
    int ownHash, prevHash, nextHash, newNodeHash, fileNameHash; //newNodeHash = van nieuwe node opgemerkt uit de multicast
    boolean onlyNode, wasOnlyNode, lowEdge, highEdge, shutdown = false, prevHighEdge, isFailed;
    ConcurrentHashMap<String, FileMarker> fileMarkerMap; // markers met key=naam en filemarker object = value: eigenaar
    ConcurrentHashMap<String, Boolean> systemYfiles; // string is filenaam, Boolean = lock op de file
    ArrayList<String> removedFiles;
    File fileDir;
    CopyOnWriteArrayList<File> currentFileList, newFileList; // = alle files die op het systeemstaan ,
    CopyOnWriteArrayList<String> creatorFiles; //lijst van filenamen die deze node in het netwerk bracht

    // Node constructor
    public Node() throws SocketException, UnknownHostException {
        getIP();
        NScommunication = new Node_NameServerRMI();
        bindNodeRMIReceive(); // RMI Node-Node
        fileMarkerMap = new ConcurrentHashMap<>();
        systemYfiles = new ConcurrentHashMap<>();
        removedFiles = new ArrayList<>();
        newFileList = new CopyOnWriteArrayList<File>();
        creatorFiles = new CopyOnWriteArrayList<>();
    }

    // Gebruikt door de GUI
    public void manageFile(String fileName, int column)
    {
        try
        {
            switch(column)
            {
                case 1: // open file action //todo: rekening houden met de agent dat de file eventueel gedownload moet worden

                    boolean isFound = false;

                    for(File file : currentFileList)
                    {
                        if(file.getName().equals(fileName))
                        {
                            isFound = true;
                            Desktop.getDesktop().open(file);
                        }
                    }
                    if(!isFound)
                    {
                        downloadFile(fileName);
                        File file = getFileFromFilename(fileName);
                        Desktop.getDesktop().open(file);
                    }
                    break;
                case 2: // delete from network
                    Main_node.showDeleteNetworkMessage();
                    break;
                case 3: // delete local
                    if(!fileMarkerMap.containsKey(fileName))
                    {
                        File file = getFileFromFilename(fileName);
                        file.delete();
                    }
                    else
                    {
                        Main_node.showDeleteMessage(fileName);
                    }

                    break;
                default:
                    System.out.println("Column is NOT 1, 2 or 3!");
                    break;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Op registerpoort 9876 wordt de Node_nodeRMI_Receive klasse verbonden op een locatie
    public void bindNodeRMIReceive() {
        try {
            nodeRMIReceive = new Node_nodeRMI_Receive(this); //RMIclass maken + referentie naar zichzelf doorgeven (voor buren te plaatsen)
            String bindLocation = "NodeSet";
            Registry reg = LocateRegistry.createRegistry(1099); // Standaardpoort 1099!
            reg.bind(bindLocation, nodeRMIReceive);
            System.out.println("Node is reachable at" + bindLocation);
            System.out.println("java RMI registry created.");
        } catch (AlreadyBoundException | RemoteException e) {
            e.printStackTrace();
            System.err.println("java RMI registry already exists.");
        }
    }

    // Opstarten van de Node: Naam instellen, zijn eigen MultiCast sturen (anderen laten weten) en startup info ophalen
    public void startUp(String name) {
        this.name = name;
        ownHash = calcHash(name);
        sendMC();
        try {
            Thread.sleep(2000); // Belangrijk: Andere Nodes moeten eerst de MC ontvangen
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getStartupInfoFromNS();
        loadFiles();
        updateFiles();
        //receiveFile();
        //testBootstrapDiscovery();
        testSystemYfilesMap();
    }

    public void shutDown() {
        shutdown = true; //overbodig

        if(onlyNode)
            NScommunication.deleteNode(ownHash); //delete eigen node uit de map van de server
        else
        {
            NScommunication.deleteNode(ownHash); //delete eigen node uit de map van de server
            if (NScommunication.getMapsize() == 1) //er schiet nog maar 1 node over
            {
                String lastNodeIP = NScommunication.getLastNodeIP(); //zet de "onlynode" boolean op true van die laatst overgebleven node
                Node_nodeRMI_Transmit nRMIt = new Node_nodeRMI_Transmit(lastNodeIP, this);
                nRMIt.updateOnlyNode();
            }

            //bestandenregeling todo: SHUTDOWN MOET GETEST WORDEN
            for(File file : currentFileList)
            {
                String fileName = file.getName();
                int fileNameHash = calcHash(fileName);
                int nodeHash;

                if(fileMarkerMap.containsKey(fileName) && !creatorFiles.contains(fileName)) // Node is owner + niet creator -> replicated file
                {
                    FileMarker fileMarker = fileMarkerMap.get(fileName);

                    if(fileMarker.creator == prevHash)
                        nodeHash = NScommunication.getNodeFromFilename(prevHash-1); // gebruiken om prev node van jouw prev node te weten te komen
                    else
                        nodeHash = prevHash;

                    fileMarker.ownerID = nodeHash;

                    String ipDest = NScommunication.getIP(nodeHash);
                    boolean askFile = false;

                    Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(ipDest, this);
                    int port = nodeRMIt.negotiatePort(fileName, askFile, ipDest, 0);
                    sendFile(file, ipDest, port);
                    nodeRMIt.updateFileMarkers(fileMarker);
                    fileMarkerMap.remove(fileMarker.fileName);
                }
                else if(!fileMarkerMap.containsKey(fileName) && creatorFiles.contains(fileName)) // Niet owner , wel creator = lokale file
                {
                    int fileOwnerID = NScommunication.getNodeFromFilename(fileNameHash);
                    Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(NScommunication.getIP(fileOwnerID), this);
                    nodeRMIt.notifyOwner(fileName);
                }
                else if(fileMarkerMap.containsKey(fileName) && creatorFiles.contains(fileName)) // Owner en creator : lokale
                {
                    if(fileMarkerMap.get(fileName).downloadList.isEmpty()) // als het bestand nooit gedownload is
                    {
                        Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(prevNodeIP, this);
                        nodeRMIt.removeFile(fileName);
                    }
                    else
                    {
                        Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(prevNodeIP, this);
                        int port = negotiatePort(fileName, false, prevNodeIP, 0);
                        sendFile(file, prevNodeIP, port);
                        nodeRMIt.updateFileMarkers(fileMarkerMap.get(fileName));
                        fileMarkerMap.remove(fileName);
                    }
                }
            }
            updateLeftNeighbour(); //geef zijn linkerbuur aan de rechterbuur
            updateRightNeighbour(); //geeft zijn rechterbuur aan de linkerbuur
        }
        System.exit(0); //terminate JVM
    }

    // Een andere node heeft een lokaal bestand waarvan ik owner ben, die zal uitgezet worden
    public void notifyOwner(String fileName)
    {
        boolean isEmpty = fileMarkerMap.get(fileName).downloadList.isEmpty();

        if(isEmpty) // zoja bestand verwijderen
        {
            fileMarkerMap.remove(fileName);
            removedFiles.add(fileName);
            for (File file : currentFileList)
            {
                if(file.getName().equals(fileName))
                    file.delete();
            }

        }
        else //downloadlijst niet leeg -> creator removen als downloadlocatie (hij shut down)
        {
            fileMarkerMap.get(fileName).creator = -1;
        }
    }

    // Initialisatie: Een naam
    public boolean checkName(String name)
    {
        boolean isCorrectName;
        //System.out.println("Choose a name for the node and press enter.");
        //Scanner s = new Scanner(System.in);
        //name = s.nextLine();
        if (name.contains(" ") || NScommunication.checkIfNameExists(name))
        {
            isCorrectName = false;
            //System.out.println("Your name contains a white space or already exists, please choose another name.");
            //name = s.nextLine();
        }
        else
        {
            isCorrectName = true;
        }
        return isCorrectName;
    }

    // Nakijken of de Node op de laagste en/of hoogste rand zit en is Node de eerste Node in de cirkel?
    public void getStartupInfoFromNS() {
        lowEdge = NScommunication.checkIfLowEdge(ownHash);
        highEdge = NScommunication.checkIfHighEdge(ownHash);

        if (NScommunication.checkAmountOfNodes() <= 1) //Deze check is NA dat de node aan de map is toegevoegd
        {
            onlyNode = true;
            prevHash = ownHash;
            prevNodeIP = ip.toString().substring(1);
            System.out.println("UW IP StringVorm = " + prevNodeIP);
            nextHash = ownHash;
            nextNodeIP = ip.toString().substring(1);
        } else
            onlyNode = false;
    }

    // Sturen van een MultiCast
    public void sendMC() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Luisteren naar / Ontvangen van een MultiCast
    public void listenMC() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    int portMC = 12345;
                    Inet4Address IPMC = (Inet4Address) Inet4Address.getByName("230.0.0.0");
                    MulticastSocket mcSocket;
                    mcSocket = new MulticastSocket(portMC);
                    mcSocket.joinGroup(IPMC);
                    DatagramPacket packet;

                    while (!shutdown) {
                        packet = new DatagramPacket(new byte[1024], 1024);
                        System.out.println("Waiting for a  multicast message...");
                        mcSocket.receive(packet);
                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        String[] info = msg.split(" "); // het ontvangen bericht splitsen in woorden gescheiden door een spatie
                        newNodeHash = calcHash(info[0]);
                        newNodeIP = info[1];
                        recalcPosition();
                        if(wasOnlyNode)
                            startFileAgent();
                        if(newNodeHash == nextHash) //indien de nieuwe node een rechtse buur wordt: update eigenaar van de files.
                            updateFilesOwner();
                        System.out.println("Naam: " + info[0]);
                        System.out.println("IP: " + info[1]);
                    }
                    //shutDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startFileAgent()
    {
        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        FileAgent fileAgent = new FileAgent();
        fileAgent.setCurrentNodeMaps(systemYfiles, fileMarkerMap, removedFiles);
        fileAgent.run();
        Node_nodeRMI_Transmit nodeRMITransmit = new Node_nodeRMI_Transmit(nextNodeIP,this);
        nodeRMITransmit.transferFileAgent(fileAgent);
    }

    public void transferFileAgent(FileAgent agent)
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Main_node.refreshGUI(systemYfiles);
        Node_nodeRMI_Transmit nodeRMITransmit = new Node_nodeRMI_Transmit(nextNodeIP,this);
        nodeRMITransmit.transferFileAgent(agent);
    }

    // Positie (buren) wordt gehercalculeerd door volgend algoritme
    public void recalcPosition() {
        prevHighEdge = false;
        wasOnlyNode = false;
        if (onlyNode) // Enigste Node in de cirkel
        {
            onlyNode = false;
            wasOnlyNode = true;
            updateNewNodeNeighbours(newNodeIP);
            prevHash = newNodeHash;
            prevNodeIP = newNodeIP;
            nextHash = newNodeHash;
            nextNodeIP = newNodeIP;
            if (newNodeHash < ownHash)
                lowEdge = false;
            else
            {
                highEdge = false;
                prevHighEdge = true;
            }
        } else {
            if (newNodeHash > ownHash && newNodeHash < nextHash) {
                updateNewNodeNeighbours(newNodeIP);
                nextHash = newNodeHash;
                nextNodeIP = newNodeIP;
            } else if (newNodeHash < ownHash && newNodeHash > prevHash) {
                prevHash = newNodeHash;
                prevNodeIP = newNodeIP;
            } else if (lowEdge) {
                if (newNodeHash < ownHash) {
                    prevHash = newNodeHash;
                    prevNodeIP = newNodeIP;
                    lowEdge = false;
                } else if (newNodeHash > prevHash) {
                    prevHash = newNodeHash;
                    prevNodeIP = newNodeIP;
                }
            }
            else if (highEdge)
            {
                if (newNodeHash < nextHash)
                {
                    updateNewNodeNeighbours(newNodeIP);
                    nextHash = newNodeHash;
                    nextNodeIP = newNodeIP;
                }
                else if (newNodeHash > ownHash)
                {
                    updateNewNodeNeighbours(newNodeIP);
                    nextHash = newNodeHash;
                    nextNodeIP = newNodeIP;
                    highEdge = false;
                    prevHighEdge = true;
                }
            }
        }
    }

    // Buren van de Nieuwe Node updaten
    public void updateNewNodeNeighbours(String ipAddr) {
        Node_nodeRMI_Transmit nodeRMITransmit = new Node_nodeRMI_Transmit(ipAddr, this);
        nodeRMITransmit.setNeighbours(ownHash, nextHash);
    }

    public void updateLeftNeighbour() {
        Node_nodeRMI_Transmit nodeRMITransmit = new Node_nodeRMI_Transmit(prevNodeIP, this);
        nodeRMITransmit.updateLeftNeighbour(nextHash); //maak connectie met de linkerbuur en geef rechterbuur door
    }

    public void updateRightNeighbour() {
        Node_nodeRMI_Transmit nodeRMITransmit = new Node_nodeRMI_Transmit(nextNodeIP, this);
        nodeRMITransmit.updateRightNeighbour(prevHash);
    }

    public void failureOtherNode(String IP) //ip adrr van falende node
    {
        int[] neighbours = NScommunication.getIDs(IP); //in [0] zit de linkse buur, in [1] zit de rechtse buur
        System.out.println("failureOtherNode: falende node's buren: " + neighbours[0] + " - " + neighbours[1]);
        //FailureAgent failureAgent = new FailureAgent(NScommunication.getID(IP), this, neighbouts[1]);
        if (neighbours[0] == neighbours[1])//in dit geval is deze node de laatste node
        {
            onlyNode = true;
            wasOnlyNode = false;
            prevHash = ownHash;
            prevNodeIP = ip.toString().substring(1);
            nextHash = ownHash;
            nextNodeIP = ip.toString().substring(1);
        } else if (neighbours[0] == ownHash) //deze node is de linkse buur van de gefaalde node
        {
            Node_nodeRMI_Transmit nodeRMITransmitR = new Node_nodeRMI_Transmit(NScommunication.getIP(neighbours[1]), this);
            nodeRMITransmitR.updateLeftNeighbour(neighbours[0]); //verbind met de RECHTSEbuur van de GEFAALDE node en update ZIJN LINKSE buur met de linkse van de gefaalde
            nextHash = neighbours[1]; //update jezelf

        } else if (neighbours[1] == ownHash) //deze node is de rechtse buur van de gefaalde node
        {
            Node_nodeRMI_Transmit nodeRMITransmitL = new Node_nodeRMI_Transmit(NScommunication.getIP(neighbours[0]), this);
            nodeRMITransmitL.updateRightNeighbour(neighbours[1]); //verbind met de LINKSE node van de GEFAALDE node, en update ZIJN RECHTSE buur met de RECHTSE van de gefaalde node
            prevHash = neighbours[0];
        } else {
            System.out.println("failureOtherNode: laatste else");
            Node_nodeRMI_Transmit nodeRMITransmitL = new Node_nodeRMI_Transmit(NScommunication.getIP(neighbours[0]), this);
            nodeRMITransmitL.updateRightNeighbour(neighbours[1]); //verbind met de LINKSE node van de GEFAALDE node, en update ZIJN RECHTSE buur met de RECHTSE van de gefaalde node
            Node_nodeRMI_Transmit nodeRMITransmitR = new Node_nodeRMI_Transmit(NScommunication.getIP(neighbours[1]), this);
            nodeRMITransmitR.updateLeftNeighbour(neighbours[0]); //verbind met de RECHTSEbuur van de GEFAALDE node en update ZIJN LINKSE buur met de linkse van de gefaalde
        }
        NScommunication.deleteNode(NScommunication.getID(IP));
    }

    // Berekenen van een hash van een naam (of filenaam)
    public int calcHash(String name) {
        return Math.abs(name.hashCode() % 32768);
    }

    public void getIP() throws UnknownHostException, SocketException {
        boolean hasIP = false;
        Inet4Address IP = null;
        for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (InetAddress inetAddress : Collections.list(netint.getInetAddresses())) {
                System.out.println("Found IP's: " + inetAddress);
                if (inetAddress.toString().contains("192.168.1.")) {
                    hasIP = true;
                    System.out.println("IP Adres: " + inetAddress);
                    IP = (Inet4Address) inetAddress;
                }
            }
        }
        if (hasIP) {
            ip = IP;

        } else {
            System.out.println("IP not found! Type your local IP manually:");
            //Scanner s = new Scanner(System.in);
            //String ipString = s.nextLine();
            //ip = (Inet4Address) ipString;
        }
    }

    public void loadFiles()
    {
        fileDir = new File("Files"); // gaat naar de "Files" directory in de locale projectmap
        File[] fileArray = fileDir.listFiles(); //maakt een array van alle files in de directory  !! enkel files geen directories zelf
        currentFileList = new CopyOnWriteArrayList<>(Arrays.asList(fileArray));
        for (File file : currentFileList)
        {
            addFile(file);
        }
    }

    public void updateFiles()
    {
        new Thread(new Runnable() {
            public void run()
            {
                while(true)
                {
                    try
                    {
                        Thread.sleep(30000); // Update na elke 30 seconden
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    File[] newFileArray = fileDir.listFiles();
                    newFileList.addAll(Arrays.asList(newFileArray));
                    CopyOnWriteArrayList<File> tempFileList = new CopyOnWriteArrayList<File>();
                    tempFileList.addAll(newFileList);

                    tempFileList.removeAll(currentFileList);
                    currentFileList.addAllAbsent(newFileList);

                    System.out.println("newFilelist size: " + newFileList.size());
                    System.out.println("tempFilelist size: " + tempFileList.size());
                    System.out.println("currentFilelist size: " + currentFileList.size());

                    //Check for new files (not already send or received)
                    if(!tempFileList.isEmpty())
                    {
                        for(int i=0;i<tempFileList.size();i++)
                        {
                            if(!addFile(tempFileList.get(i)))
                            {
                                isFailed = false;
                                addFile(tempFileList.get(i));
                            }
                        }
                    }
                    tempFileList.clear();
                    newFileList.clear();

                    //print all filemarkers
                    Set<String> keyset = fileMarkerMap.keySet(); //maak een set van keys van de map van de node van de bestanden waar hij eigenaar van is
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("---All Files that the Node is owner of---");
                    for(String key : keyset) //ga de map af (van de files waar de node eigenaar van is) en put alles in de systemYfiles
                    {
                        System.out.println("File: "+key);
                    }
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
            }
        }).start();
    }

    public Boolean addFile(File file)
    {
        String fileName = file.getName();

        creatorFiles.add(fileName);
        int fileNameHash = calcHash(file.getName());
        FileMarker fileMarker = new FileMarker(fileName, fileNameHash, ownHash);
        fileMarkerMap.put(fileName, fileMarker); //maak bestandfiche aan en zet in de hashmap
        int fileOwnerID = NScommunication.getNodeFromFilename(fileNameHash);

        if(fileOwnerID == ownHash)
        {
            fileMarker.ownerID = ownHash;
            if(!onlyNode)
            {
                boolean askFile = false;
                Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(prevNodeIP, this);
                if(!isFailed)
                {
                    int port = nodeRMIt.negotiatePort(fileName, askFile, prevNodeIP, 0);
                    sendFile(file, prevNodeIP, port);
                    return true;
                }
                return false;
            }
            return true;
        }
        else
        {
            fileMarker.ownerID = fileOwnerID;

            String ipDest = NScommunication.getIP(fileOwnerID);
            boolean askFile = false;
            Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(ipDest, this);
            if(!isFailed)
            {
                int port = nodeRMIt.negotiatePort(fileName, askFile, ipDest, 0);
                sendFile(file, ipDest, port);

                nodeRMIt.updateFileMarkers(fileMarker);
                fileMarkerMap.remove(fileMarker.fileName);
                return true;
            }
            return false;
        }
    }

    //controleert wanneer een nieuwe node in het netwerk komt of deze node eigenaar wordt van de bestanden (waar deze node eigenaar van is)
    //hier kom je in als de nieuwe node uw nextNode is.
    public void updateFilesOwner()
    {
        try
        {
            Thread.sleep(5000); //geeft de nieuwe node tijd om op te starten
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Set<String> keyset = fileMarkerMap.keySet(); // Map van files waar deze node owner van is
        for (String str : keyset)
        {
            FileMarker fileMarker = fileMarkerMap.get(str);
            fileNameHash = fileMarker.fileNameHash;
            if (fileNameHash > nextHash)   // elke "normale" situatie (of je bent highedge en er komt een leftedge in
            {
                File file = new File("\\Files\\" + fileMarkerMap.get(str).fileName);

                boolean askFile = false;
                Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(newNodeIP, this);
                int port = nodeRMIt.negotiatePort(file.getName(), askFile, newNodeIP, 0);
                sendFile(file, newNodeIP, port);
                if(fileMarker.creator != ownHash)
                    fileMarker.downloadList.add(ownHash); // node zal een downloadlocatie worden
                nodeRMIt.updateFileMarkers(fileMarker);
                fileMarkerMap.remove(str);
            }
            else if(prevHighEdge && fileNameHash < ownHash) //er komt een nieuwe highedge in
            {
                File file = new File("\\Files\\" + fileMarkerMap.get(str).fileName); //opent de file die verstuurd moet worden

                boolean askFile = false;
                Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(newNodeIP, this);
                int port = nodeRMIt.negotiatePort(file.getName(), askFile, newNodeIP, 0);
                sendFile(file, newNodeIP, port);
                if(fileMarker.creator != ownHash)
                    fileMarker.downloadList.add(ownHash); // node zal een downloadlocatie worden
                nodeRMIt.updateFileMarkers(fileMarker);
                fileMarkerMap.remove(str);
            }
            else if(wasOnlyNode)
            {
                File file = new File("\\Files\\" + fileMarkerMap.get(str).fileName);

                boolean askFile = false;
                Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(newNodeIP, this);
                int port = nodeRMIt.negotiatePort(file.getName(), askFile, newNodeIP, 0);

                sendFile(file, newNodeIP, port);
            }

        }
    }

    public void sendFile(File file, String IPdest, int port)
    {
        //Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(IPdest, this);
        //int port = nodeRMIt.negotiatePort();
        String fileLocation = fileDir.toString() + "/" + file.getName();
        System.out.println("\n sendFile: "+fileLocation);
        System.out.println("IP dest: "+IPdest);

        try
        {
            Socket socket = new Socket(IPdest, port);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            oos.writeObject(file.getName());

            //Sending a file
            byte[] b = new byte[1024];
            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            FileInputStream fis = new FileInputStream(fileLocation);
            int i;
            int byteLength = 1024;
            while ((i = fis.read(b, 0, 1024)) != -1)
            {
                //System.out.println("Sending file... " + byteLength + " bytes sent");
                byteLength = byteLength + 1024;
                bos.write(b, 0, i);
                bos.flush();
            }
            socket.shutdownOutput(); // Important: output is being terminated

            //Receiving ACK from the client
            //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //String ack = (String) ois.readObject();
            //System.out.println("Message from the client: " + ack);

            bos.close();
            fis.close();
            oos.close();
            //ois.close();
            socket.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateFileMarker(FileMarker fm)
    {
        fileMarkerMap.put(fm.fileName, fm);
    }

    public void receiveFile(int port)
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ServerSocket serverSocket;

                   // while(true)
                   // {
                        serverSocket = new ServerSocket(port);
                        Socket socket = serverSocket.accept();

                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String fileName = (String) ois.readObject();

                        //Receiving a file from another node
                        byte[] b = new byte[1024];
                        int length;
                        int byteLength = 1024;
                        FileOutputStream fos = new FileOutputStream(fileDir.getName()+ "/" + fileName); //fixme: als het niet werkt: \\
                        System.out.println("\n receiving file: "+fileDir.getName() + "/" + fileName);
                        InputStream is = socket.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is, 1024);
                        while ((length = bis.read(b, 0, 1024)) != -1)
                        {
                            //System.out.println("Receiving file... " + byteLength + " bytes received");
                            byteLength = byteLength + 1024;
                            fos.write(b, 0, length);
                        }

                        //Laat niet toe om een bestand continu heen en weer te laten sturen
                        File file = new File(fileDir.getName() + "/" + fileName);
                        if(!currentFileList.contains(file))
                        {
                            currentFileList.add(file);
                        }

                        //Sending an ACK to the server
                        //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        //oos.flush();
                        //oos.writeObject("ACKNOWLEDGE");

                        bis.close();
                        fos.close();
                        ois.close();
                        //oos.close();
                        socket.close();
                        serverSocket.close();
                    //}
                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int negotiatePort(String filename, boolean askedFile, String ipDest, int port)
    {
        if (askedFile)
        {
            File file = getFileFromFilename(filename);
            sendFile(file, ipDest, port);
        }
        else
        {
            Random rand = new Random();
            port = rand.nextInt((30000 - 10000) + 1) + 10000; // return port tussen 10 000 en 30 000
            receiveFile(port);

        }
        return port;
    }

    public void downloadFile(String filename)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int fileHash = calcHash(filename);
                String ipSrc = NScommunication.getIP(NScommunication.getNodeFromFilename(fileHash));
                boolean askFile = true;
                Node_nodeRMI_Transmit nodeRMIt = new Node_nodeRMI_Transmit(ipSrc, thisNode);

                String ipDest = ip.toString().substring(1);

                Random rand = new Random();
                int port = rand.nextInt((30000 - 10000) + 1) + 10000; // return port tussen 10 000 en 30 000
                receiveFile(port);
                nodeRMIt.negotiatePort(filename, askFile, ipDest, port);

            }

        }).start();
    }

    public File getFileFromFilename(String filename)
    {
        File fileToSend = null;
        for(File file : currentFileList)
        {
            String tempFilename = file.getName();
            if(tempFilename.equals(filename))
            {
                fileToSend = file;
            }
        }
        return fileToSend;
    }

    // TEST: gegevens weergeven van de Node
    public void testBootstrapDiscovery() {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("PrevHash: " + prevHash);
                    System.out.println("NextHash: " + nextHash);
                    System.out.println("ownHash: " + ownHash);
                    System.out.println("FirstNode: " + onlyNode);
                    System.out.println("lowEdge: " + lowEdge);
                    System.out.println("highEdge: " + highEdge);
                }
            }
        }).start();
    }

    public void testFailure(String ip) {
        Node_nodeRMI_Transmit node_rmiObj = new Node_nodeRMI_Transmit(ip, this);
        node_rmiObj.setNeighbours(1234, 1234);
    }

    public void testSystemYfilesMap()
    {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("--------- SysteYfilesize: " + systemYfiles.size() + "----------");
                }
            }
        }).start();
    }
}
