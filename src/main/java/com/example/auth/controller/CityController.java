package com.example.auth.controller;

import com.example.auth.model.City;
import com.example.auth.model.Country;
import com.example.auth.model.Province;
import com.example.auth.service.CityService;
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

    @GetMapping("/cities")
    public String cities(
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
        if (!isLoggedIn(session)) return "redirect:/login";

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
        if (!isLoggedIn(session)) return "redirect:/login";

        city.setIdCountry(emptyToNull(city.getIdCountry()));
        city.setIdProvince(emptyToNull(city.getIdProvince()));
        city.setCode(emptyToNull(city.getCode()));
        city.setName(emptyToNull(city.getName()));

        String validation = service.validate(city, null);
        if (validation != null) {
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

        if (city.getStatus() == null) city.setStatus(1);

        service.save(city);
        ra.addFlashAttribute("success", "City saved successfully");

        return "redirect:/cities";
    }

    @GetMapping("/cities/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        try {
            City city = service.findById(id);
            model.addAttribute("city", city);
            model.addAttribute("mode", "edit");

            return appLocation + "/City/city-form";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "City not found");
            return "redirect:/cities";
        }
    }

    @PostMapping("/cities/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute City city,
            RedirectAttributes ra,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) return "redirect:/login";

        City existing = service.findById(id);

        city.setId(id);
        city.setIdCity(existing.getIdCity());
        city.setIdCountry(emptyToNull(city.getIdCountry()));
        city.setIdProvince(emptyToNull(city.getIdProvince()));
        city.setCode(emptyToNull(city.getCode()));
        city.setName(emptyToNull(city.getName()));
        city.setCreatedDate(existing.getCreatedDate());
        city.setCreatedBy(existing.getCreatedBy());
        city.setUpdatedDate(LocalDate.now().toString());
        city.setUpdatedBy(String.valueOf(session.getAttribute("userId")));
        city.setDeletedDate(existing.getDeletedDate());
        city.setDeletedBy(existing.getDeletedBy());

        String validation = service.validate(city, id);
        if (validation != null) {
            ra.addFlashAttribute("error", validation);
            return "redirect:/cities/" + id + "/edit";
        }

        if (city.getStatus() == null) {
            city.setStatus(existing.getStatus());
        }

        service.update(id, city);
        ra.addFlashAttribute("success", "City updated successfully");

        return "redirect:/cities";
    }
}