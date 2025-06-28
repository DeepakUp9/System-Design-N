package LLD.DesignPattern.creationalPattern.factory.firstResource.factoryDesignpattern;


public class DeveloperClient{

   public static void main(String args[]){
      Employee employee =  EmployeeFactory.getEmployee("Android Developer");
      System.out.println(employee.salary());
      System.out.println(employee.skills());

      System.out.println();

      Employee employee2 =  EmployeeFactory.getEmployee("Web Developer");
      System.out.println(employee2.salary());
   }
}