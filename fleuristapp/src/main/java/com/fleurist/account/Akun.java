// cspell:ignore fleurist
package com.fleurist.account;

public abstract class Akun {
    protected String username;
    protected String password;

    public Akun(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password != null && this.password.equals(password);
    }

    public abstract String getRole();
}
