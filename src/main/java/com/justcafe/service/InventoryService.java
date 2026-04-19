package com.justcafe.service;

import com.justcafe.model.InventoryItem;
import com.justcafe.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findByOrderByItemNameAsc();
    }

    public List<InventoryItem> getItemsByCategory(String category) {
        return inventoryItemRepository.findByCategory(category);
    }

    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }

    public List<InventoryItem> getOutOfStockItems() {
        return inventoryItemRepository.findOutOfStockItems();
    }

    public Map<String, List<InventoryItem>> getAllGroupedByCategory() {
        return inventoryItemRepository.findByOrderByItemNameAsc()
                .stream()
                .collect(Collectors.groupingBy(InventoryItem::getCategory));
    }

    public InventoryItem updateStock(Long id, int newQuantity) {
        Optional<InventoryItem> opt = inventoryItemRepository.findById(id);
        if (opt.isEmpty()) return null;
        InventoryItem item = opt.get();
        item.setStockQuantity(newQuantity);
        return inventoryItemRepository.save(item);
    }

    public InventoryItem adjustStock(Long id, int delta) {
        Optional<InventoryItem> opt = inventoryItemRepository.findById(id);
        if (opt.isEmpty()) return null;
        InventoryItem item = opt.get();
        int newQty = Math.max(0, item.getStockQuantity() + delta);
        item.setStockQuantity(newQty);
        return inventoryItemRepository.save(item);
    }

    public InventoryItem save(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    public Optional<InventoryItem> findById(Long id) {
        return inventoryItemRepository.findById(id);
    }
}