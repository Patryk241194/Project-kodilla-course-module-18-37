package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrelloCard {
    private String name;
    private String description;
    private String pos;
    private String listId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrelloCard that)) return false;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getPos() != null ? !getPos().equals(that.getPos()) : that.getPos() != null) return false;
        return getListId() != null ? getListId().equals(that.getListId()) : that.getListId() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getPos() != null ? getPos().hashCode() : 0);
        result = 31 * result + (getListId() != null ? getListId().hashCode() : 0);
        return result;
    }
}
