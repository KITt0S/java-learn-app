package com.k1ts.security.refresh;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class JwtRefreshToken {
    private String username;
    
    private String ipAddress;
    
    private long expiredAt;
    
    @Id
    @Column(length = 200)
    private String token = UUID.randomUUID().toString();
}
