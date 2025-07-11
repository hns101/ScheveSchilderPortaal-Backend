package nl.scheveschilder.scheveschilderportaal.dtos;

public class StudentDto {
    public Long id;
    public String firstname;
    public String lastname;
    public String defaultSlot;
    public String email; // Linked to User.email
    public boolean active; // --- NEW FIELD ---
}
