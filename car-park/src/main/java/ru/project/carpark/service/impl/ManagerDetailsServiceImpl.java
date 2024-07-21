package ru.project.carpark.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.security.ManagerDetails;
import ru.project.carpark.service.ManagerDetailsService;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerDetailsServiceImpl implements ManagerDetailsService {

    private final ManagerRepository managerRepository;

    private AuthenticationProvider authenticationManager;

    @Autowired
    public void setAuthenticationManager(@Lazy AuthenticationProvider authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

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

    @Override
    public Optional<Manager> getManagerByUserName(String username) throws UsernameNotFoundException {
        Optional<Manager> managerByName = managerRepository.findManagerByName(username);
        if (managerByName.isEmpty()) {
            throw new UsernameNotFoundException("username " + username + " is not found");
        }
        Manager manager = managerByName.get();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(manager.getName(), manager.getPassword());
        Authentication authentication = authenticationManager.authenticate(authRequest);
        return managerRepository.findManagerByName(username);
    }

    @Override
    public Optional<Manager> getManagerByUserName(String username, String password) throws UsernameNotFoundException {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authRequest);
        if (authentication.isAuthenticated()) {
            Optional<Manager> managerByName = managerRepository.findManagerByName(username);
            if (managerByName.isEmpty()) {
                throw new UsernameNotFoundException("username " + username + " is not found");
            }
            Manager manager = managerByName.get();
            return managerRepository.findManagerByName(username);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Manager> getManagerByChatId(Long chatId) {
        return managerRepository.findManagerByChatId(chatId);
    }

    @Override
    @Transactional
    public void addChatIdToManager(Manager manager, Long chatId) {
        manager.setChatId(chatId);
        managerRepository.save(manager);
    }


}
