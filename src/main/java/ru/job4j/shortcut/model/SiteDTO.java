package ru.job4j.shortcut.model;

import java.util.Objects;

public class SiteDTO {

    private boolean registered;
    private String login;
    private String password;

    public SiteDTO(boolean registered, String login, String password) {
        this.registered = registered;
        this.login = login;
        this.password = password;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteDTO siteDTO = (SiteDTO) o;
        return registered == siteDTO.registered
                && Objects.equals(login, siteDTO.login)
                && Objects.equals(password, siteDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registered, login, password);
    }
}
