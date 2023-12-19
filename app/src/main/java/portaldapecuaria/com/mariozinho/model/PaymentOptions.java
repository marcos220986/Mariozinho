package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentOptions  implements Serializable {
    private String uid;
    private String name;
    private double ratePayment;//Taxa cobrada
    private double totalOP = 0.0;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRatePayment() {
        return ratePayment;
    }

    public void setRatePayment(double ratePayment) {
        this.ratePayment = ratePayment;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String gerDate() {
        Date date_c_new = new Date();
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        String dateString = df.format(date_c_new);
        return dateString;
    }

    public void insertDB(String name, double ratePayment){
        this.uid = gerDate() + "_" + name.replaceAll("\\.","");
        this.name = name.replaceAll("\\.","");
        this.ratePayment = ratePayment;
    }
    public double calValue(double totalOP){
        totalOP = totalOP * (1+(ratePayment/100));
        return totalOP;
    }

    public void updateDB(String name, double ratePayment){
        this.name = name.replaceAll("\\.","");
        this.ratePayment = ratePayment;
    }
    @Override
    public String toString(){
            return "| " + name + " | Taxa: " + ratePayment;

    }
}
