package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.ReportingTool;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport.MultipleFormateExport;

public class Summary extends ReportingTool{

    public Summary(MultipleFormateExport multipleFormateExport) {
        super(multipleFormateExport);
    }

    @Override
    public void fetch() {
        multipleFormateExport.fetch();
    }
    
}
