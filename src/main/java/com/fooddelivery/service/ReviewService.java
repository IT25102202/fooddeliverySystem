package com.fooddelivery.service;

import com.fooddelivery.model.Review;
import com.fooddelivery.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * ReviewService - Component 6: IT25100159
 */
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // CREATE - Add review
    public boolean addReview(Review review) {
        review.setId("REV" + System.currentTimeMillis());
        review.setStatus("ACTIVE");
        reviewRepository.save(review);
        return true;
    }

    // READ - Active reviews for a restaurant
    public List<Review> getReviewsByRestaurant(String restaurantId) {
        return reviewRepository.findByRestaurantIdAndStatusOrderByReviewDateTimeDesc(restaurantId, "ACTIVE");
    }

    // READ - All reviews by customer
    public List<Review> getReviewsByCustomer(String customerId) {
        return reviewRepository.findByCustomerIdOrderByReviewDateTimeDesc(customerId);
    }

    // READ - All reviews (admin)
    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByReviewDateTimeDesc();
    }

    // READ - Find by ID
    public Review findById(String id) {
        return reviewRepository.findById(id).orElse(null);
    }

    // UPDATE - Edit review
    public boolean updateReview(String id, int rating, String comment) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return false;
        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.save(review);
        return true;
    }

    // DELETE - Permanently delete review
    public boolean deleteReview(String id) {
        if (!reviewRepository.existsById(id)) return false;
        reviewRepository.deleteById(id);
        return true;
    }

    // Admin - Hide review (soft delete)
    public boolean hideReview(String id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return false;
        review.setStatus("HIDDEN");
        reviewRepository.save(review);
        return true;
    }

    // Admin - Restore hidden review
    public boolean restoreReview(String id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return false;
        review.setStatus("ACTIVE");
        reviewRepository.save(review);
        return true;
    }

    // Average rating for a restaurant
    public double getAverageRating(String restaurantId) {
        Double avg = reviewRepository.getAverageRating(restaurantId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public int countReviews() { return (int) reviewRepository.count(); }

    public void initSampleData() {
        if (reviewRepository.count() == 0) {
            reviewRepository.save(new Review("REV001","USR001","Kasun Perera","RST003","Pizza Paradise","ORD001",5,"Absolutely delicious! The Margherita was perfect."));
            reviewRepository.save(new Review("REV002","USR002","Nimasha Silva","RST002","Burger Palace","ORD001",4,"Great burgers, fast delivery. Will order again!"));
            reviewRepository.save(new Review("REV003","USR003","Amal Fernando","RST001","The Curry House","ORD001",5,"Best Sri Lankan food in Colombo. Highly recommend!"));
        }
    }
}
