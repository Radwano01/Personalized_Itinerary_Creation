package com.example.Personal_Travel_Concierge.security;


import com.example.Personal_Travel_Concierge.entity.user.RoleEntity;
import com.example.Personal_Travel_Concierge.entity.user.UserEntity;
import com.example.Personal_Travel_Concierge.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        return new User(userEntity.getUsername(),userEntity.getPassword(), mapAuthority(userEntity.getRole()));
    }

    private Collection<GrantedAuthority> mapAuthority(RoleEntity role){
        return Collections.singleton(new SimpleGrantedAuthority(role.getRole()));
    }
}
