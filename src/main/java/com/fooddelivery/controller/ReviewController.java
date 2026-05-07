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
 * ReviewController - Component 6: IT25100159
 */
@Controller
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired private RestaurantService restaurantService;
    @Autowired private OrderService orderService;

    // ─── SUBMIT REVIEW PAGE (CREATE) ──────────────────────────────────────────

    @GetMapping("/review/submit/{orderId}")
    public String submitPage(@PathVariable String orderId,
                             HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = orderService.findById(orderId);
        if (order == null || !order.getCustomerId().equals(user.getId()))
            return "redirect:/order/history";

        Restaurant restaurant = restaurantService.findById(order.getRestaurantId());
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("restaurant", restaurant);
        return "review/submit";
    }

    @PostMapping("/review/submit")
    public String submit(@RequestParam String restaurantId,
                         @RequestParam String restaurantName,
                         @RequestParam String orderId,
                         @RequestParam int rating,
                         @RequestParam String comment,
                         HttpSession session, RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Review review = new Review(null, user.getId(), user.getUsername(),
                restaurantId, restaurantName, orderId, rating, comment);
        reviewService.addReview(review);
        ra.addFlashAttribute("success", "Thank you for your review!");
        return "redirect:/restaurants/" + restaurantId;
    }

    // ─── VIEW REVIEWS (READ) ──────────────────────────────────────────────────

    @GetMapping("/reviews")
    public String viewAll(HttpSession session, Model model,
                          @RequestParam(defaultValue = "") String restaurantId) {
        model.addAttribute("user", session.getAttribute("user"));
        if (!restaurantId.isEmpty()) {
            model.addAttribute("reviews", reviewService.getReviewsByRestaurant(restaurantId));
            model.addAttribute("restaurant", restaurantService.findById(restaurantId));
        } else {
            model.addAttribute("reviews", reviewService.getAllReviews());
        }
        return "review/list";
    }

    // ─── MY REVIEWS ───────────────────────────────────────────────────────────

    @GetMapping("/review/my")
    public String myReviews(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviewService.getReviewsByCustomer(user.getId()));
        return "review/my-reviews";
    }

    // ─── EDIT REVIEW (UPDATE) ─────────────────────────────────────────────────

    @GetMapping("/review/edit/{id}")
    public String editPage(@PathVariable String id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Review review = reviewService.findById(id);
        if (review == null || !review.getCustomerId().equals(user.getId()))
            return "redirect:/review/my";
        model.addAttribute("user", user);
        model.addAttribute("review", review);
        return "review/edit";
    }

    @PostMapping("/review/edit/{id}")
    public String edit(@PathVariable String id,
                       @RequestParam int rating,
                       @RequestParam String comment,
                       HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Review review = reviewService.findById(id);
        if (review == null || !review.getCustomerId().equals(user.getId())) {
            ra.addFlashAttribute("error", "Unauthorized.");
            return "redirect:/review/my";
        }
        reviewService.updateReview(id, rating, comment);
        ra.addFlashAttribute("success", "Review updated successfully!");
        return "redirect:/review/my";
    }

    // ─── DELETE REVIEW (DELETE) ───────────────────────────────────────────────

    @PostMapping("/review/delete/{id}")
    public String delete(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Review review = reviewService.findById(id);
        if (review != null && review.getCustomerId().equals(user.getId())) {
            reviewService.deleteReview(id);
            ra.addFlashAttribute("success", "Review deleted.");
        }
        return "redirect:/review/my";
    }

    // ─── ADMIN: REVIEW PANEL ──────────────────────────────────────────────────

    @GetMapping("/admin/reviews")
    public String adminPanel(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        model.addAttribute("user", u);
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "admin/reviews";
    }

    @PostMapping("/admin/reviews/hide/{id}")
    public String adminHide(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        reviewService.hideReview(id);
        ra.addFlashAttribute("success", "Review hidden.");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/admin/reviews/restore/{id}")
    public String adminRestore(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        reviewService.restoreReview(id);
        ra.addFlashAttribute("success", "Review restored.");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/admin/reviews/delete/{id}")
    public String adminDelete(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        reviewService.deleteReview(id);
        ra.addFlashAttribute("success", "Review deleted permanently.");
        return "redirect:/admin/reviews";
    }
}
