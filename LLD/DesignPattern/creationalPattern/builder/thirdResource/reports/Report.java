package CreationalDesignPattern.Builder.report;

import java.util.List;


public class Report {
    private String header;
    private String footer;
    private List<String>setContentBlock;

    private Report(reportBuilder rBuilder){
        this.header = rBuilder.header;
        this.footer = rBuilder.footer;
        this.contenBlock = rBuilder.contenBlock;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }

    public List<String> getContenBlock() {
        return contenBlock;
    }

    

    @Override
    public String toString() {
        return String.format("report [header=%s, footer=%s, contenBlock=%s]", header, footer, contenBlock);
    }



    static class ReportBuilder {

        private String header;
        private String footer;
        private List<String>contenBlock;
      
        ReportBuilder(){

        }

        public ReportBuilder setHeader(String header) {
            this.header = header;
            return this;
        }

        public ReportBuilder setFooter(String footer) {
            this.footer = footer;
            return this;
        }

        public ReportBuilder setContenBlock(List<String> contenBlock) {
            this.contenBlock = contenBlock;
            return this;
        }
        
        public Report build(){
            Report r = new Report(this);
            return r;
        }
        
    }
}
