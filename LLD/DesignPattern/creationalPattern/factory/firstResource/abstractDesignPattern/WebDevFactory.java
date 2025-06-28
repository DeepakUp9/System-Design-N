package LLD.DesignPattern.creationalPattern.factory.firstResource.abstractDesignPattern;

public class WebDevFactory extends EmployeeAbstractFactory{

    @Override
    public Employee createEmployee() {
        return new WebDeveloper();
    }
    
}
