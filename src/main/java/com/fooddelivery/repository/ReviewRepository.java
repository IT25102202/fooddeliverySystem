package com.fooddelivery.repository;

import com.fooddelivery.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ReviewRepository - Database operations for Review
 * Component 6: IT25100159
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    // READ - Active reviews for a restaurant
    List<Review> findByRestaurantIdAndStatusOrderByReviewDateTimeDesc(String restaurantId, String status);

    // READ - All reviews by a customer
    List<Review> findByCustomerIdOrderByReviewDateTimeDesc(String customerId);

    // READ - All reviews newest first
    List<Review> findAllByOrderByReviewDateTimeDesc();

    // READ - Average rating for a restaurant
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurantId = :restaurantId AND r.status = 'ACTIVE'")
    Double getAverageRating(@Param("restaurantId") String restaurantId);
}
