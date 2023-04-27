package com.fa.ims.security.service;

import com.fa.ims.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final AuthenticationService authenticationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.fa.ims.entity.User> studentOptional = authenticationService.findByUsername(username);
        return studentOptional.map(this::createSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("The username doesn't exist: " + username));
    }

    private User createSpringUser(com.fa.ims.entity.User user) {
        List<GrantedAuthority> grantedAuthorities = Collections
                .singletonList(new SimpleGrantedAuthority(user.getUserRole().getRoleDesc()));

        return new User(user.getUserName(), user.getUserPassword(), grantedAuthorities);
    }
}
