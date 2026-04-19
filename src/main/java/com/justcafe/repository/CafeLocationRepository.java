package com.justcafe.repository;

import com.justcafe.model.CafeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CafeLocationRepository extends JpaRepository<CafeLocation, Long> {
    List<CafeLocation> findByCity(String city);
}
