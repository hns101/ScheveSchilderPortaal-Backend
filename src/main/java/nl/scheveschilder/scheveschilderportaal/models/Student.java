package nl.scheveschilder.scheveschilderportaal.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or AUTO or SEQUENCE depending on your DB
    private Long id;

    private String firstname;
    private String lastname;
    private String defaultSlot;

    private boolean activeMember;

    @ManyToMany(mappedBy = "students")
    private Set<Lesson> lessons = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_email", unique = true)
    private User user;

    // --- Getters and Setters ---

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public boolean isActiveMember() {
        return activeMember;
    }

    public void setActiveMember(boolean activeMember) {
        this.activeMember = activeMember;
    }

    public String getDefaultSlot() {
        return defaultSlot;
    }

    public void setDefaultSlot(String defaultSlot) {
        this.defaultSlot = defaultSlot;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
