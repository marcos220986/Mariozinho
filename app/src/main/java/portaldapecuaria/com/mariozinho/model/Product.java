package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {
    private String barCode = "Código de Barras";//Código de Barras
    private String name = "Limpo";
    private String date; //Data de Cadastro
    private String provider_whatsApp;//Fornecedor
    private double saleValue;// Valor de Venda
    private double supplierValue;//Valor do Fornecessor
    private int totalOutput = 0;
    private String uid;
    private String uidCompany;
    private String uidUser;


    //private List <StockGoods> listEntreies = new ArrayList<>();


    //Construtor
    public Product() {

    }

    //Get e Sets


    public String getUid() {
        return uid;
    }
    public void setUid(String uid) { this.uid = uid; }

    public String getBarCode() { return barCode; }

    public void setBarCode(String barCode) { this.barCode = barCode; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getSupplierValue() {
        return supplierValue;
    }
    public void setSupplierValue(double supplierValue) {
        this.supplierValue = supplierValue;
    }

    public double getSaleValue() {
        return saleValue;
    }
    public void setSaleValue(double saleValue) {
        this.saleValue = saleValue;
    }

    public String getProvider_whatsApp() {
        return provider_whatsApp;
    }
    public void setProvider_whatsApp(String provider_whatsApp) { this.provider_whatsApp = provider_whatsApp;
    }

    //public List<StockGoods> getListEntreies() { return listEntreies; }
    //public void setListEntreies(List<StockGoods> listEntreies) { this.listEntreies = listEntreies; }

    public int getTotalOutput() {
        return totalOutput;
    }
    public void setTotalOutput(int totalOutput) {
        this.totalOutput = totalOutput;
    }

    public String getDate() { return date; }
    public String setDate() {
        Date date_c_new = new Date();
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        String dateString = df.format(date_c_new);
        this.date = dateString;
        return dateString;
    }

    /* public int totalEntriesStock(){//Obter Quantidade de Produtos em estoque
        int totalValue = 0;
        for (int i = 0; i < this.getListEntreies().size(); i++) {
            totalValue += this.getListEntreies().get(i).getThe_amount();
        }
        return totalValue;
    }
    public double valueQuantity (){
        double totalValue = 0;
        if(totalEntriesStock() > 0 ){
            totalValue += totalEntriesStock() * this.getSupplierValue();
        }
        return totalValue;
    } */

    public void insertDB(String name, double supplierValue, double saleValue) {
        this.uid = setDate() + "_" + name.replaceAll("\\.","");
        //this.setBarCode(barCode);
        this.setName(name);
        this.setSaleValue(supplierValue);
        this.setSaleValue(saleValue);
        this.setDate();
    }

    public void updateDB(String name, double supplierValue, double saleValue) {
        this.setName(name);
        this.setSupplierValue(supplierValue);
        this.setSaleValue(saleValue);
        this.setDate();
    }

    @Override
    public String toString() {

        return "| " + name + " (R$ " + saleValue + ") | #  ";
    }


}
