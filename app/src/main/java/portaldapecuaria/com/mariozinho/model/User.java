package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.util.Date;

import portaldapecuaria.com.mariozinho.ActionMethods;

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String password;
    private Date date;
    private String uid_company = "Alls";
    private String profile = "Alls";
    private String checker_login = "Alls";//Usado para Guardar Login


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid_company() {
        return uid_company;
    }

    public void setUid_company(String uid_company) {
        this.uid_company = uid_company;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getChecker_login() {
        return checker_login;
    }

    public void setChecker_login(String checker_login) {
        this.checker_login = checker_login;
    }

    public void createUser(String name, String email, String password) {
        this.uid = new ActionMethods().gerDateFormate() + "_" + name;
        this.name = name;
        this.email = email;
        this.password = new ActionMethods().gerMD5(password);
        this.date = date;
    }


}
