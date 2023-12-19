package portaldapecuaria.com.mariozinho.model;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private User user;
    private Company company;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
