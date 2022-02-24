package ru.job4j.shortcut.model;

import java.util.List;
import java.util.Objects;

public class StatLinkListDTO {

    private List<StatLinkDTO> statLinkDTOList;

    public StatLinkListDTO(List<StatLinkDTO> statLinkDTOList) {
        this.statLinkDTOList = statLinkDTOList;
    }

    public List<StatLinkDTO> getStatLinkDTOList() {
        return statLinkDTOList;
    }

    public void setStatLinkDTOList(List<StatLinkDTO> statLinkDTOList) {
        this.statLinkDTOList = statLinkDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatLinkListDTO that = (StatLinkListDTO) o;
        return Objects.equals(statLinkDTOList, that.statLinkDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statLinkDTOList);
    }
}
