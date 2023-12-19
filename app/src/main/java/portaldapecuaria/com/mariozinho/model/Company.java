package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;
import java.util.List;

public class Company implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String PIX;
    private List <Command> listCommands;
    private List <Product> listProducts;

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

    public String getPIX() {
        return PIX;
    }

    public void setPIX(String PIX) {
        this.PIX = PIX;
    }

    public List<Command> getListCommands() {
        return listCommands;
    }

    public void setListCommands(List<Command> listCommands) {
        this.listCommands = listCommands;
    }

    public List<Product> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<Product> listProducts) {
        this.listProducts = listProducts;
    }
}
