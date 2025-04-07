package nl.scheveschilder.scheveschilderportaal.dtos;

public class AuthResponseDto {
    private String token;
    private String role;

    public AuthResponseDto(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}