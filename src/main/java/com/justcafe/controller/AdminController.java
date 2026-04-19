package com.justcafe.controller;

import com.justcafe.model.InventoryItem;
import com.justcafe.model.Order;
import com.justcafe.service.InventoryService;
import com.justcafe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    // ── Admin page (HTML) ──────────────────────────────────────────────────
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        List<Order> allOrders = orderService.getAllOrders();

        model.addAttribute("pendingOrders",   allOrders.stream().filter(o -> "PENDING".equals(o.getStatus())).collect(Collectors.toList()));
        model.addAttribute("confirmedOrders", allOrders.stream().filter(o -> "CONFIRMED".equals(o.getStatus())).collect(Collectors.toList()));
        model.addAttribute("readyOrders",     allOrders.stream().filter(o -> "READY".equals(o.getStatus())).collect(Collectors.toList()));

        model.addAttribute("totalOrders",   allOrders.size());
        model.addAttribute("pendingCount",  allOrders.stream().filter(o -> "PENDING".equals(o.getStatus())).count());
        model.addAttribute("confirmedCount",allOrders.stream().filter(o -> "CONFIRMED".equals(o.getStatus())).count());
        model.addAttribute("readyCount",    allOrders.stream().filter(o -> "READY".equals(o.getStatus())).count());

        model.addAttribute("inventory",        inventoryService.getAllItems());
        model.addAttribute("lowStockItems",    inventoryService.getLowStockItems());
        model.addAttribute("outOfStockItems",  inventoryService.getOutOfStockItems());
        model.addAttribute("lowStockCount",    inventoryService.getLowStockItems().size());
        model.addAttribute("outOfStockCount",  inventoryService.getOutOfStockItems().size());
        model.addAttribute("inventoryByCategory", inventoryService.getAllGroupedByCategory());

        return "admin";
    }

    // ── REST: Orders ──────────────────────────────────────────────────────
    @GetMapping("/api/admin/orders")
    @ResponseBody
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/api/admin/orders/status/{status}")
    @ResponseBody
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status.toUpperCase()));
    }

    @PatchMapping("/api/admin/orders/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String newStatus = body.get("status");
        if (newStatus == null || !List.of("PENDING", "CONFIRMED", "READY").contains(newStatus.toUpperCase())) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Invalid status. Use PENDING, CONFIRMED, or READY.");
            return ResponseEntity.badRequest().body(err);
        }

        Order updated = orderService.updateStatus(id, newStatus.toUpperCase());
        if (updated == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Order not found");
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("orderId", updated.getId());
        res.put("status", updated.getStatus());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/api/admin/orders/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Order #" + id + " deleted");
        return ResponseEntity.ok(res);
    }

    // ── REST: Inventory ───────────────────────────────────────────────────
    @GetMapping("/api/admin/inventory")
    @ResponseBody
    public ResponseEntity<List<InventoryItem>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/api/admin/inventory/low-stock")
    @ResponseBody
    public ResponseEntity<List<InventoryItem>> getLowStock() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @PatchMapping("/api/admin/inventory/{id}/stock")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateStock(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        Map<String, Object> res = new HashMap<>();
        Object qtyObj = body.get("quantity");
        if (qtyObj == null) {
            res.put("success", false); res.put("message", "quantity required");
            return ResponseEntity.badRequest().body(res);
        }
        int qty = Integer.parseInt(qtyObj.toString());
        InventoryItem updated = inventoryService.updateStock(id, qty);
        if (updated == null) return ResponseEntity.notFound().build();
        res.put("success", true);
        res.put("itemId", updated.getId());
        res.put("itemName", updated.getItemName());
        res.put("newQuantity", updated.getStockQuantity());
        res.put("status", updated.getStockStatus());
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/api/admin/inventory/{id}/adjust")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> adjustStock(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        Map<String, Object> res = new HashMap<>();
        Object deltaObj = body.get("delta");
        if (deltaObj == null) {
            res.put("success", false); res.put("message", "delta required");
            return ResponseEntity.badRequest().body(res);
        }
        int delta = Integer.parseInt(deltaObj.toString());
        InventoryItem updated = inventoryService.adjustStock(id, delta);
        if (updated == null) return ResponseEntity.notFound().build();
        res.put("success", true);
        res.put("itemId", updated.getId());
        res.put("itemName", updated.getItemName());
        res.put("newQuantity", updated.getStockQuantity());
        res.put("status", updated.getStockStatus());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/api/admin/inventory/{id}")
    @ResponseBody
    public ResponseEntity<InventoryItem> updateInventoryItem(
            @PathVariable Long id,
            @RequestBody InventoryItem incoming) {

        Optional<InventoryItem> opt = inventoryService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        InventoryItem existing = opt.get();
        existing.setStockQuantity(incoming.getStockQuantity());
        existing.setLowStockThreshold(incoming.getLowStockThreshold());
        existing.setNotes(incoming.getNotes());
        return ResponseEntity.ok(inventoryService.save(existing));
    }

    // ── REST: Dashboard summary ───────────────────────────────────────────
    @GetMapping("/api/admin/summary")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSummary() {
        List<Order> all = orderService.getAllOrders();
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders",    all.size());
        summary.put("pendingCount",   all.stream().filter(o -> "PENDING".equals(o.getStatus())).count());
        summary.put("confirmedCount", all.stream().filter(o -> "CONFIRMED".equals(o.getStatus())).count());
        summary.put("readyCount",     all.stream().filter(o -> "READY".equals(o.getStatus())).count());
        summary.put("lowStockCount",  inventoryService.getLowStockItems().size());
        summary.put("outOfStockCount",inventoryService.getOutOfStockItems().size());
        return ResponseEntity.ok(summary);
    }
}