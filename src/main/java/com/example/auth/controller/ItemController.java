package com.example.auth.controller;

import com.example.auth.model.Item;
import com.example.auth.service.ItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.example.auth.util.GenerateController;

@Controller
public class ItemController extends GenerateController {

    private final ItemService service;

    @Value("${app.pagination.page-size:10}")
    private int defaultPageSize;

    @Value("${app.location:Page_Components}")
    private String appLocation;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @ModelAttribute("businessUnits")
    public List<String> businessUnits() {
        return Arrays.asList(
                "PT Mindo",
                "PT Visiniaga",
                "PT Primavisi",
                "PT Bimoli",
                "PT Transmoda"
        );
    }

    @ModelAttribute("itemTypes")
    public List<String> itemTypes() {
        return Arrays.asList(
                "Raw Material",
                "General Material",
                "Spare Part",
                "Finish Goods",
                "Fixed Assets",
                "Logs"
        );
    }

    @GetMapping("/items")
    public String items(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", required = false) Integer size,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        int pageSize = (size == null || size <= 0) ? defaultPageSize : size;
        if (page < 0) {
            page = 0;
        }

        long totalElements = service.countAll();
        int totalPages = (int) Math.ceil(totalElements / (double) pageSize);

        if (totalPages > 0 && page > totalPages - 1) {
            page = totalPages - 1;
        }

        List<Item> items = service.getPage(page, pageSize);

        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", totalPages > 0 && page < totalPages - 1);

        if (totalPages > 1) {
            model.addAttribute("pageNumbers", IntStream.range(0, totalPages).boxed().toList());
        }

        return appLocation + "/ItemMaster/items";
    }

    @GetMapping("/items/new")
    public String newItemForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute("item", new com.example.auth.model.Item());
        model.addAttribute("mode", "new");

        return appLocation + "/ItemMaster/item-form";
    }

    @GetMapping("/items/{id}/edit")
    public String editItem(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra,
            HttpSession session
    ) {
        try {
            if (!isLoggedIn(session)) {
                return "redirect:/login";
            }

            Item item = service.findById(id);
            model.addAttribute("item", item);
            model.addAttribute("mode", "edit");

            return appLocation + "/ItemMaster/item-form";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Item not found");
            return "redirect:/items";
        }
    }

    @PostMapping("/items")
    public String saveItem(
            @ModelAttribute Item item,
            RedirectAttributes ra,
            HttpSession session
    ) {
        try {
            Object userIdObj = session.getAttribute("userId");
            if (userIdObj == null) {
                return "redirect:/login";
            }

            item.setCreatedBy(userIdObj.toString());
            service.save(item);
            ra.addFlashAttribute("success", "Item saved successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to save item: " + e.getMessage());
        }

        return "redirect:/items";
    }

    @PostMapping("/items/{id}")
    public String updateItem(
            @PathVariable Long id,
            @ModelAttribute Item item,
            RedirectAttributes ra,
            HttpSession session
    ) {
        try {
            if (!isLoggedIn(session)) {
                return "redirect:/login";
            }

            item.setUpdatedBy(session.getAttribute("userId").toString());
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