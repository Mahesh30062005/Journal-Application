package com.JournalApp.journalApp.Service;

import com.JournalApp.journalApp.Entity.Users;
import com.JournalApp.journalApp.Repository.UserEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntryService {

    @Autowired
    private UserEntryRepo userEntryRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // RENAMED back to saveEntry to fix your error
    public void saveEntry(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("Users"));
        userEntryRepo.save(user);
    }

    public List<Users> getAll() {
        return userEntryRepo.findAll();
    }

    public Optional<Users> getByName(String username) {
        return Optional.ofNullable(userEntryRepo.findByUsername(username));
    }

    public void deleteById(ObjectId myId) {
        userEntryRepo.deleteById(myId);
    }

    public Users update(String username, Users newEntry) {
        Users old = userEntryRepo.findByUsername(username);
        if (old != null && newEntry != null) {
            old.setUsername(newEntry.getUsername() != null && !newEntry.getUsername().equals("") ? newEntry.getUsername() : old.getUsername());
            old.setPassword(newEntry.getPassword() != null && !newEntry.getPassword().equals("") ? newEntry.getPassword() : old.getPassword());
            userEntryRepo.save(old);
        }
        return old;
    }
}