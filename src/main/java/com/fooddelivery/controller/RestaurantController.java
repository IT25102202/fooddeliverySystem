package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class RestaurantController {

    @Autowired private RestaurantService restaurantService;
    @Autowired private FoodItemService foodItemService;
    @Autowired private ReviewService reviewService;

    // ─── LIST / SEARCH (READ) ─────────────────────────────────────────────────

    @GetMapping("/restaurants")
    public String list(HttpSession session, Model model,
                       @RequestParam(defaultValue = "") String search,
                       @RequestParam(defaultValue = "") String type,
                       @RequestParam(defaultValue = "") String city) {
        model.addAttribute("user", session.getAttribute("user"));

        java.util.List<com.fooddelivery.model.Restaurant> restaurants;
        if (!search.isEmpty()) {
            restaurants = restaurantService.searchRestaurants(search);
        } else if (!type.isEmpty()) {
            restaurants = restaurantService.filterByType(type);
        } else if (!city.isEmpty()) {
            restaurants = restaurantService.filterByCity(city);
        } else {
            restaurants = restaurantService.getAllRestaurants();
        }

        model.addAttribute("restaurants", restaurants);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        model.addAttribute("city", city);
        return "restaurant/list";
    }

    // ─── DETAIL PAGE ──────────────────────────────────────────────────────────

    @GetMapping("/restaurants/{id}")
    public String detail(@PathVariable String id, HttpSession session, Model model) {
        com.fooddelivery.model.Restaurant r = restaurantService.findById(id);
        if (r == null) return "redirect:/restaurants";

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("restaurant", r);
        model.addAttribute("mainCourses", foodItemService.getFoodItemsByCategory(id, "MAINCOURSE"));
        model.addAttribute("desserts",    foodItemService.getFoodItemsByCategory(id, "DESSERT"));
        model.addAttribute("beverages",   foodItemService.getFoodItemsByCategory(id, "BEVERAGE"));
        model.addAttribute("reviews",     reviewService.getReviewsByRestaurant(id));
        model.addAttribute("avgRating",   reviewService.getAverageRating(id));
        return "restaurant/detail";
    }

    // ─── ADMIN: ADD RESTAURANT (CREATE) ───────────────────────────────────────

    @GetMapping("/admin/restaurants/add")
    public String addPage(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        model.addAttribute("user", u);
        model.addAttribute("restaurant", new com.fooddelivery.model.Restaurant());
        return "admin/restaurant-form";
    }

    @PostMapping("/admin/restaurants/add")
    public String add(@RequestParam String name,
                      @RequestParam String address,
                      @RequestParam String city,
                      @RequestParam String phone,
                      @RequestParam String cuisine,
                      @RequestParam String type,
                      @RequestParam(defaultValue = "4.0") double rating,
                      @RequestParam(defaultValue = "true") boolean isOpen,
                      @RequestParam(defaultValue = "") String imageUrl,
                      @RequestParam(defaultValue = "") String description,
                      @RequestParam(defaultValue = "08:00") String openTime,
                      @RequestParam(defaultValue = "22:00") String closeTime,
                      @RequestParam(defaultValue = "150") double deliveryFee,
                      @RequestParam(defaultValue = "0") int discountPercent,
                      @RequestParam(defaultValue = "") String discountLabel,
                      HttpSession session, RedirectAttributes ra) {

        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";

        if (imageUrl.isEmpty()) {
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600";
        }

        com.fooddelivery.model.Restaurant r = new com.fooddelivery.model.Restaurant(null, name, address, city, phone, cuisine, type,
                rating, isOpen, imageUrl, description, openTime, closeTime, deliveryFee);
        r.setDiscountPercent(discountPercent);
        r.setDiscountLabel(discountLabel.isEmpty() ? null : discountLabel);
        restaurantService.addRestaurant(r);
        ra.addFlashAttribute("success", "Restaurant added successfully!");
        return "redirect:/admin/restaurants";
    }

    // ─── ADMIN: EDIT RESTAURANT (UPDATE) ──────────────────────────────────────

    @GetMapping("/admin/restaurants/edit/{id}")
    public String editPage(@PathVariable String id, HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        com.fooddelivery.model.Restaurant r = restaurantService.findById(id);
        if (r == null) return "redirect:/admin/restaurants";
        model.addAttribute("user", u);
        model.addAttribute("restaurant", r);
        return "admin/restaurant-form";
    }

    @PostMapping("/admin/restaurants/edit/{id}")
    public String edit(@PathVariable String id,
                       @RequestParam String name,
                       @RequestParam String address,
                       @RequestParam String city,
                       @RequestParam String phone,
                       @RequestParam String cuisine,
                       @RequestParam String type,
                       @RequestParam double rating,
                       @RequestParam(defaultValue = "false") boolean isOpen,
                       @RequestParam String imageUrl,
                       @RequestParam String description,
                       @RequestParam String openTime,
                       @RequestParam String closeTime,
                       @RequestParam double deliveryFee,
                       @RequestParam(defaultValue = "0") int discountPercent,
                       @RequestParam(defaultValue = "") String discountLabel,
                       HttpSession session, RedirectAttributes ra) {

        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";

        com.fooddelivery.model.Restaurant r = new com.fooddelivery.model.Restaurant(id, name, address, city, phone, cuisine, type,
                rating, isOpen, imageUrl, description, openTime, closeTime, deliveryFee);
        r.setDiscountPercent(discountPercent);
        r.setDiscountLabel(discountLabel.isEmpty() ? null : discountLabel);
        restaurantService.updateRestaurant(r);
        ra.addFlashAttribute("success", "Restaurant updated successfully!");
        return "redirect:/admin/restaurants";
    }

    // ─── ADMIN: DELETE RESTAURANT ─────────────────────────────────────────────

    @PostMapping("/admin/restaurants/delete/{id}")
    public String delete(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        foodItemService.deleteByRestaurant(id);
        restaurantService.deleteRestaurant(id);
        ra.addFlashAttribute("success", "Restaurant removed successfully!");
        return "redirect:/admin/restaurants";
    }

    // ─── ADMIN: LIST ──────────────────────────────────────────────────────────

    @GetMapping("/admin/restaurants")
    public String adminList(HttpSession session, Model model,
                            @RequestParam(defaultValue = "") String search) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        model.addAttribute("user", u);
        if (!search.isEmpty()) {
            model.addAttribute("restaurants", restaurantService.searchRestaurants(search));
        } else {
            model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        }
        model.addAttribute("search", search);
        return "admin/restaurants";
    }
}
