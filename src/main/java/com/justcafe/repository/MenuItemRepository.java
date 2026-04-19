package com.justcafe.repository;

import com.justcafe.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByCategoryAndAvailable(String category, Boolean available);
    List<MenuItem> findByAvailable(Boolean available);
}
