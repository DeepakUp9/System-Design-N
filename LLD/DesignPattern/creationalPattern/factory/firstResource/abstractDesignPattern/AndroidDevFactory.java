package LLD.DesignPattern.creationalPattern.factory.firstResource.abstractDesignPattern;

public class AndroidDevFactory extends EmployeeAbstractFactory {

    @Override
    public Employee createEmployee() {
       return new AndroidDeveloper();
    }
    
}
