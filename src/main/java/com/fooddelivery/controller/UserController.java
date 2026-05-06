package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * UserController - Component 1: IT251022022
 * Handles all user CRUD operations
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ─── REGISTRATION (CREATE) ───────────────────────────────────────────────

    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) return "redirect:/";
        model.addAttribute("customer", new Customer());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String phone,
                           @RequestParam(defaultValue = "") String address,
                           @RequestParam(defaultValue = "") String city,
                           RedirectAttributes ra) {
        Customer c = new Customer();
        c.setUsername(username);
        c.setEmail(email);
        c.setPassword(password);
        c.setPhone(phone);
        c.setAddress(address);
        c.setCity(city);

        if (userService.register(c)) {
            ra.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } else {
            ra.addFlashAttribute("error", "Email already registered. Please use a different email.");
            return "redirect:/register";
        }
    }

    // ─── LOGIN (READ) ─────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) return "redirect:/";
        model.addAttribute("user", null);
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        User user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            if ("ADMIN".equals(user.getRole())) return "redirect:/admin/dashboard";
            return "redirect:/";
        }
        ra.addFlashAttribute("error", "Invalid email or password. Please try again.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ─── PROFILE (READ) ───────────────────────────────────────────────────────

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        User fresh = userService.findById(user.getId());
        model.addAttribute("user", fresh != null ? fresh : user);
        return "user/profile";
    }

    // ─── UPDATE PROFILE ───────────────────────────────────────────────────────

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String phone,
                                @RequestParam(defaultValue = "") String address,
                                @RequestParam(defaultValue = "") String city,
                                @RequestParam(defaultValue = "") String password,
                                HttpSession session,
                                RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Customer updated = new Customer();
        updated.setId(user.getId());
        updated.setUsername(username);
        updated.setEmail(user.getEmail());
        updated.setPhone(phone);
        updated.setAddress(address);
        updated.setCity(city);
        if (!password.isEmpty()) updated.setPassword(password);

        if (userService.updateUser(updated)) {
            session.setAttribute("user", updated);
            ra.addFlashAttribute("success", "Profile updated successfully!");
        } else {
            ra.addFlashAttribute("error", "Update failed. Please try again.");
        }
        return "redirect:/profile";
    }

    // ─── DELETE ACCOUNT ───────────────────────────────────────────────────────

    @PostMapping("/profile/delete")
    public String deleteAccount(HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (userService.deleteUser(user.getId())) {
            session.invalidate();
            ra.addFlashAttribute("success", "Account deleted successfully.");
            return "redirect:/";
        }
        ra.addFlashAttribute("error", "Failed to delete account.");
        return "redirect:/profile";
    }

    // ─── ADMIN USER MANAGEMENT ────────────────────────────────────────────────

    @GetMapping("/admin/users")
    public String adminUsers(HttpSession session, Model model,
                             @RequestParam(defaultValue = "") String search) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        model.addAttribute("user", user);
        if (!search.isEmpty()) {
            model.addAttribute("users", userService.searchUsers(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        return "admin/users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String adminDeleteUser(@PathVariable String id,
                                  HttpSession session,
                                  RedirectAttributes ra) {
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getRole())) return "redirect:/login";

        if ("ADM001".equals(id)) {
            ra.addFlashAttribute("error", "Cannot delete main admin account.");
            return "redirect:/admin/users";
        }
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User deleted successfully.");
        return "redirect:/admin/users";
    }
}

