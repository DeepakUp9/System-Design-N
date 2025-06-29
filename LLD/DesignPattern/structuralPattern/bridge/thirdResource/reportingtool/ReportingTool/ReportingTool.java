package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.ReportingTool;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport.MultipleFormateExport;

public abstract class ReportingTool {
    protected MultipleFormateExport multipleFormateExport;

    public ReportingTool(MultipleFormateExport multipleFormateExport){
        this.multipleFormateExport = multipleFormateExport; 
    }

    public abstract void fetch();
}
