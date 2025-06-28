package LLD.DesignPattern.creationalPattern.prototypePattern.firstResource.deepcopy;

import java.util.ArrayList;
import java.util.List;

public class NetworkConnection implements Cloneable {
    
    private String ip;
    private String importantData;
    private List<String>domains = new ArrayList<>();

    public String getIp(){
        return  ip;
    }

    public String getImportantData(){
       return importantData;
    }


    public void setImportantData(String importantData){
        this.importantData = importantData;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public List<String>getDomains(){
        return domains;
    }

    public void setDomains(List<String>domains){
        this.domains = domains;
    }

    public void loadVeryImportantData(){
        //it will take 5minutes
        this.importantData = "Very very important data";

        domains.add("www.google.com");
        domains.add("www.abbs.com");
        domains.add("www.goofacegle.com");
        domains.add("www.facebook.com");
        domains.add("www.google.com");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        // logic for cloning for Deep copy 
        NetworkConnection networkConnection = new NetworkConnection();
        networkConnection.setIp(this.getIp());
        networkConnection.setImportantData(this.getImportantData());

        for(String d : this.getDomains()){
            networkConnection.getDomains().add(d);
        }

        return networkConnection;
    }
}