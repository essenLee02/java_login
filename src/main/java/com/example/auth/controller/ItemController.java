package com.example.auth.controller;

import com.example.auth.model.Item;
import com.example.auth.service.ItemService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class ItemController {

    private final ItemService service;

    /**
     * Default page size for pagination (can be configured in application.properties).
     */
    @Value("${app.pagination.page-size:10}")
    private int defaultPageSize;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/items")
    public String items(Model model
        , @RequestParam(name = "page", defaultValue = "0") int page
        , @RequestParam(name = "size", required = false) Integer size
    ) {

        int pageSize = (size == null || size <= 0) ? defaultPageSize : size;
        if (page < 0) page = 0;

        long totalElements = service.countAll();
        int totalPages = (int) Math.ceil(totalElements / (double) pageSize);
        if (totalPages > 0 && page > totalPages - 1) page = totalPages - 1;

        List<Item> items = service.getPage(page, pageSize);

        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", totalPages > 0 && page < totalPages - 1);

        // Build list of page numbers for the UI (0-based index internally).
        if (totalPages > 1) {
            model.addAttribute("pageNumbers", IntStream.range(0, totalPages).boxed().toList());
        }

        return "items";
    }

    @GetMapping("/items/new")
    public String newItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "item-form";
    }

    @PostMapping("/items")
    public String saveItem(@ModelAttribute Item item
        , RedirectAttributes ra
        , HttpSession session
    ) {
        try {
            String userId = session.getAttribute("userId").toString();
            item.setCreatedBy(userId); // bisa diganti session user
            service.save(item);
            ra.addFlashAttribute("success", "Item saved successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to save item: " + e.getMessage());
        }
        return "redirect:/items";
    }

    @GetMapping("/items/{id}/edit")
    public String editItem(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Item item = service.findById(id);
            model.addAttribute("item", item);
            return "item-form";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Item not found");
            return "redirect:/items";
        }
    }

    @PostMapping("/items/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute Item item, RedirectAttributes ra) {
        try {
            item.setUpdatedBy("system");
            service.update(id, item);
            ra.addFlashAttribute("success", "Item updated successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to update item: " + e.getMessage());
        }
        return "redirect:/items";
    }

    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Item deleted successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to delete item: " + e.getMessage());
        }
        return "redirect:/items";
    }

    @PostMapping("/items/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
