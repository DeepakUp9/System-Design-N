package LLD.DesignPattern.behaviouralPattern.iterator.firstResource;

public class User{
    private String name;
    private String userId;


    public User(String name, String userId){
      this.name = name;
      this.userId = userId;
    }

    public User(){
        
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
      return name;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
      return userId;
    }


}