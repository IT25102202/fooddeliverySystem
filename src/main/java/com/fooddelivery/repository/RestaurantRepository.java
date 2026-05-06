package com.fooddelivery.repository;

import com.fooddelivery.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    // READ - Search by name, cuisine, city, description
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%',:term,'%')) OR LOWER(r.cuisine) LIKE LOWER(CONCAT('%',:term,'%')) OR LOWER(r.city) LIKE LOWER(CONCAT('%',:term,'%'))")
    List<Restaurant> searchRestaurants(@Param("term") String term);

    // READ - Filter by type (VEG / NONVEG / BOTH)
    List<Restaurant> findByType(String type);

    // READ - Filter by city
    List<Restaurant> findByCity(String city);

    // READ - Open restaurants only
    List<Restaurant> findByIsOpen(boolean isOpen);

    // READ - Top rated (order by rating descending)
    List<Restaurant> findTop6ByOrderByRatingDesc();
}
