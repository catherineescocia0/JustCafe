package com.justcafe.repository;

import com.justcafe.model.SecretCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SecretCodeRepository extends JpaRepository<SecretCode, Long> {
    Optional<SecretCode> findByCodeAndActive(String code, Boolean active);
}