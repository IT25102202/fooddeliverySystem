package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * PaymentController - Component 5: IT25101967
 */
@Controller
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private OrderService orderService;

    // ─── PROCESS PAYMENT (CREATE) ─────────────────────────────────────────────

    @PostMapping("/payment/process")
    public String process(@RequestParam String orderId,
                          @RequestParam String method,
                          @RequestParam(defaultValue = "") String cardNumber,
                          @RequestParam(defaultValue = "") String cardHolder,
                          @RequestParam(defaultValue = "VISA") String cardType,
                          @RequestParam(defaultValue = "0") double amountTendered,
                          HttpSession session, RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = orderService.findById(orderId);
        if (order == null || !order.getCustomerId().equals(user.getId())) {
            ra.addFlashAttribute("error", "Invalid order.");
            return "redirect:/order/history";
        }

        Map<String, String> details = new HashMap<>();
        if ("CARD".equalsIgnoreCase(method)) {
            String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "0000";
            details.put("cardLast4", last4);
            details.put("cardHolder", cardHolder);
            details.put("cardType", cardType);
        } else {
            details.put("amountTendered", String.valueOf(amountTendered > 0 ? amountTendered : order.getTotalAmount()));
        }

        Payment payment = paymentService.recordPayment(orderId, user.getId(), order.getTotalAmount(), method, details);

        if ("COMPLETED".equals(payment.getStatus())) {
            orderService.updateOrderStatus(orderId, "CONFIRMED");
            return "redirect:/payment/confirmation/" + payment.getId();
        } else {
            ra.addFlashAttribute("error", "Payment failed. Please try again.");
            return "redirect:/order/payment/" + orderId;
        }
    }

    // ─── PAYMENT CONFIRMATION PAGE ────────────────────────────────────────────

    @GetMapping("/payment/confirmation/{paymentId}")
    public String confirmation(@PathVariable String paymentId,
                               HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Payment payment = paymentService.findById(paymentId);
        if (payment == null || !payment.getCustomerId().equals(user.getId()))
            return "redirect:/order/history";

        Order order = orderService.findById(payment.getOrderId());
        model.addAttribute("user", user);
        model.addAttribute("payment", payment);
        model.addAttribute("order", order);
        return "payment/confirmation";
    }

    // ─── PAYMENT HISTORY (READ) ───────────────────────────────────────────────

    @GetMapping("/payment/history")
    public String history(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("payments", paymentService.getPaymentsByCustomer(user.getId()));
        return "payment/history";
    }

    // ─── ADMIN: ALL PAYMENTS ──────────────────────────────────────────────────

    @GetMapping("/admin/payments")
    public String adminPayments(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        model.addAttribute("user", u);
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        return "admin/payments";
    }

    // ─── ADMIN: UPDATE PAYMENT STATUS (UPDATE) ────────────────────────────────

    @PostMapping("/admin/payments/update/{id}")
    public String adminUpdate(@PathVariable String id,
                              @RequestParam String status,
                              HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        paymentService.updatePaymentStatus(id, status);
        ra.addFlashAttribute("success", "Payment status updated.");
        return "redirect:/admin/payments";
    }

    // ─── ADMIN: DELETE PAYMENT RECORD (DELETE) ────────────────────────────────

    @PostMapping("/admin/payments/delete/{id}")
    public String adminDelete(@PathVariable String id,
                              HttpSession session, RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || !"ADMIN".equals(u.getRole())) return "redirect:/login";
        paymentService.deletePayment(id);
        ra.addFlashAttribute("success", "Payment record deleted.");
        return "redirect:/admin/payments";
    }

    // ─── DOWNLOAD INVOICE PDF (CARD PAYMENTS ONLY) ───────────────────────────

    @GetMapping("/payment/invoice/{paymentId}")
    public void downloadInvoice(@PathVariable String paymentId,
                                HttpSession session,
                                HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) { response.sendRedirect("/login"); return; }

        Payment payment = paymentService.findById(paymentId);
        if (payment == null || !payment.getCustomerId().equals(user.getId())) {
            response.sendRedirect("/order/history"); return;
        }

        Order order = orderService.findById(payment.getOrderId());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=FoodHub-Invoice-" + payment.getId() + ".pdf");

        Document doc = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        // Colors
        Color orange  = new Color(249, 115, 22);
        Color dark    = new Color(26, 26, 26);
        Color muted   = new Color(107, 114, 128);
        Color light   = new Color(249, 250, 251);
        Color green   = new Color(22, 163, 74);

        // Fonts
        Font titleFont   = new Font(Font.HELVETICA, 26, Font.BOLD, orange);
        Font headFont    = new Font(Font.HELVETICA, 11, Font.BOLD, dark);
        Font normalFont  = new Font(Font.HELVETICA, 10, Font.NORMAL, dark);
        Font mutedFont   = new Font(Font.HELVETICA, 9, Font.NORMAL, muted);
        Font greenFont   = new Font(Font.HELVETICA, 12, Font.BOLD, green);
        Font whiteFont   = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font tblHead     = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);

        // ── Header bar ──
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{60, 40});

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setBackgroundColor(orange);
        logoCell.setPadding(18);
        Paragraph logo = new Paragraph("🍔 FoodHub", titleFont);
        logo.setFont(new Font(Font.HELVETICA, 24, Font.BOLD, Color.WHITE));
        logoCell.addElement(logo);
        Paragraph tagline = new Paragraph("Online Food Delivery", new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(255,200,150)));
        logoCell.addElement(tagline);
        header.addCell(logoCell);

        PdfPCell invCell = new PdfPCell();
        invCell.setBorder(Rectangle.NO_BORDER);
        invCell.setBackgroundColor(dark);
        invCell.setPadding(18);
        invCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph invTitle = new Paragraph("INVOICE", new Font(Font.HELVETICA, 18, Font.BOLD, Color.WHITE));
        invTitle.setAlignment(Element.ALIGN_RIGHT);
        invCell.addElement(invTitle);
        Paragraph invId = new Paragraph(payment.getId(), new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(180,180,180)));
        invId.setAlignment(Element.ALIGN_RIGHT);
        invCell.addElement(invId);
        Paragraph invDate = new Paragraph(payment.getPaymentDateTime() != null ? payment.getPaymentDateTime() : "",
                new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(180,180,180)));
        invDate.setAlignment(Element.ALIGN_RIGHT);
        invCell.addElement(invDate);
        header.addCell(invCell);

        doc.add(header);
        doc.add(Chunk.NEWLINE);

        // ── Bill To / Order Info ──
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10);

        PdfPCell billCell = new PdfPCell();
        billCell.setBorder(Rectangle.NO_BORDER);
        billCell.setBackgroundColor(light);
        billCell.setPadding(12);
        billCell.addElement(new Paragraph("BILL TO", new Font(Font.HELVETICA, 8, Font.BOLD, muted)));
        billCell.addElement(new Paragraph(user.getUsername(), headFont));
        billCell.addElement(new Paragraph(user.getEmail() != null ? user.getEmail() : "", normalFont));
        if (order != null && order.getDeliveryAddress() != null)
            billCell.addElement(new Paragraph(order.getDeliveryAddress(), normalFont));
        infoTable.addCell(billCell);

        PdfPCell orderCell = new PdfPCell();
        orderCell.setBorder(Rectangle.NO_BORDER);
        orderCell.setBackgroundColor(light);
        orderCell.setPadding(12);
        orderCell.addElement(new Paragraph("ORDER DETAILS", new Font(Font.HELVETICA, 8, Font.BOLD, muted)));
        orderCell.addElement(new Paragraph("Order ID: " + payment.getOrderId(), normalFont));
        if (order != null) orderCell.addElement(new Paragraph("Restaurant: " + order.getRestaurantName(), normalFont));
        orderCell.addElement(new Paragraph("Payment: " + payment.getMethod(), normalFont));
        orderCell.addElement(new Paragraph("Transaction: " + payment.getTransactionRef(), normalFont));
        infoTable.addCell(orderCell);

        doc.add(infoTable);
        doc.add(Chunk.NEWLINE);

        // ── Items Table ──
        PdfPTable itemsTable = new PdfPTable(4);
        itemsTable.setWidthPercentage(100);
        itemsTable.setWidths(new float[]{45, 15, 20, 20});
        itemsTable.setSpacingBefore(8);

        String[] cols = {"Item", "Qty", "Unit Price", "Total"};
        for (String col : cols) {
            PdfPCell c = new PdfPCell(new Phrase(col, tblHead));
            c.setBackgroundColor(dark);
            c.setPadding(10);
            c.setBorder(Rectangle.NO_BORDER);
            itemsTable.addCell(c);
        }

        if (order != null) {
            for (int i = 0; i < order.getItemIds().size(); i++) {
                String name  = i < order.getItemNames().size()  ? order.getItemNames().get(i)  : "Item";
                int    qty   = i < order.getQuantities().size() ? order.getQuantities().get(i)  : 1;
                double price = i < order.getItemPrices().size() ? order.getItemPrices().get(i)  : 0;
                Color bg = (i % 2 == 0) ? Color.WHITE : light;

                PdfPCell n = new PdfPCell(new Phrase(name, normalFont));
                n.setBackgroundColor(bg); n.setPadding(9); n.setBorder(Rectangle.BOTTOM); n.setBorderColor(new Color(229,231,235));
                itemsTable.addCell(n);
                PdfPCell q = new PdfPCell(new Phrase(String.valueOf(qty), normalFont));
                q.setBackgroundColor(bg); q.setPadding(9); q.setBorder(Rectangle.BOTTOM); q.setBorderColor(new Color(229,231,235)); q.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemsTable.addCell(q);
                PdfPCell p = new PdfPCell(new Phrase("LKR " + String.format("%.0f", price), normalFont));
                p.setBackgroundColor(bg); p.setPadding(9); p.setBorder(Rectangle.BOTTOM); p.setBorderColor(new Color(229,231,235)); p.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemsTable.addCell(p);
                PdfPCell t = new PdfPCell(new Phrase("LKR " + String.format("%.0f", price * qty), normalFont));
                t.setBackgroundColor(bg); t.setPadding(9); t.setBorder(Rectangle.BOTTOM); t.setBorderColor(new Color(229,231,235)); t.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemsTable.addCell(t);
            }
        }
        doc.add(itemsTable);

        // ── Totals ──
        PdfPTable totals = new PdfPTable(2);
        totals.setWidthPercentage(45);
        totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totals.setSpacingBefore(6);

        java.util.function.BiConsumer<String,String> addRow = (label, val) -> {
            PdfPCell lc = new PdfPCell(new Phrase(label, mutedFont));
            lc.setBorder(Rectangle.NO_BORDER); lc.setPadding(5); lc.setHorizontalAlignment(Element.ALIGN_LEFT);
            totals.addCell(lc);
            PdfPCell vc = new PdfPCell(new Phrase(val, normalFont));
            vc.setBorder(Rectangle.NO_BORDER); vc.setPadding(5); vc.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.addCell(vc);
        };

        if (order != null) {
            double subtotal = order.getTotalAmount() + order.getDiscountAmount() - order.getDeliveryFee();
            addRow.accept("Subtotal", "LKR " + String.format("%.0f", subtotal));
            if (order.getDiscountAmount() > 0)
                addRow.accept("Discount", "- LKR " + String.format("%.0f", order.getDiscountAmount()));
            addRow.accept("Delivery Fee", "LKR " + String.format("%.0f", order.getDeliveryFee()));
        }

        // Total row with colored background
        PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL PAID", whiteFont));
        totalLabel.setBackgroundColor(orange); totalLabel.setBorder(Rectangle.NO_BORDER); totalLabel.setPadding(8);
        totals.addCell(totalLabel);
        PdfPCell totalVal = new PdfPCell(new Phrase("LKR " + String.format("%.0f", payment.getAmount()), whiteFont));
        totalVal.setBackgroundColor(orange); totalVal.setBorder(Rectangle.NO_BORDER); totalVal.setPadding(8); totalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totals.addCell(totalVal);

        doc.add(totals);

        // ── Paid stamp ──
        doc.add(Chunk.NEWLINE);
        Paragraph paid = new Paragraph("✓  PAID IN FULL", greenFont);
        paid.setAlignment(Element.ALIGN_CENTER);
        doc.add(paid);

        // ── Footer ──
        doc.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph("Thank you for ordering with FoodHub! | SE1020 OOP Project | foodhub.lk", mutedFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);

        doc.close();
    }
}
