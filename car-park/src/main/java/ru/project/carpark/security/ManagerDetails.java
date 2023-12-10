package ru.project.carpark.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import ru.project.carpark.entity.Manager;

import java.util.Collection;
import java.util.Collections;

public class ManagerDetails extends User {

    private final Manager manager;

    public ManagerDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities,
                          Manager manager) {
        super(username, password, authorities);
        this.manager = manager;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        String role = manager.getRole();
        return Collections
                .unmodifiableList(AuthorityUtils.createAuthorityList(role));
    }

    public Manager getManager() {
        return this.manager;
    }
}
