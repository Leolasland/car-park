package ru.project.carpark.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.project.carpark.entity.Manager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ManagerDetails extends User {

    private final Manager manager;

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

    public ManagerDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities,
                          Manager manager) {
        super(username, password, authorities);
        this.manager = manager;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return ROLE_USER;
    }

    public Manager getManager() {
        return this.manager;
    }
}
