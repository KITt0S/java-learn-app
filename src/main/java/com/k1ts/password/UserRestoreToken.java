package com.k1ts.password;

import com.k1ts.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class UserRestoreToken {
    @Id
    private String token = UUID.randomUUID().toString();

    private long expiredAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
