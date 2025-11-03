package com.k1ts.verification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {

    @Query(nativeQuery = true, value = "SELECT token FROM verification_token WHERE user_id = :username")
    Set<String> getTokensByUsername(@Param(value = "username") String username);
}
