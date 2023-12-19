package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandProduct implements Serializable {
    private String uid;
    private String uidCommand;
    private String uidProduct;

    private String  uidUser = "Mariozinho";
    private String name;
    private String situation; //Receber
    private double unitary_value;
    private Integer product_quantity;
    private Date date_d;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;//Setando ID automatica
    }

    public String getUidCommand() {
        return uidCommand;
    }

    public void setUidCommand(String uidCommand) {
        this.uidCommand = uidCommand;
    }

    public String getUidProduct() {
        return uidProduct;
    }

    public void setUidProduct(String uidProduct) {
        this.uidProduct = uidProduct;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public double getUnitary_value() {
        return unitary_value;
    }

    public void setUnitary_value(double unitary_value) {
        this.unitary_value = unitary_value;
    }

    public Integer getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(Integer product_quantity) {
        this.product_quantity = product_quantity;
    }


    public Date getDate_d() {
        return date_d;
    }

    public void setDate_d() {
        Date date_c_new = new Date();
        this.date_d = date_c_new;
    }

    public String formatDate(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/YY - HH:mm");
            String date_cp = df.format(date);
            return date_cp;
        } catch (Exception e) {
            return "Anterior 15/12/2021";
        }

    }

    public String formatDateID(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
        String date_cp = df.format(date);
        return date_cp;
    }

    public double totalValue() {
        return this.unitary_value * this.product_quantity;
    }

    //Numeros formatados
    public String product_Value_format() {
        return "R$ " + new DecimalFormat("#,##0.00").format(this.unitary_value);
    }

    public String totalValue_format() {
        return "R$ " + new DecimalFormat("#,##0.00").format(totalValue());
    }

    public String Description() {
        return formatDate(this.date_d) + "   /   " + this.getProduct_quantity() + " x " + this.product_Value_format();
    }

    public void insert(String uidCommand, String uidProduct, String name, Double unitary_value, int product_quantity) {
        this.setDate_d();
        this.setUid(this.formatDateID(this.date_d) + "_" + uidCommand + "_" + uidProduct);
        this.setUidCommand(uidCommand);
        this.setUidProduct(uidProduct);
        this.setName(name.replaceAll("\\.", ""));
        this.setSituation("receive");
        this.setUnitary_value(unitary_value);
        this.setProduct_quantity(product_quantity);
    }

    @Override
    public String toString() {
        return "| " + name + " | " + product_quantity + "x " + product_Value_format() + " = " + totalValue_format();
    }
}
