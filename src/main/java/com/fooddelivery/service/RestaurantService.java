package com.fooddelivery.service;

import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // CREATE - Add new restaurant
    public boolean addRestaurant(Restaurant restaurant) {
        if (restaurant.getId() == null || restaurant.getId().isEmpty())
            restaurant.setId("RST" + System.currentTimeMillis());
        restaurantRepository.save(restaurant);
        return true;
    }

    // READ - Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // READ - Open only
    public List<Restaurant> getOpenRestaurants() {
        return restaurantRepository.findByIsOpen(true);
    }

    // READ - Find by ID
    public Restaurant findById(String id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    // READ - Search
    public List<Restaurant> searchRestaurants(String term) {
        return restaurantRepository.searchRestaurants(term);
    }

    // READ - Filter by type
    public List<Restaurant> filterByType(String type) {
        return restaurantRepository.findByType(type);
    }

    // READ - Filter by city
    public List<Restaurant> filterByCity(String city) {
        return restaurantRepository.findByCity(city);
    }

    // READ - Top rated (for home page)
    public List<Restaurant> getTopRated(int limit) {
        return restaurantRepository.findTop6ByOrderByRatingDesc();
    }

    // UPDATE - Update restaurant details
    public boolean updateRestaurant(Restaurant restaurant) {
        if (!restaurantRepository.existsById(restaurant.getId())) return false;
        restaurantRepository.save(restaurant);
        return true;
    }

    // DELETE - Remove restaurant
    public boolean deleteRestaurant(String id) {
        if (!restaurantRepository.existsById(id)) return false;
        restaurantRepository.deleteById(id);
        return true;
    }

    public int countRestaurants() { return (int) restaurantRepository.count(); }

    public void initSampleData() {
        if (restaurantRepository.count() == 0) {
            restaurantRepository.save(new Restaurant("RST001","The Curry House","45 Galle Road","Colombo","0112223344","Sri Lankan","VEG",4.5,true,"https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=600","Authentic Sri Lankan vegetarian cuisine","08:00","22:00",150.0));
            Restaurant rst002 = new Restaurant("RST002","Burger Palace","78 Kandy Road","Colombo","0112334455","American","NONVEG",4.2,true,"https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600","Premium burgers and fast food","10:00","23:00",200.0);
            rst002.setDiscountPercent(15); rst002.setDiscountLabel("Online Exclusive");
            restaurantRepository.save(rst002);
            Restaurant rst003 = new Restaurant("RST003","Pizza Paradise","12 Marine Drive","Colombo","0112445566","Italian","BOTH",4.7,true,"https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600","Wood-fired authentic Italian pizzas","11:00","23:30",175.0);
            rst003.setDiscountPercent(20); rst003.setDiscountLabel("Weekend Special");
            restaurantRepository.save(rst003);
            restaurantRepository.save(new Restaurant("RST004","Sushi Garden","34 Union Place","Colombo","0112556677","Japanese","BOTH",4.8,true,"https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=600","Freshest sushi and Japanese cuisine","12:00","22:00",250.0));
            Restaurant rst005 = new Restaurant("RST005","Kottu Kingdom","56 High Street","Kandy","0812233445","Sri Lankan","BOTH",4.3,true,"https://images.unsplash.com/photo-1552611052-33e04de081de?w=600","Best kottu roti in town","07:00","21:00",100.0);
            rst005.setDiscountPercent(10); rst005.setDiscountLabel("App Deal");
            restaurantRepository.save(rst005);
            restaurantRepository.save(new Restaurant("RST006","Chinese Dragon","89 Main Street","Galle","0912344556","Chinese","BOTH",4.1,true,"https://images.unsplash.com/photo-1563245372-f21724e3856d?w=600","Authentic Chinese dim sum and noodles","10:00","22:30",180.0));
        }
    }
}
