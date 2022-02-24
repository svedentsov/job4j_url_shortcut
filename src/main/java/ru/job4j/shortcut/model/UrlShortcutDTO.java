package ru.job4j.shortcut.model;

import java.util.Objects;

public class UrlShortcutDTO {

    private String shortcutUrl;

    public UrlShortcutDTO(String shortcutUrl) {
        this.shortcutUrl = shortcutUrl;
    }

    public String getShortcutUrl() {
        return shortcutUrl;
    }

    public void setShortcutUrl(String shortcutUrl) {
        this.shortcutUrl = shortcutUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UrlShortcutDTO that = (UrlShortcutDTO) o;
        return Objects.equals(shortcutUrl, that.shortcutUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortcutUrl);
    }
}
