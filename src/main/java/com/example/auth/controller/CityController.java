package com.example.auth.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.auth.model.City;
import com.example.auth.model.Country;
import com.example.auth.model.Province;
import com.example.auth.service.CityService;
import com.example.auth.service.CountryService;
import com.example.auth.service.ProvinceService;
import com.example.auth.util.GenerateController;

import jakarta.servlet.http.HttpSession;

@Controller
public class CityController extends GenerateController {

    private final CityService service;
    private final CountryService countryService;
    private final ProvinceService provinceService;

    @Value("${app.pagination.page-size:10}")
    private int defaultPageSize;

    @Value("${app.location:Page_Components}")
    private String appLocation;

    public CityController(CityService service, CountryService countryService, ProvinceService provinceService) {
        this.service = service;
        this.countryService = countryService;
        this.provinceService = provinceService;
    }

    @ModelAttribute("countries")
    public List<Country> countries() {
        return countryService.findAll();
    }

    @ModelAttribute("provinces")
    public List<Province> provinces() {
        return provinceService.findAll();
    }

    private String emptyToNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    private String emptyToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    @GetMapping("/cities")
    public String cities(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "search", defaultValue = "") String search,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            showLogger(
                "CITY",
                "Access denied to list page because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);
        String logMessage = "User %s opened City list page. page=%d, size=%s"
            .formatted(userName, page, size);
        showLogger("CITY", logMessage, "info");

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

        model.addAttribute("cities", service.getPage(search, page, pageSize));
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < totalPages - 1);
        model.addAttribute("pageNumbers", IntStream.range(0, totalPages).boxed().toList());

        return appLocation + "/City/cities";
    }

    @GetMapping("/cities/new")
    public String newForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            showLogger(
                "CITY",
                "Access denied to create page because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);
        String logMessage = "User %s opened NEW City form."
            .formatted(userName);
        showLogger("CITY", logMessage, "info");

        City city = new City();
        city.setStatus(1);

        model.addAttribute("city", city);
        model.addAttribute("mode", "new");

        return appLocation + "/City/city-form";
    }

    @PostMapping("/cities")
    public String save(
            @ModelAttribute City city,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            showLogger(
                "CITY",
                "Insert denied because user is not logged in.",
                "warn"
            );
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            city.setIdCountry(emptyToNull(city.getIdCountry()));
            city.setIdProvince(emptyToNull(city.getIdProvince()));
            city.setCode(emptyToNull(city.getCode() == null ? null : city.getCode().toUpperCase()));
            city.setName(emptyToNull(city.getName() == null ? null : city.getName().toUpperCase()));

            String validation = service.validate(city, null);
            if (validation != null) {
                String logMessage = "INSERT FAILED by user %s. validation=%s"
                    .formatted(userName, validation);
                showLogger("CITY", logMessage, "warn");

                ra.addFlashAttribute("error", validation);
                return "redirect:/cities/new";
            }

            long totalData = service.countAll("");
            city.setIdCity(generateRandomString(3, city.getName(), totalData));
            city.setCreatedDate(LocalDate.now().toString());
            city.setCreatedBy(String.valueOf(session.getAttribute("userId")));
            city.setUpdatedDate(LocalDate.now().toString());
            city.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            city.setDeletedDate(null);
            city.setDeletedBy(null);

            if (city.getStatus() == null) {
                city.setStatus(1);
            }

            service.save(city);

            String logMessage = "INSERT SUCCESS by user %s. generatedId=%s, idCountry=%s, idProvince=%s, code=%s, name=%s"
                .formatted(
                    userName,
                    city.getIdCity(),
                    city.getIdCountry(),
                    city.getIdProvince(),
                    city.getCode(),
                    city.getName()
                );
            showLogger("CITY", logMessage, "info");

            ra.addFlashAttribute("success", "City saved successfully");
        } catch (Exception e) {
            String logMessage = "INSERT FAILED by user %s. code=%s, name=%s, error=%s"
                .formatted(
                    userName,
                    city.getCode(),
                    city.getName(),
                    e.getMessage()
                );
            showLogger("CITY", logMessage, "error");

            ra.addFlashAttribute("error", "Failed to save City: " + e.getMessage());
            return "redirect:/cities/new";
        }

        return "redirect:/cities";
    }

    @GetMapping("/cities/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            String logMessage = "Edit denied because user is not logged in. id=%d"
                .formatted(id);
            showLogger("CITY", logMessage, "warn");
            return "redirect:/login";
        }

        String userName = getLoginUserName(session);

        try {
            City city = service.findById(id);
            model.addAttribute("city", city);
            model.addAttribute("mode", "edit");

            String logMessage = "User %s opened EDIT City form for ID %d"
                .formatted(userName, id);
            showLogger("CITY", logMessage, "info");

            return appLocation + "/City/city-form";
        } catch (Exception e) {
            String logMessage = "FAILED opening edit page by user %s. id=%d, error=%s"
                .formatted(userName, id, e.getMessage());
            showLogger("CITY", logMessage, "error");

            ra.addFlashAttribute("error", "City not found");
            return "redirect:/cities";
        }
    }

    @PostMapping("/cities/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute City city,
            RedirectAttributes ra,
            HttpSession session,
            Model model
    ) {
        if (!isLoggedIn(session)) {
            String logMessage = "Update denied because user is not logged in. id=%d"
                .formatted(id);
            showLogger("CITY", logMessage, "warn");
            return "redirect:/login";
        }

        model.addAttribute("mode", "edit");
        String userName = getLoginUserName(session);

        try {
            City existing = service.findById(id);

            city.setId(id);
            city.setIdCity(existing.getIdCity());
            city.setIdCountry(emptyToNull(city.getIdCountry()));
            city.setIdProvince(emptyToNull(city.getIdProvince()));
            city.setCode(emptyToNull(city.getCode() == null ? null : city.getCode().toUpperCase()));
            city.setName(emptyToNull(city.getName() == null ? null : city.getName().toUpperCase()));
            city.setCreatedDate(existing.getCreatedDate());
            city.setCreatedBy(existing.getCreatedBy());
            city.setUpdatedDate(LocalDate.now().toString());
            city.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
            city.setDeletedDate(existing.getDeletedDate());
            city.setDeletedBy(existing.getDeletedBy());

            String validation = service.validate(city, id);
            if (validation != null) {
                String logMessage = "UPDATE FAILED by user %s. id=%d, validation=%s"
                    .formatted(userName, id, validation);
                showLogger("CITY", logMessage, "warn");

                ra.addFlashAttribute("error", validation);
                return "redirect:/cities/" + id + "/edit";
            }

            // if (city.getStatus() == null) {
            //     city.setStatus(existing.getStatus() == null ? 1 : existing.getStatus());
            // }

            service.update(id, city);

            String logMessage = "UPDATE SUCCESS by user %s. id=%d, generatedId=%s, idCountry=%s, idProvince=%s, code=%s, name=%s"
                .formatted(
                    userName,
                    id,
                    city.getIdCity(),
                    city.getIdCountry(),
                    city.getIdProvince(),
                    city.getCode(),
                    city.getName()
                );
            showLogger("CITY", logMessage, "info");

            ra.addFlashAttribute("success", "City updated successfully");
        } catch (Exception e) {
            String logMessage = "FAILED to update City by user %s. id=%d, error=%s"
                .formatted(userName, id, e.getMessage());
            showLogger("CITY", logMessage, "error");

            ra.addFlashAttribute("error", "Failed to update City: " + e.getMessage());
            return "redirect:/cities/" + id + "/edit";
        }

        return "redirect:/cities";
    }
}