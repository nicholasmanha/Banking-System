package bss;

import java.time.LocalDateTime;

public class User {

    protected int id;
    private LocalDateTime created;
    static int count = 0;

    // Set id and date of creation
    public User() {
        this.id = count++;
        this.created = LocalDateTime.now(); 
    }

    public int getId() {
        return id;
    }

    public void setID(int in_id) {
        this.id = in_id;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
