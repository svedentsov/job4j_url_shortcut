package ru.job4j.shortcut.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SiteOnlyNameDTO {

    @NotNull(message = "Name of site must be not null")
    @NotEmpty(message = "Name of site must be not empty")
    private String nameOfSite;

    public static SiteOnlyNameDTO of(String nameOfSite) {
        SiteOnlyNameDTO site = new SiteOnlyNameDTO();
        site.nameOfSite = nameOfSite;
        return site;
    }

    public String getNameOfSite() {
        return nameOfSite;
    }

    public void setNameOfSite(String nameOfSite) {
        this.nameOfSite = nameOfSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteOnlyNameDTO that = (SiteOnlyNameDTO) o;
        return Objects.equals(nameOfSite, that.nameOfSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfSite);
    }
}
