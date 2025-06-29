package LLD.SolidPrinciple;

//Open for extension but closed for modification

class InvoiceDao{
    Invoice invoice;

    public InvoiceDao(Invoice invoice){
        this.invoice = invoice;
    }

    public void saveToDB(){
        //save into the db
    }
}

class InvoiceDao{
    Invoice invoice;

    public InvoiceDao(Invoice invoice){
        this.invoice = invoice;
    }

    public void saveToDB(){
        //save into the db
    }

    public void saveToFile(String filename){
        //save invoice in the file with the given name
    }
}

//above class does not not follow Open/ Closed principles beacuse if allowing to modifing the exiting codebase

//So let's modifed the above code so that it follow Open/ Closed Principles 


interface InvoiceDao {
     void save(Invoice invoice);
}

class DatabaseInvoiceDao implements InvoiceDao {
    @Override
    public void save(Invoice invoice){
        //save to DB
    }
}

class FileInvoiceDao implements InvoiceDao{
    @Override
    public void save(Invoice invoice){
        //save to file
    }
}



