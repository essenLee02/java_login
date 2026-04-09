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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private String emptyToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    @GetMapping("/provinces")
    public String provinces(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "search", defaultValue = "") String search,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            showLogger(
                "PROVINCE",
                "Access denied to list page because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);
        String logMessage = "User %s opened Province list page. page=%d, size=%s"
            .formatted(userName, page, size);
        showLogger("PROVINCE", logMessage, "info");

        int pageSize = (size == null || size <= 0) ? defaultPageSize : size;
        if (page < 0) {
            page = 0;
        }

        search = emptyToEmpty(search);

        long totalElements = service.countAll(search);
        int totalPages = (int) Math.ceil(totalElements / (double) pageSize);

        if (totalPages <= 0) {
            totalPages = 1;
        }

        if (page > totalPages - 1) {
            page = totalPages - 1;
        }

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
        if (!isLoggedIn(session)) {
            showLogger(
                "PROVINCE",
                "Access denied to create page because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);
        String logMessage = "User %s opened NEW Province form."
            .formatted(userName);
        showLogger("PROVINCE", logMessage, "info");

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
        if (!isLoggedIn(session)) {
            showLogger(
                "PROVINCE",
                "Insert denied because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            province.setIdCountry(emptyToNull(province.getIdCountry()));
            province.setCode(emptyToNull(province.getCode() == null ? null : province.getCode().toUpperCase()));
            province.setName(emptyToNull(province.getName() == null ? null : province.getName().toUpperCase()));

            String validation = service.validate(province, null);
            if (validation != null) {
                String logMessage = "INSERT FAILED by user %s. validation=%s"
                    .formatted(userName, validation);
                showLogger("PROVINCE", logMessage, "warn");

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

            if (province.getStatus() == null) {
                province.setStatus(1);
            }

            service.save(province);

            String logMessage = "INSERT SUCCESS by user %s. generatedId=%s, idCountry=%s, code=%s, name=%s"
                .formatted(
                    userName,
                    province.getIdProvince(),
                    province.getIdCountry(),
                    province.getCode(),
                    province.getName()
                );
            showLogger("PROVINCE", logMessage, "info");

            ra.addFlashAttribute("success", "Province saved successfully");
        } catch (Exception e) {
            String logMessage = "INSERT FAILED by user %s. code=%s, name=%s, error=%s"
                .formatted(
                    userName,
                    province.getCode(),
                    province.getName(),
                    e.getMessage()
                );
            showLogger("PROVINCE", logMessage, "error");

            ra.addFlashAttribute("error", "Failed to save Province: " + e.getMessage());
            return "redirect:/provinces/new";
        }

        return "redirect:/provinces";
    }

    @GetMapping("/provinces/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            String logMessage = "Edit denied because user is not logged in. id=%d"
                .formatted(id);
            showLogger("PROVINCE", logMessage, "warn");
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            Province province = service.findById(id);
            model.addAttribute("province", province);
            model.addAttribute("mode", "edit");

            String logMessage = "User %s opened EDIT Province form for ID %d"
                .formatted(userName, id);
            showLogger("PROVINCE", logMessage, "info");

            return appLocation + "/Province/province-form";
        } catch (Exception e) {
            String logMessage = "FAILED opening edit page by user %s. id=%d, error=%s"
                .formatted(userName, id, e.getMessage());
            showLogger("PROVINCE", logMessage, "error");

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
        if (!isLoggedIn(session)) {
            String logMessage = "Update denied because user is not logged in. id=%d"
                .formatted(id);
            showLogger("PROVINCE", logMessage, "warn");
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            Province existing = service.findById(id);

            province.setId(id);
            province.setIdProvince(existing.getIdProvince());
            province.setIdCountry(emptyToNull(province.getIdCountry()));
            province.setCode(emptyToNull(province.getCode() == null ? null : province.getCode().toUpperCase()));
            province.setName(emptyToNull(province.getName() == null ? null : province.getName().toUpperCase()));
            province.setCreatedDate(existing.getCreatedDate());
            province.setCreatedBy(existing.getCreatedBy());
            province.setUpdatedDate(LocalDate.now().toString());
            province.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            province.setDeletedDate(existing.getDeletedDate());
            province.setDeletedBy(existing.getDeletedBy());

            String validation = service.validate(province, id);
            if (validation != null) {
                String logMessage = "UPDATE FAILED by user %s. id=%d, validation=%s"
                    .formatted(userName, id, validation);
                showLogger("PROVINCE", logMessage, "warn");

                ra.addFlashAttribute("error", validation);
                return "redirect:/provinces/" + id + "/edit";
            }

            if (province.getStatus() == null) {
                province.setStatus(existing.getStatus() == null ? 1 : existing.getStatus());
            }

            service.update(id, province);

            String logMessage = "UPDATE SUCCESS by user %s. id=%d, generatedId=%s, idCountry=%s, code=%s, name=%s"
                .formatted(
                    userName,
                    id,
                    province.getIdProvince(),
                    province.getIdCountry(),
                    province.getCode(),
                    province.getName()
                );
            showLogger("PROVINCE", logMessage, "info");

            ra.addFlashAttribute("success", "Province updated successfully");
        } catch (Exception e) {
            String logMessage = "FAILED to update Province by user %s. id=%d, error=%s"
                .formatted(userName, id, e.getMessage());
            showLogger("PROVINCE", logMessage, "error");

            ra.addFlashAttribute("error", "Failed to update Province: " + e.getMessage());
            return "redirect:/provinces/" + id + "/edit";
        }

        return "redirect:/provinces";
    }
}