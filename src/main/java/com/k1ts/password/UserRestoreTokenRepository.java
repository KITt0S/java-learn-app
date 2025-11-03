package com.k1ts.password;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRestoreTokenRepository extends CrudRepository<UserRestoreToken, String> {

    @Query(nativeQuery = true, value = "SELECT token FROM user_restore_token WHERE user_id = :username")
    Set<String> getTokensByUsername(@Param(value = "username") String username);
}
