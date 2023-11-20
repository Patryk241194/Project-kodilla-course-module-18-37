package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrelloBoard {

    private String id;
    private String name;
    private List<TrelloList> lists;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrelloBoard that)) return false;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getLists() != null ? getLists().equals(that.getLists()) : that.getLists() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getLists() != null ? getLists().hashCode() : 0);
        return result;
    }
}
