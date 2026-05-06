package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * FoodItemController - Component 3: IT25100897
 */
@Controller
@RequestMapping("/admin/food")
public class FoodItemController {

    @Autowired private FoodItemService foodItemService;
    @Autowired private RestaurantService restaurantService;

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("user");
        return u != null && "ADMIN".equals(u.getRole());
    }

    // ─── LIST ALL FOOD ITEMS (READ) ───────────────────────────────────────────

    @GetMapping
    public String list(HttpSession session, Model model,
                       @RequestParam(defaultValue = "") String search,
                       @RequestParam(defaultValue = "") String restaurantId) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("user", session.getAttribute("user"));

        java.util.List<FoodItem> items;
        if (!search.isEmpty()) {
            items = foodItemService.searchFoodItems(search);
        } else if (!restaurantId.isEmpty()) {
            items = foodItemService.getFoodItemsByRestaurant(restaurantId);
        } else {
            items = foodItemService.getAllFoodItems();
        }

        model.addAttribute("foodItems", items);
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        model.addAttribute("search", search);
        model.addAttribute("selectedRestaurant", restaurantId);
        return "admin/food-items";
    }

    // ─── ADD FOOD ITEM FORM (CREATE) ──────────────────────────────────────────

    @GetMapping("/add")
    public String addPage(HttpSession session, Model model,
                          @RequestParam(defaultValue = "") String restaurantId) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        model.addAttribute("selectedRestaurant", restaurantId);
        model.addAttribute("foodItem", new FoodItem());
        model.addAttribute("editMode", false);
        return "admin/food-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam String description,
                      @RequestParam double price,
                      @RequestParam String restaurantId,
                      @RequestParam String category,
                      @RequestParam(defaultValue = "true") boolean isAvailable,
                      @RequestParam(defaultValue = "false") boolean isVeg,
                      @RequestParam(defaultValue = "") String imageUrl,
                      @RequestParam(defaultValue = "15") int prepTime,
                      HttpSession session, RedirectAttributes ra) {

        if (!isAdmin(session)) return "redirect:/login";

        if (imageUrl.isEmpty()) {
            imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400";
        }

        FoodItem item = createFoodItemByCategory(null, name, description, price,
                restaurantId, category, isAvailable, isVeg, imageUrl, prepTime);
        foodItemService.addFoodItem(item);
        ra.addFlashAttribute("success", "Food item added successfully!");
        return "redirect:/admin/food";
    }

    // ─── EDIT FOOD ITEM (UPDATE) ──────────────────────────────────────────────

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        FoodItem item = foodItemService.findById(id);
        if (item == null) return "redirect:/admin/food";
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("foodItem", item);
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        model.addAttribute("editMode", true);
        return "admin/food-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable String id,
                       @RequestParam String name,
                       @RequestParam String description,
                       @RequestParam double price,
                       @RequestParam String restaurantId,
                       @RequestParam String category,
                       @RequestParam(defaultValue = "false") boolean isAvailable,
                       @RequestParam(defaultValue = "false") boolean isVeg,
                       @RequestParam String imageUrl,
                       @RequestParam int prepTime,
                       HttpSession session, RedirectAttributes ra) {

        if (!isAdmin(session)) return "redirect:/login";
        FoodItem item = createFoodItemByCategory(id, name, description, price,
                restaurantId, category, isAvailable, isVeg, imageUrl, prepTime);
        foodItemService.updateFoodItem(item);
        ra.addFlashAttribute("success", "Food item updated successfully!");
        return "redirect:/admin/food";
    }

    // ─── HELPER: Create correct subclass by category ──────────────────────────

    private FoodItem createFoodItemByCategory(String id, String name, String description,
                                               double price, String restaurantId, String category,
                                               boolean isAvailable, boolean isVeg,
                                               String imageUrl, int prepTime) {
        switch (category.toUpperCase()) {
            case "MAINCOURSE": {
                MainCourse mc = new MainCourse();
                mc.setId(id); mc.setName(name); mc.setDescription(description);
                mc.setPrice(price); mc.setRestaurantId(restaurantId);
                mc.setAvailable(isAvailable); mc.setVeg(isVeg);
                mc.setImageUrl(imageUrl); mc.setPrepTime(prepTime);
                return mc;
            }
            case "DESSERT": {
                Dessert d = new Dessert();
                d.setId(id); d.setName(name); d.setDescription(description);
                d.setPrice(price); d.setRestaurantId(restaurantId);
                d.setAvailable(isAvailable); d.setVeg(isVeg);
                d.setImageUrl(imageUrl); d.setPrepTime(prepTime);
                return d;
            }
            case "BEVERAGE": {
                Beverage b = new Beverage();
                b.setId(id); b.setName(name); b.setDescription(description);
                b.setPrice(price); b.setRestaurantId(restaurantId);
                b.setAvailable(isAvailable); b.setVeg(isVeg);
                b.setImageUrl(imageUrl); b.setPrepTime(prepTime);
                return b;
            }
            default: {
                return new FoodItem(id, name, description, price, restaurantId,
                        category, isAvailable, isVeg, imageUrl, prepTime);
            }
        }
    }

    // ─── DELETE FOOD ITEM ─────────────────────────────────────────────────────

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        foodItemService.deleteFoodItem(id);
        ra.addFlashAttribute("success", "Food item deleted.");
        return "redirect:/admin/food";
    }
}
