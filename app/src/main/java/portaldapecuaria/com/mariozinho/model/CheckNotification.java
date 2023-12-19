package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckNotification implements Serializable {
    private String uid;
    private Date date;
    private String msChek;
    private String msChekTitle;
    private String channel;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsChek() {
        return msChek;
    }

    public void setMsChek(String msChek) {
        this.msChek = msChek;
    }

    public String getMsChekTitle() {
        return msChekTitle;
    }

    public void setMsChekTitle(String msChekTitle) {
        this.msChekTitle = msChekTitle;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void insert(String msChekTitle, String msChek, String channel) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        this.uid = df.format(date);
        this.date = date;
        this.msChek = msChek;
        this.msChekTitle = msChekTitle;
        this.channel = channel;
    }
}
