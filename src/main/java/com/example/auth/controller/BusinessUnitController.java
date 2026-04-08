package com.example.auth.controller;

import com.example.auth.model.BusinessUnit;
import com.example.auth.service.BusinessUnitService;
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

import java.time.LocalDate;
import java.util.stream.IntStream;
import com.example.auth.util.GenerateController;

@Controller
public class BusinessUnitController extends GenerateController {

    private final BusinessUnitService service;

    @Value("${app.pagination.page-size:10}")
    private int defaultPageSize;

    @Value("${app.location:Page_Components}")
    private String appLocation;

    public BusinessUnitController(BusinessUnitService service) {
        this.service = service;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    private String emptyToNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    private String emptyToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    @GetMapping("/business-units")
    public String businessUnits(
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

        model.addAttribute("businessUnits", service.getPage(page, pageSize));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", totalPages > 0 && page < totalPages - 1);

        if (totalPages > 1) {
            model.addAttribute("pageNumbers", IntStream.range(0, totalPages).boxed().toList());
        }

        return appLocation + "/BusinessUnit/business-units";
    }

    @GetMapping("/business-units/new")
    public String newBusinessUnitForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setStatus(1);

        model.addAttribute("businessUnit", businessUnit);
        model.addAttribute("mode", "new");

        return appLocation + "/BusinessUnit/business-unit-form";
    }

    @PostMapping("/business-units")
    public String saveBusinessUnit(
            @ModelAttribute BusinessUnit businessUnit,
            RedirectAttributes ra,
            HttpSession session
    ) {
        try {
            if (!isLoggedIn(session)) {
                return "redirect:/login";
            }

            long totalByCompany = service.countAll();

            // 🔥 pakai function dari parent class
            String generatedId = generateRandomString(3, businessUnit.getName(), totalByCompany);

            businessUnit.setIdBussinessUnit(generatedId);
            businessUnit.setIdCompany(emptyToNull(businessUnit.getIdCompany()));
            businessUnit.setCode(businessUnit.getCode());
            businessUnit.setName(businessUnit.getName());
            businessUnit.setAddress(emptyToNull(businessUnit.getAddress()));
            businessUnit.setIdCountry(emptyToNull(businessUnit.getIdCountry()));
            businessUnit.setIdProvince(emptyToNull(businessUnit.getIdProvince()));
            businessUnit.setIdCity(emptyToNull(businessUnit.getIdCity()));
            businessUnit.setTaxNumber(emptyToNull(businessUnit.getTaxNumber()));
            businessUnit.setEmail(emptyToNull(businessUnit.getEmail()));
            businessUnit.setPhoneNumber(emptyToNull(businessUnit.getPhoneNumber()));
            businessUnit.setCreatedDate(LocalDate.now().toString());
            businessUnit.setCreatedBy(String.valueOf(session.getAttribute("userId")));
            businessUnit.setUpdatedDate(LocalDate.now().toString());
            businessUnit.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            businessUnit.setDeletedDate(null);
            businessUnit.setDeletedBy(null);
            if (businessUnit.getStatus() == null) {
                businessUnit.setStatus(1); // Aktif secara default
            }

            service.save(businessUnit);
            ra.addFlashAttribute("success", "Business Unit saved successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to save Business Unit: " + e.getMessage());
            return "redirect:/business-units/new";
        }

        return "redirect:/business-units";
    }

    @GetMapping("/business-units/{id}/edit")
    public String editBusinessUnit(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra,
            HttpSession session
    ) {
        try {
            if (!isLoggedIn(session)) {
                return "redirect:/login";
            }

            BusinessUnit businessUnit = service.findById(id);
            model.addAttribute("businessUnit", businessUnit);
            model.addAttribute("mode", "edit");

            return appLocation + "/BusinessUnit/business-unit-form";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Business Unit not found");
            return "redirect:/business-units";
        }
    }

    @PostMapping("/business-units/{id}")
    public String updateBusinessUnit(
            @PathVariable Long id,
            @ModelAttribute BusinessUnit businessUnit,
            RedirectAttributes ra,
            HttpSession session
    ) {
        try {
            if (!isLoggedIn(session)) {
                return "redirect:/login";
            }

            BusinessUnit existingBusinessUnit = service.findById(id);

            businessUnit.setIdBussinessUnit(emptyToEmpty(existingBusinessUnit.getIdBussinessUnit()));
            businessUnit.setIdCompany(emptyToNull(existingBusinessUnit.getIdCompany()));
            businessUnit.setCode(emptyToNull(businessUnit.getCode()));
            businessUnit.setName(emptyToNull(businessUnit.getName()));
            businessUnit.setAddress(emptyToNull(businessUnit.getAddress()));
            businessUnit.setIdCountry(emptyToNull(businessUnit.getIdCountry()));
            businessUnit.setIdProvince(emptyToNull(businessUnit.getIdProvince()));
            businessUnit.setIdCity(emptyToNull(businessUnit.getIdCity()));
            businessUnit.setTaxNumber(emptyToNull(businessUnit.getTaxNumber()));
            businessUnit.setEmail(emptyToNull(businessUnit.getEmail()));
            businessUnit.setPhoneNumber(emptyToNull(businessUnit.getPhoneNumber()));
            businessUnit.setCreatedDate(existingBusinessUnit.getCreatedDate());
            businessUnit.setCreatedBy(existingBusinessUnit.getCreatedBy());
            businessUnit.setUpdatedDate(LocalDate.now().toString());
            businessUnit.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            businessUnit.setDeletedDate(existingBusinessUnit.getDeletedDate());
            businessUnit.setDeletedBy(existingBusinessUnit.getDeletedBy());

            if (businessUnit.getStatus() == null) {
                businessUnit.setStatus(existingBusinessUnit.getStatus() == null ? 1 : existingBusinessUnit.getStatus());
            }

            service.update(id, businessUnit);
            ra.addFlashAttribute("success", "Business Unit updated successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to update Business Unit: " + e.getMessage());
            return "redirect:/business-units/" + id + "/edit";
        }

        return "redirect:/business-units";
    }
}