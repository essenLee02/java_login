package com.example.auth.controller;

import com.example.auth.model.Country;
import com.example.auth.service.CountryService;
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
import java.util.stream.IntStream;

@Controller
public class CountryController extends GenerateController {

    private final CountryService service;

    @Value("${app.pagination.page-size:10}")
    private int defaultPageSize;

    @Value("${app.location:Page_Components}")
    private String appLocation;

    public CountryController(CountryService service) {
        this.service = service;
    }

    private String emptyToNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    private String emptyToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    @GetMapping("/countries")
    public String countries(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "search", defaultValue = "") String search,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            showLogger(
                "COUNTRY",
                "Access denied to list page because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);
        String logMessage = "User %s opened Country list page. page=%d, size=%s"
            .formatted(userName, page, size);
        showLogger("COUNTRY", logMessage, "info");

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

        model.addAttribute("countries", service.getPage(search, page, pageSize));
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < totalPages - 1);
        model.addAttribute("pageNumbers", IntStream.range(0, totalPages).boxed().toList());

        return appLocation + "/Country/countries";
    }

    @GetMapping("/countries/new")
    public String newForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            showLogger(
                "COUNTRY",
                "Access denied to create page because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);
        String logMessage = "User %s opened NEW Country form."
            .formatted(userName);
        showLogger("COUNTRY", logMessage, "info");

        Country country = new Country();
        country.setStatus(1);

        model.addAttribute("country", country);
        model.addAttribute("mode", "new");

        return appLocation + "/Country/country-form";
    }

    @PostMapping("/countries")
    public String save(
            @ModelAttribute Country country,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            showLogger(
                "COUNTRY",
                "Insert denied because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            country.setCode(emptyToNull(country.getCode() == null ? null : country.getCode().toUpperCase()));
            country.setName(emptyToNull(country.getName() == null ? null : country.getName().toUpperCase()));

            String validation = service.validate(country, null);
            if (validation != null) {
                String logMessage = "INSERT FAILED by user %s. validation=%s"
                    .formatted(userName, validation);
                showLogger("COUNTRY", logMessage, "warn");

                ra.addFlashAttribute("error", validation);
                return "redirect:/countries/new";
            }

            long totalData = service.countAll("");
            country.setIdCountry(generateRandomString(3, country.getName(), totalData));
            country.setCreatedDate(LocalDate.now().toString());
            country.setCreatedBy(String.valueOf(session.getAttribute("userId")));
            country.setUpdatedDate(LocalDate.now().toString());
            country.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            country.setDeletedDate(null);
            country.setDeletedBy(null);

            if (country.getStatus() == null) {
                country.setStatus(1);
            }

            service.save(country);

            String logMessage = "INSERT SUCCESS by user %s. generatedId=%s, code=%s, name=%s"
                .formatted(
                    userName,
                    country.getIdCountry(),
                    country.getCode(),
                    country.getName()
                );
            showLogger("COUNTRY", logMessage, "info");

            ra.addFlashAttribute("success", "Country saved successfully");
        } catch (Exception e) {
            String logMessage = "INSERT FAILED by user %s. code=%s, name=%s, error=%s"
                .formatted(
                    userName,
                    country.getCode(),
                    country.getName(),
                    e.getMessage()
                );
            showLogger("COUNTRY", logMessage, "error");

            ra.addFlashAttribute("error", "Failed to save Country: " + e.getMessage());
            return "redirect:/countries/new";
        }

        return "redirect:/countries";
    }

    @GetMapping("/countries/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            String logMessage = "Edit denied because user is not logged in. id=%d"
                .formatted(id);
            showLogger("COUNTRY", logMessage, "warn");
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            Country country = service.findById(id);
            model.addAttribute("country", country);
            model.addAttribute("mode", "edit");

            String logMessage = "User %s opened EDIT Country form for ID %d"
                .formatted(userName, id);
            showLogger("COUNTRY", logMessage, "info");

            return appLocation + "/Country/country-form";
        } catch (Exception e) {
            String logMessage = "FAILED opening edit page by user %s. id=%d, error=%s"
                .formatted(userName, id, e.getMessage());
            showLogger("COUNTRY", logMessage, "error");

            ra.addFlashAttribute("error", "Country not found");
            return "redirect:/countries";
        }
    }

    @PostMapping("/countries/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Country country,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            String logMessage = "Update denied because user is not logged in. id=%d"
                .formatted(id);
            showLogger("COUNTRY", logMessage, "warn");
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            Country existing = service.findById(id);

            country.setId(id);
            country.setIdCountry(existing.getIdCountry());
            country.setCode(emptyToNull(country.getCode() == null ? null : country.getCode().toUpperCase()));
            country.setName(emptyToNull(country.getName() == null ? null : country.getName().toUpperCase()));
            country.setCreatedDate(existing.getCreatedDate());
            country.setCreatedBy(existing.getCreatedBy());
            country.setUpdatedDate(LocalDate.now().toString());
            country.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            country.setDeletedDate(existing.getDeletedDate());
            country.setDeletedBy(existing.getDeletedBy());

            String validation = service.validate(country, id);
            if (validation != null) {
                String logMessage = "UPDATE FAILED by user %s. id=%d, validation=%s"
                    .formatted(userName, id, validation);
                showLogger("COUNTRY", logMessage, "warn");

                ra.addFlashAttribute("error", validation);
                return "redirect:/countries/" + id + "/edit";
            }

            if (country.getStatus() == null) {
                country.setStatus(existing.getStatus() == null ? 1 : existing.getStatus());
            }

            service.update(id, country);

            String logMessage = "UPDATE SUCCESS by user %s. id=%d, generatedId=%s, code=%s, name=%s"
                .formatted(
                    userName,
                    id,
                    country.getIdCountry(),
                    country.getCode(),
                    country.getName()
                );
            showLogger("COUNTRY", logMessage, "info");

            ra.addFlashAttribute("success", "Country updated successfully");
        } catch (Exception e) {
            String logMessage = "FAILED to update Country by user %s. id=%d, error=%s"
                .formatted(userName, id, e.getMessage());
            showLogger("COUNTRY", logMessage, "error");

            ra.addFlashAttribute("error", "Failed to update Country: " + e.getMessage());
            return "redirect:/countries/" + id + "/edit";
        }

        return "redirect:/countries";
    }
}