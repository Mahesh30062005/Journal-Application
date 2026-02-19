package com.JournalApp.journalApp.Repository;

import com.JournalApp.journalApp.Entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepo extends MongoRepository<Users, ObjectId> {
    Users findByUsername(String username);
}