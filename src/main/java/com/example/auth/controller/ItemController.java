package com.example.auth.controller;

import com.example.auth.model.Item;
import com.example.auth.service.ItemService;
import jakarta.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ItemController {
    
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Dropdown options (sesuai requirement)
    private static final List<String> BUSINESS_UNITS = List.of(
            "PT Mindo",
            "PT Visiniaga",
            "PT Primavisi",
            "PT Bimoli",
            "PT Transmoda"
    );

    private static final List<String> ITEM_TYPES = List.of(
            "Finish Good",
            "Spare Part",
            "Raw Material",
            "General Material",
            "Sawn Timber",
            "Fixed Asset",
            "Waste Material"
    );

    private static void putFormOptions(Model model) {
        model.addAttribute("businessUnits", BUSINESS_UNITS);
        model.addAttribute("itemTypes", ITEM_TYPES);
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    @GetMapping("/items")
    public String items(Model model, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/login";

        List<Item> items = itemService.listAll();
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/items/new")
    public String newItem(Model model, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/login";

        model.addAttribute("item", new Item());
        model.addAttribute("mode", "create");
        putFormOptions(model);
        return "item_form";
    }

    @PostMapping("/items")
    public String createItem(
            @RequestParam String code,
            @RequestParam String description,
            @RequestParam String itemType,
            @RequestParam(required = false, defaultValue = "0") String stock,
            @RequestParam(required = false) String note,
            @RequestParam String businessUnit,
            Model model,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        Item item = new Item();
        item.setCode(code == null ? "" : code.trim());
        item.setDescription(description == null ? "" : description.trim());
        item.setItemType(itemType);
        item.setStock(parseBigDecimalOrZero(stock));
        item.setNote(note);
        item.setBusinessUnit(businessUnit);
        String username = String.valueOf(session.getAttribute("userName"));
        String error = itemService.create(item, username);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("item", item);
            model.addAttribute("mode", "create");
            putFormOptions(model);
            return "item_form";
        }
        return "redirect:/items";
    }

    @GetMapping("/items/{id}/edit")
    public String editItem(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/login";

        Optional<Item> itemOpt = itemService.getById(id);
        if (itemOpt.isEmpty()) return "redirect:/items";

        model.addAttribute("item", itemOpt.get());
        model.addAttribute("mode", "edit");
        putFormOptions(model);
        return "item_form";
    }

    @PostMapping("/items/{id}")
    public String updateItem(
            @PathVariable Long id,
            @RequestParam String description,
            @RequestParam String itemType,
            @RequestParam(required = false, defaultValue = "0") String stock,
            @RequestParam(required = false) String note,
            Model model,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        Optional<Item> itemOpt = itemService.getById(id);
        if (itemOpt.isEmpty()) return "redirect:/items";

        Item item = itemOpt.get();
        item.setDescription(description == null ? "" : description.trim());
        item.setItemType(itemType);
        item.setStock(parseBigDecimalOrZero(stock));
        item.setNote(note);
        String username = String.valueOf(session.getAttribute("userName"));
        String error = itemService.update(id, item, username);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("item", item);
            model.addAttribute("mode", "edit");
            putFormOptions(model);
            return "item_form";
        }

        return "redirect:/items";
    }

    @GetMapping // Ini logger untuk debug saja
    public String list(Model model) {
        List<Item> items = itemService.listAll();
        System.out.println("Items count = " + items.size());
        log.info("Items count = {}", items.size());
        model.addAttribute("items", items);
        return "items";
    }

    private static BigDecimal parseBigDecimalOrZero(String input) {
        try {
            if (input == null || input.trim().isEmpty()) return BigDecimal.ZERO;
            return new BigDecimal(input.trim());
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }
}
