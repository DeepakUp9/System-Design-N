package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.ReportingTool;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport.MultipleFormateExport;

public class Details extends ReportingTool{

    public Details(MultipleFormateExport multipleFormateExport) {
        super(multipleFormateExport);
    }

    @Override
    public void fetch() {
      multipleFormateExport.fetch();
    }
    
}
