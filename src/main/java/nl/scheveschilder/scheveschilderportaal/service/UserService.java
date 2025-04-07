package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.UserDto;
import nl.scheveschilder.scheveschilderportaal.models.Role;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.repositories.RoleRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepos;
    private final RoleRepository roleRepos;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepos, RoleRepository roleRepos, PasswordEncoder encoder) {
        this.userRepos = userRepos;
        this.roleRepos = roleRepos;
        this.encoder = encoder;
    }

    public String createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setEmail(userDto.email);
        newUser.setPassword(encoder.encode(userDto.password));

        Set<Role> userRoles = new HashSet<>();
        for (String roleName : userDto.roles) {
            roleRepos.findById("ROLE_" + roleName).ifPresent(userRoles::add);
        }
        newUser.setRoles(userRoles);

        userRepos.save(newUser);
        return "User created successfully";
    }
}

