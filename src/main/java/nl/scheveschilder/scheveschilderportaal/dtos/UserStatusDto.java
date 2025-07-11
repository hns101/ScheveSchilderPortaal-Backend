package nl.scheveschilder.scheveschilderportaal.dtos;

import jakarta.validation.constraints.NotNull;

public class UserStatusDto {
    @NotNull
    public Boolean active;

    // Getter
    public Boolean getActive() {
        return active;
    }

    // Setter
    public void setActive(Boolean active) {
        this.active = active;
    }
}
