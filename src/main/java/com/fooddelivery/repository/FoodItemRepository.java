package com.fooddelivery.repository;

import com.fooddelivery.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * FoodItemRepository - Database operations for FoodItem
 * Component 3: IT25100897
 */
@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, String> {

    // READ - All items for a restaurant
    List<FoodItem> findByRestaurantId(String restaurantId);

    // READ - Items by restaurant and category
    List<FoodItem> findByRestaurantIdAndCategory(String restaurantId, String category);

    // READ - Search by name or description
    @Query("SELECT f FROM FoodItem f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:term,'%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%',:term,'%'))")
    List<FoodItem> searchFoodItems(@Param("term") String term);

    // DELETE - Remove all food items for a restaurant
    void deleteByRestaurantId(String restaurantId);
}
