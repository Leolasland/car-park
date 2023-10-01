package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.security.ManagerDetails;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Manager> managerByName = managerRepository.findManagerByName(username);
        if (managerByName.isEmpty()) {
            throw new UsernameNotFoundException("username " + username + " is not found");
        }
        Manager manager = managerByName.get();
        return new ManagerDetails(manager.getName(), manager.getPassword(), Collections
                .unmodifiableList(AuthorityUtils.createAuthorityList(manager.getRole())), manager);
    }
}
