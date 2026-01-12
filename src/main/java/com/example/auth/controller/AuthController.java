package com.example.auth.controller;

import com.example.auth.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        return (session.getAttribute("userEmail") != null) ? "redirect:/dashboard" : "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        var userOpt = userService.authenticate(email, password);
        if (userOpt.isEmpty()) {
            return "redirect:/login?error=Email atau password salah";
        }

        // dipakai oleh menu lain (mis. /items) untuk validasi login
        session.setAttribute("userId", userOpt.get().getId());
        session.setAttribute("userEmail", userOpt.get().getEmail());
        session.setAttribute("userName", userOpt.get().getName());
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String registerPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String success,
                               Model model) {
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
        @RequestParam String email,
        @RequestParam String password) {

        if (name == null || name.trim().isEmpty()) {
            return "redirect:/register?error=Nama wajib diisi";
        }
        if (email == null || email.trim().isEmpty()) {
            return "redirect:/register?error=Email wajib diisi";
        }
        if (password == null || password.length() < 6) {
            return "redirect:/register?error=Password minimal 6 karakter";
        }
        if (userService.emailExists(email)) {
            return "redirect:/register?error=Email sudah terdaftar";
        }

        userService.register(name.trim(), email.trim().toLowerCase(), password);
        return "redirect:/login?error=Berhasil daftar, silakan login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        var email = session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        model.addAttribute("name", session.getAttribute("userName"));
        model.addAttribute("email", email);
        return "dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
