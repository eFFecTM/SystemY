package JJTP_DS_UA;

/**
 * Created by jonas on 19-11-2016.
 */
public class FileMarker
{
    String fileName;
    int fileNameHash;
    public int ownerID; //degene die nu eigenaar is (naar waar het voor het laatst gerepliceerd is)
    int creator; //degene die het bestand origineel in het netwerk bracht
    //bevat ook op welke noden het bestand te vinden is (onderscheid lokaal en gedownload)

    public FileMarker(String fileName, int fileNameHash, int creator)
    {
        this.fileName = fileName;
        this.fileNameHash = fileNameHash;
        this.creator = creator;
    }

    public void setOwnerID(int ownerID)
    {
        this.ownerID = ownerID;
    }
}
