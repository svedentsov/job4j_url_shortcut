package ru.job4j.shortcut.model;

import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id of site must be not null")
    private int id;

    @NotNull(message = "Url of site must be not null")
    @NotBlank(message = "Url of site must be not empty")
    private String url;

    @NotNull(message = "Shortcut url of site must be not null")
    @NotBlank(message = "Shortcut url of site must be not empty")
    private String shortcut;

    private int total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    public static Link of(String url, Site site) {
        Link link = new Link();
        link.url = url;
        link.shortcut = genString();
        link.total = 0;
        link.site = site;
        return link;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return id == link.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
