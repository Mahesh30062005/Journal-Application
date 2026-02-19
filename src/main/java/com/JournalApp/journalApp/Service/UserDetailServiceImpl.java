package com.JournalApp.journalApp.Service;

import com.JournalApp.journalApp.Entity.Users;
import com.JournalApp.journalApp.Repository.UserEntryRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserEntryRepo userEntryRepo;

    public UserDetailServiceImpl(UserEntryRepo userEntryRepo) {
        this.userEntryRepo = userEntryRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userEntryRepo.findByUsername(username);

        if(user != null){
            UserDetails userDetails =  User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
            return userDetails;
        }
        throw new UsernameNotFoundException("User Not Found with username : " + username );


    }
}
