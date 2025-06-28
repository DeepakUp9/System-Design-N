package LLD.DesignPattern.creationalPattern.factory.firstResource.factoryDesignpattern;

public class EmployeeFactory{

  //get the Employee, this method is static 
  public static Employee getEmployee(String empType){
    if(empType.trim().equalsIgnoreCase("Android Developer"))
      return new AndroidDeveloper();
    else if (empType.trim().equalsIgnoreCase("Web Developer"))
      return new WebDeveloper();
    else 
      return null;
  }
  
}