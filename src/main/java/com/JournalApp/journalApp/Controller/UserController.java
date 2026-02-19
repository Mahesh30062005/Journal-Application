package com.JournalApp.journalApp.Controller;

import com.JournalApp.journalApp.Entity.Users;
import com.JournalApp.journalApp.Service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/Users")  // it wraps the journalEntryController in the path (end point)of /Journal
public class UserController {

    @Autowired
    private UserEntryService userService;

    @GetMapping
    public List<Users> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<Users> createEntry(@RequestBody Users userEntry) {
        try{
            userService.saveEntry(userEntry);
            return new ResponseEntity<Users>(userEntry, HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<Users>(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PutMapping
    public  ResponseEntity<Users> updateEntry(@RequestBody Users userEntry){
        Optional<Users> userInDb = userService.getByName(userEntry.getUsername());
        if (userInDb.isPresent()) {

            // 3. Open the Box to get the User
            Users user = userInDb.get();

            // 4. NOW you can use Lombok's setters
            user.setPassword(userEntry.getPassword());

            // 5. CRITICAL: Save the updated user back to DB!
            userService.saveEntry(user);

            return new ResponseEntity<Users>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
