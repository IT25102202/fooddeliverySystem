package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * OrderController - Component 4: IT25101831
 */
@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private RestaurantService restaurantService;
    @Autowired private FoodItemService foodItemService;
    @Autowired private PaymentService paymentService;

    // ─── PLACE ORDER PAGE (CREATE) ────────────────────────────────────────────

    @GetMapping("/order/place/{restaurantId}")
    public String placePage(@PathVariable String restaurantId,
                            HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Restaurant r = restaurantService.findById(restaurantId);
        if (r == null) return "redirect:/restaurants";

        model.addAttribute("user", user);
        model.addAttribute("restaurant", r);
        model.addAttribute("mainCourses", foodItemService.getFoodItemsByCategory(restaurantId, "MAINCOURSE"));
        model.addAttribute("desserts",    foodItemService.getFoodItemsByCategory(restaurantId, "DESSERT"));
        model.addAttribute("beverages",   foodItemService.getFoodItemsByCategory(restaurantId, "BEVERAGE"));
        return "order/place";
    }

    @PostMapping("/order/place")
    public String placeOrder(@RequestParam String restaurantId,
                             @RequestParam String restaurantName,
                             @RequestParam String deliveryAddress,
                             @RequestParam(defaultValue = "") String specialInstructions,
                             @RequestParam Map<String, String> allParams,
                             HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = new Order();
        order.setCustomerId(user.getId());
        order.setRestaurantId(restaurantId);
        order.setRestaurantName(restaurantName);
        order.setDeliveryAddress(deliveryAddress);
        order.setSpecialInstructions(specialInstructions);

        double total = 0;
        // Parse item quantities from form params: qty_<itemId>=<quantity>
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("qty_")) {
                int qty;
                try { qty = Integer.parseInt(entry.getValue()); } catch (Exception e) { continue; }
                if (qty <= 0) continue;
                String itemId = entry.getKey().substring(4);
                FoodItem item = foodItemService.findById(itemId);
                if (item != null) {
                    order.getItemIds().add(itemId);
                    order.getItemNames().add(item.getName());
                    order.getQuantities().add(qty);
                    order.getItemPrices().add(item.getPrice());
                    total += item.getPrice() * qty;
                }
            }
        }

        if (order.getItemIds().isEmpty()) {
            ra.addFlashAttribute("error", "Please select at least one item.");
            return "redirect:/order/place/" + restaurantId;
        }

        Restaurant rest = restaurantService.findById(restaurantId);
        double deliveryFee = rest != null ? rest.getDeliveryFee() : 150.0;

        // Apply online discount if restaurant offers one
        double discountAmount = 0;
        if (rest != null && rest.hasDiscount()) {
            discountAmount = Math.round(total * rest.getDiscountPercent() / 100.0);
            total -= discountAmount;
        }

        order.setDeliveryFee(deliveryFee);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(total + deliveryFee);

        Order placed = orderService.placeOrder(order);
        session.setAttribute("lastOrderId", placed.getId());
        return "redirect:/order/payment/" + placed.getId();
    }

    // ─── PAYMENT PAGE ─────────────────────────────────────────────────────────

    @GetMapping("/order/payment/{orderId}")
    public String paymentPage(@PathVariable String orderId,
                              HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = orderService.findById(orderId);
        if (order == null || !order.getCustomerId().equals(user.getId()))
            return "redirect:/order/history";

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        return "order/payment";
    }

    // ─── ORDER HISTORY (READ) ─────────────────────────────────────────────────

    @GetMapping("/order/history")
    public String history(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersByCustomer(user.getId()));
        return "order/history";
    }

    // ─── ORDER STATUS (READ) ──────────────────────────────────────────────────

    @GetMapping("/order/status/{orderId}")
    public String status(@PathVariable String orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = orderService.findById(orderId);
        if (order == null || !order.getCustomerId().equals(user.getId()))
            return "redirect:/order/history";

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        Payment payment = paymentService.findByOrderId(orderId);
        model.addAttribute("payment", payment);
        return "order/status";
    }

    // ─── CANCEL ORDER (DELETE) ────────────────────────────────────────────────

    @PostMapping("/order/cancel/{orderId}")
    public String cancel(@PathVariable String orderId, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (orderService.cancelOrder(orderId, user.getId())) {
            ra.addFlashAttribute("success", "Order cancelled successfully.");
        } else {
            ra.addFlashAttribute("error", "Cannot cancel this order. It may already be on the way.");
        }
        return "redirect:/order/history";
    }

    // ─── ADMIN: ALL ORDERS ────────────────────────────────────────────────────

    @GetMapping("/admin/orders")
    public String adminOrders(HttpSession session, Model model,
                              @RequestParam(defaultValue = "") String status) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        model.addAttribute("user", u);
        if (!status.isEmpty()) {
            model.addAttribute("orders", orderService.getOrdersByStatus(status));
        } else {
            model.addAttribute("orders", orderService.getAllOrders());
        }
        model.addAttribute("filterStatus", status);
        return "admin/orders";
    }

    // ─── ADMIN: UPDATE ORDER STATUS (UPDATE) ──────────────────────────────────

    @PostMapping("/admin/orders/status/{orderId}")
    public String adminUpdateStatus(@PathVariable String orderId,
                                    @RequestParam String status,
                                    HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        orderService.updateOrderStatus(orderId, status);
        ra.addFlashAttribute("success", "Order status updated to: " + status);
        return "redirect:/admin/orders";
    }

    // ─── ADMIN: CANCEL ORDER ──────────────────────────────────────────────────

    @PostMapping("/admin/orders/cancel/{orderId}")
    public String adminCancel(@PathVariable String orderId,
                              HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        orderService.cancelOrderAdmin(orderId);
        ra.addFlashAttribute("success", "Order cancelled.");
        return "redirect:/admin/orders";
    }
}

