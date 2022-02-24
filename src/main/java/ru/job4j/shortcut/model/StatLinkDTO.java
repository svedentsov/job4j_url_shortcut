package ru.job4j.shortcut.model;

import java.util.Objects;

public class StatLinkDTO {

    private String url;
    private int total;

    public StatLinkDTO(String url, int total) {
        this.url = url;
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatLinkDTO that = (StatLinkDTO) o;
        return total == that.total
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, total);
    }
}
