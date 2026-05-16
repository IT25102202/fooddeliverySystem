package com.fooddelivery.service;

import com.fooddelivery.model.Order;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * OrderService - Component 4: IT25101831
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // CREATE - Place order
    public Order placeOrder(Order order) {
        order.setId("ORD" + System.currentTimeMillis());
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        order.setEstimatedDelivery(LocalDateTime.now().plusMinutes(40)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.saveLists(); // ensure string columns are populated before save
        return orderRepository.save(order);
    }

    // READ - Customer order history
    public List<Order> getOrdersByCustomer(String customerId) {
        List<Order> orders = orderRepository.findByCustomerIdOrderByOrderDateTimeDesc(customerId);
        orders.forEach(Order::loadLists);
        return orders;
    }

    // READ - All orders (admin)
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByOrderDateTimeDesc();
        orders.forEach(Order::loadLists);
        return orders;
    }

    // READ - Find by ID
    public Order findById(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) order.loadLists();
        return order;
    }

    // READ - By status
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findByStatus(status);
        orders.forEach(Order::loadLists);
        return orders;
    }

    // UPDATE - Update order status (Polymorphism: different handling per status)
    public boolean updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return false;
        order.loadLists();
        order.setStatus(newStatus);
        if ("DELIVERED".equals(newStatus)) order.setPaymentStatus("PAID");
        order.saveLists();
        orderRepository.save(order);
        return true;
    }

    // DELETE - Cancel order (customer)
    public boolean cancelOrder(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !customerId.equals(order.getCustomerId())) return false;
        order.loadLists();
        if (!order.canCancel()) return false;
        order.setStatus("CANCELLED");
        order.saveLists();
        orderRepository.save(order);
        return true;
    }

    // DELETE - Cancel order (admin)
    public boolean cancelOrderAdmin(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return false;
        order.loadLists();
        order.setStatus("CANCELLED");
        order.saveLists();
        orderRepository.save(order);
        return true;
    }

    public int countOrders() { return (int) orderRepository.count(); }

    public double getTotalRevenue() {
        return getAllOrders().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount).sum();
    }

    public void initSampleData() {
        if (orderRepository.count() == 0) {
            Order o1 = new Order();
            o1.setId("ORD001"); o1.setCustomerId("USR001");
            o1.setRestaurantId("RST003"); o1.setRestaurantName("Pizza Paradise");
            o1.getItemIds().add("FD009"); o1.getItemNames().add("Margherita Pizza");
            o1.getQuantities().add(1);    o1.getItemPrices().add(1450.0);
            o1.setTotalAmount(1625.0);    o1.setDeliveryFee(175.0);
            o1.setDeliveryAddress("45 Galle Road, Colombo");
            o1.setStatus("DELIVERED"); o1.setPaymentStatus("PAID");
            o1.setOrderDateTime("2025-01-10 14:30:00");
            o1.setEstimatedDelivery("2025-01-10 15:10:00");
            o1.saveLists();
            orderRepository.save(o1);
        }
    }

    // ANALYTICS - Orders by status count
    public Map<String, Long> getOrdersByStatus() {
        return getAllOrders().stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
    }

    // ANALYTICS - Revenue per restaurant (top 5)
    public Map<String, Double> getRevenueByRestaurant() {
        return getAllOrders().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .collect(Collectors.groupingBy(Order::getRestaurantName,
                        Collectors.summingDouble(Order::getTotalAmount)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    // ANALYTICS - Orders count per status for pie chart
    public long countByStatus(String status) {
        return getAllOrders().stream().filter(o -> status.equals(o.getStatus())).count();
    }

    public long countDeliveredOrders() { return countByStatus("DELIVERED"); }
    public long countPendingOrders()   { return countByStatus("PENDING"); }
    public long countCancelledOrders() { return countByStatus("CANCELLED"); }

}