package LLD.DesignPattern.creationalPattern.factory.firstResource.abstractDesignPattern;

public class EmployeeFactory{

  //get the Employee, this method is static 
  public static Employee getEmployee(EmployeeAbstractFactory factory){
     return factory.createEmployee();
  }
  
}