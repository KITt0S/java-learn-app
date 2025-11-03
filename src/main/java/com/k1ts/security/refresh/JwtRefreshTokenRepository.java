package com.k1ts.security.refresh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("NullableProblems")
@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, String>, JpaSpecificationExecutor<JwtRefreshToken> {
    @Override
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE FROM jwt_refresh_token WHERE token = :token")
    void deleteById(String token);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE FROM jwt_refresh_token")
    @Override
    void deleteAll();
}
