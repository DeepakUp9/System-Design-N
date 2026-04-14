package LLD.DesignPattern.creationalPattern.factory.firstResource.abstractDesignPattern;



public class DeveloperClient{

   public static void main(String args[]){

      Employee employee = EmployeeFactory.getEmployee(new AndroidDevFactory());
      System.out.println(employee.salary());
      System.out.println(employee.skills().toString());
      System.out.println(employee.name());

   

      System.out.println();

      Employee employee2 = EmployeeFactory.getEmployee(new WebDevFactory());
      System.out.println(employee2.salary());
      System.out.println(employee2.skills().toString());
      System.out.println(employee2.name());


   }
}