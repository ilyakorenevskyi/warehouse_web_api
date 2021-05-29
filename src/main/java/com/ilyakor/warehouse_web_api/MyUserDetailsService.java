package com.ilyakor.warehouse_web_api;

import com.ilyakor.warehouse_web_api.entities.Role;
import com.ilyakor.warehouse_web_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.ilyakor.warehouse_web_api.entities.User user =  repo.findByLogin(username).orElse(null);
        if(user == null){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            return null;
        }
        List<Role> roles= new ArrayList<>();
            if (user.isAdmin()) {
                roles.add(Role.ADMIN);
            }
            if (user.isSupervisor()) {
                roles.add(Role.SUPERVISOR);
            }
         return new User(user.getLogin(), user.getPassword(), getAuthorities(roles));
    }



    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }

        return authorities;
    }
}
