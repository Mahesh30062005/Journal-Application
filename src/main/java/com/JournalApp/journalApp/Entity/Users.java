package com.JournalApp.journalApp.Entity;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Users")
@Data
public class Users {
    @Id

    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;

    // This links the Journal Entries to the User
    @DBRef
    @ToString.Exclude // <--- ADD THIS LINE
    private List<JournalEntry> journalEntries = new ArrayList<>();
}