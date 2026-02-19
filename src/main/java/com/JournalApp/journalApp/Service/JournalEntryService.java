package com.JournalApp.journalApp.Service;

import com.JournalApp.journalApp.Entity.JournalEntry;
import com.JournalApp.journalApp.Entity.Users;
import com.JournalApp.journalApp.Repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserEntryService userEntryService;

    // This method saves the journal entry AND updates the user
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            Users user = userEntryService.getByName(userName).orElse(null);
            if (user != null) {
                journalEntry.setDate(LocalDate.now());
                JournalEntry saved = journalEntryRepo.save(journalEntry);

                user.getJournalEntries().add(saved);

                // ⚠️ CRITICAL: Did you comment this out or delete it?
                userEntryService.saveEntry(user);
            } else {
                // ⚠️ If user is null, the code does nothing but throws no error!
                System.out.println("User not found: " + userName);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred", e);
        }
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId myId) {
        return journalEntryRepo.findById(myId);
    }

    @Transactional
    public boolean deleteById(ObjectId myId, String userName) {
        boolean removed = false;
        try {
            Users user = userEntryService.getByName(userName).orElse(null);
            if (user != null) {
                removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
                if (removed) {
                    userEntryService.saveEntry(user); // Save updated user
                    journalEntryRepo.deleteById(myId);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error deleting entry: " + myId, e);
        }
        return removed;
    }

    public JournalEntry updateById(ObjectId myId, JournalEntry newEntry) {
        JournalEntry old = journalEntryRepo.findById(myId).orElse(null);
        if (old != null) {
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            journalEntryRepo.save(old);
        }
        return old;
    }
}