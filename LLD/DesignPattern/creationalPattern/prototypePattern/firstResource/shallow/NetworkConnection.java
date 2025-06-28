package LLD.DesignPattern.creationalPattern.prototypePattern.firstResource.shallow;

public class NetworkConnection implements Cloneable {
    
    private String ip;
    private String importantData;

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

    public void loadVeryImportantData(){
        //it will take 5minutes
        this.importantData = "Very very important data";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}