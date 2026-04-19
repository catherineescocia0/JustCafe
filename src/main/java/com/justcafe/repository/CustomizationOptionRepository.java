package com.justcafe.repository;

import com.justcafe.model.CustomizationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomizationOptionRepository extends JpaRepository<CustomizationOption, Long> {
    List<CustomizationOption> findByApplicableTo(String applicableTo);
    List<CustomizationOption> findByApplicableToIn(List<String> applicableTo);
}
