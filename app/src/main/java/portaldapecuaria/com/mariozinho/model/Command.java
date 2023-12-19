package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Command implements Serializable{
    private String uid;
    private String name;
    private String cel;
    private String situation = "";
    private String date_c;
    private Date date;
    private  List <CommandProduct> listProducts;

    //Construtor
    public Command() {

    }

    //Set e Get
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCel() {
        return cel;
    }
    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getSituation() {
        return situation;
    }
    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getDate_c(){ return date_c; }
    public void setDate_c() { this.date_c = date_c; }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public List<CommandProduct> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<CommandProduct> listProducts) {
        this.listProducts = listProducts;
    }

    public double totalCommandValue(){//Obter Total da Commanda
        double totalValue = 0.00;
        /*
        for (int i = 0; i < this.getListProducts().size(); i++) {
            totalValue += (this.getListProducts().get(i).getUnitary_value() * this.getListProducts().get(i).getProduct_quantity());
        } */
        return totalValue;
    }

    public String gerDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        String dateString = df.format(date);
        return dateString;
    }
    public String dateCreate(){
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/YY - HH:mm");
            String dateString = df.format(getDate());
            return dateString;
        }catch (Exception e){
            return "Anterior a 16/12/21";
        }

    }

    //To String retorno de nome
    public void insertCommand(String name) {
        Date date = new Date();
        this.setDate(date);
        this.setUid(gerDate(date) + "_" + name);
        this.setName(name);
        List <CommandProduct> list = new ArrayList<CommandProduct>();
        this.setListProducts(list);
    }
    public void updateDB(String name, String cel){
        this.setName(name);
        this.setCel(cel);
    }

    @Override

    public String toString(){
        return "Marcos";
    }

}
