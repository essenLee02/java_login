package com.example.auth.controller;

import com.example.auth.model.Country;
import com.example.auth.model.Province;
import com.example.auth.service.CountryService;
import com.example.auth.service.ProvinceService;
import com.example.auth.util.GenerateController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class ProvinceController extends GenerateController {

    private final ProvinceService service;
    private final CountryService countryService;

    @Value("${app.pagination.page-size:10}")
    private int defaultPageSize;

    @Value("${app.location:Page_Components}")
    private String appLocation;

    public ProvinceController(ProvinceService service, CountryService countryService) {
        this.service = service;
        this.countryService = countryService;
    }

    @ModelAttribute("countries")
    public List<Country> countries() {
        return countryService.findAll();
    }

    private String emptyToNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    @GetMapping("/provinces")
    public String provinces(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "search", defaultValue = "") String search,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        int pageSize = (size == null || size <= 0) ? defaultPageSize : size;
        long totalElements = service.countAll(search);
        int totalPages = (int) Math.ceil(totalElements / (double) pageSize);
        if (totalPages <= 0) totalPages = 1;
        if (page < 0) page = 0;
        if (page > totalPages - 1) page = totalPages - 1;

        model.addAttribute("provinces", service.getPage(search, page, pageSize));
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < totalPages - 1);
        model.addAttribute("pageNumbers", IntStream.range(0, totalPages).boxed().toList());

        return appLocation + "/Province/provinces";
    }

    @GetMapping("/provinces/new")
    public String newForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/login";

        Province province = new Province();
        province.setStatus(1);

        model.addAttribute("province", province);
        model.addAttribute("mode", "new");
        return appLocation + "/Province/province-form";
    }

    @PostMapping("/provinces")
    public String save(
            @ModelAttribute Province province,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        province.setIdCountry(emptyToNull(province.getIdCountry()));
        province.setCode(emptyToNull(province.getCode()));
        province.setName(emptyToNull(province.getName()));

        String validation = service.validate(province, null);
        if (validation != null) {
            ra.addFlashAttribute("error", validation);
            return "redirect:/provinces/new";
        }

        long totalData = service.countAll("");
        province.setIdProvince(generateRandomString(3, province.getName(), totalData));
        province.setCreatedDate(LocalDate.now().toString());
        province.setCreatedBy(String.valueOf(session.getAttribute("userId")));
        province.setUpdatedDate(LocalDate.now().toString());
        province.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
        province.setDeletedDate(null);
        province.setDeletedBy(null);

        if (province.getStatus() == null) province.setStatus(1);

        service.save(province);
        ra.addFlashAttribute("success", "Province saved successfully");
        return "redirect:/provinces";
    }

    @GetMapping("/provinces/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/login";

        try {
            model.addAttribute("province", service.findById(id));
            model.addAttribute("mode", "edit");
            return appLocation + "/Province/province-form";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Province not found");
            return "redirect:/provinces";
        }
    }

    @PostMapping("/provinces/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Province province,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        Province existing = service.findById(id);

        province.setId(id);
        province.setIdProvince(existing.getIdProvince());
        province.setIdCountry(emptyToNull(province.getIdCountry()));
        province.setCode(emptyToNull(province.getCode()));
        province.setName(emptyToNull(province.getName()));
        province.setCreatedDate(existing.getCreatedDate());
        province.setCreatedBy(existing.getCreatedBy());
        province.setUpdatedDate(LocalDate.now().toString());
        province.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
        province.setDeletedDate(existing.getDeletedDate());
        province.setDeletedBy(existing.getDeletedBy());

        String validation = service.validate(province, id);
        if (validation != null) {
            ra.addFlashAttribute("error", validation);
            return "redirect:/provinces/" + id + "/edit";
        }

        if (province.getStatus() == null) province.setStatus(existing.getStatus());

        service.update(id, province);
        ra.addFlashAttribute("success", "Province updated successfully");
        return "redirect:/provinces";
    }
}