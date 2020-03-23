package org.electromob.college_connect;

public class userinfo {

    public String email;
    public String password;
    public String confirmpassword;

    public userinfo(String email, String password, String confirmpassword) {
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
    }

    public userinfo() {
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

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
