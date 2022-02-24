package ru.job4j.shortcut.model;

import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "sites")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id of site must be not null")
    private int id;

    @NotNull(message = "Name of site must be not null")
    @NotBlank(message = "Name of site must be not empty")
    private String nameOfSite;

    @NotNull(message = "Username of site must be not null")
    @NotBlank(message = "Username of site must be not empty")
    private String username;

    @NotNull(message = "Password of site must be not null")
    @NotBlank(message = "Password of site must be not empty")
    private String password;

    public static Site of(String nameOfSite) {
        Site site = new Site();
        site.nameOfSite = nameOfSite;
        site.username = genString();
        site.password = genString();
        return site;
    }

    public static String genString() {
        int length = 7;
        return RandomStringUtils.random(length, true, true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfSite() {
        return nameOfSite;
    }

    public void setNameOfSite(String nameOfSite) {
        this.nameOfSite = nameOfSite;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        Site site = (Site) o;
        return id == site.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
