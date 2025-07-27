package DesignPattern.structuralPattern.adapter.thirdResource.kjava.ClassAdapter;

// Data analytics tool that expects JSON data
// Adaptee
class JSONAnalyticsTool {
    private String pJsonData;

    public void setJsonData(String jsonData) {
        pJsonData = jsonData;
    }

    public void analyzeData() {
        if (pJsonData.contains("json")) {
            System.out.println("Analysing JSON Data - " + pJsonData);
        } else {
            System.out.println("Not correct format. Can't analyse!");
        }
    }
}

// Interface
// Target
interface AnalyticsTool {
    void analyzeData();
}

// Adapter
class XMLToJSONAdapter implements AnalyticsTool {
    private JSONAnalyticsTool jsonAnalyticsTool;

    public XMLToJSONAdapter(String xmlData) {
        System.out.println("Converting the XML Data '" + xmlData + "' to JSON Data!");
        String newData = xmlData + " in json";
        jsonAnalyticsTool = new JSONAnalyticsTool();
        jsonAnalyticsTool.setJsonData(newData);
    }

    @Override
    public void analyzeData() {
        // Could convert here instead of the constructor
        jsonAnalyticsTool.analyzeData();
    }
}

public class XmlToJson {
    public static void main(String[] args) {
        String xmlData = "Sample Data";
        JSONAnalyticsTool tool1 = new JSONAnalyticsTool();
        tool1.setJsonData(xmlData);
        tool1.analyzeData();

        System.out.println("----------------------------------------------");

        AnalyticsTool tool2 = new XMLToJSONAdapter(xmlData);
        tool2.analyzeData();
    }
}