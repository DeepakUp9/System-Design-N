package LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.MultipleFormateExport.PDF;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.ReportingTool.Summary;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.reportingtool.ReportingTool.ReportingTool;

public class Solution {
    public static void main(String[] args) {
       ReportingTool pdf = new Summary(new PDF());
       pdf.fetch();
    }
}
