package com.fooddelivery.service;

import com.fooddelivery.model.FoodItem;
import com.fooddelivery.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * FoodItemService - Component 3: IT25100897
 */
@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    // CREATE - Add new food item
    public boolean addFoodItem(FoodItem item) {
        if (item.getId() == null || item.getId().isEmpty())
            item.setId("FD" + System.currentTimeMillis());
        foodItemRepository.save(item);
        return true;
    }

    // READ - All food items
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    // READ - Items by restaurant
    public List<FoodItem> getFoodItemsByRestaurant(String restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId);
    }

    // READ - Items by restaurant and category
    public List<FoodItem> getFoodItemsByCategory(String restaurantId, String category) {
        return foodItemRepository.findByRestaurantIdAndCategory(restaurantId, category);
    }

    // READ - Find by ID
    public FoodItem findById(String id) {
        return foodItemRepository.findById(id).orElse(null);
    }

    // READ - Search
    public List<FoodItem> searchFoodItems(String term) {
        return foodItemRepository.searchFoodItems(term);
    }

    // UPDATE - Update food item
    public boolean updateFoodItem(FoodItem item) {
        if (!foodItemRepository.existsById(item.getId())) return false;
        foodItemRepository.save(item);
        return true;
    }

    // DELETE - Remove food item
    public boolean deleteFoodItem(String id) {
        if (!foodItemRepository.existsById(id)) return false;
        foodItemRepository.deleteById(id);
        return true;
    }

    // DELETE - Remove all items for a restaurant
    @Transactional
    public void deleteByRestaurant(String restaurantId) {
        foodItemRepository.deleteByRestaurantId(restaurantId);
    }

    public int countFoodItems() { return (int) foodItemRepository.count(); }

    private com.fooddelivery.model.MainCourse mc(String id, String name, String desc,
            double price, String rId, boolean avail, boolean veg, String img, int prep) {
        com.fooddelivery.model.MainCourse m = new com.fooddelivery.model.MainCourse();
        m.setId(id); m.setName(name); m.setDescription(desc); m.setPrice(price);
        m.setRestaurantId(rId); m.setAvailable(avail); m.setVeg(veg);
        m.setImageUrl(img); m.setPrepTime(prep);
        return m;
    }

    private com.fooddelivery.model.Dessert ds(String id, String name, String desc,
            double price, String rId, boolean avail, String img, int prep) {
        com.fooddelivery.model.Dessert d = new com.fooddelivery.model.Dessert();
        d.setId(id); d.setName(name); d.setDescription(desc); d.setPrice(price);
        d.setRestaurantId(rId); d.setAvailable(avail); d.setVeg(true);
        d.setImageUrl(img); d.setPrepTime(prep);
        return d;
    }

    private com.fooddelivery.model.Beverage bv(String id, String name, String desc,
            double price, String rId, boolean avail, String img, int prep) {
        com.fooddelivery.model.Beverage b = new com.fooddelivery.model.Beverage();
        b.setId(id); b.setName(name); b.setDescription(desc); b.setPrice(price);
        b.setRestaurantId(rId); b.setAvailable(avail); b.setVeg(true);
        b.setImageUrl(img); b.setPrepTime(prep);
        return b;
    }

    public void initSampleData() {
        if (foodItemRepository.count() == 0) {
            foodItemRepository.save(mc("FD001","Dhal Curry","Rich lentil curry with spices",450.0,"RST001",true,true,"https://images.unsplash.com/photo-1546549032-9571cd6b27df?w=400",20));
            foodItemRepository.save(mc("FD002","Vegetable Kottu","Stir-fried roti with vegetables",550.0,"RST001",true,true,"https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400",25));
            foodItemRepository.save(bv("FD003","Mango Lassi","Refreshing mango yogurt drink",280.0,"RST001",true,"https://images.unsplash.com/photo-1553361371-9b22f78e8b1d?w=400",5));
            foodItemRepository.save(ds("FD004","Watalappan","Traditional Sri Lankan coconut custard",320.0,"RST001",true,"https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400",10));
            foodItemRepository.save(mc("FD005","Classic Beef Burger","Juicy beef patty with fresh veggies",1200.0,"RST002",true,false,"https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400",15));
            foodItemRepository.save(mc("FD006","Crispy Chicken Burger","Crispy fried chicken with coleslaw",1050.0,"RST002",true,false,"https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=400",15));
            foodItemRepository.save(bv("FD007","Chocolate Milkshake","Rich creamy chocolate shake",650.0,"RST002",true,"https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400",5));
            foodItemRepository.save(ds("FD008","Brownie Sundae","Warm brownie with vanilla ice cream",750.0,"RST002",true,"https://images.unsplash.com/photo-1589375924258-9a0fce70cfa0?w=400",10));
            foodItemRepository.save(mc("FD009","Margherita Pizza","Classic tomato, mozzarella and basil",1450.0,"RST003",true,true,"https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400",20));
            foodItemRepository.save(mc("FD010","Pepperoni Pizza","Loaded with premium pepperoni",1750.0,"RST003",true,false,"https://images.unsplash.com/photo-1628840042765-356cda07504e?w=400",20));
            foodItemRepository.save(ds("FD011","Tiramisu","Classic Italian coffee dessert",850.0,"RST003",true,"https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400",5));
            foodItemRepository.save(bv("FD012","Sparkling Water","Chilled sparkling mineral water",200.0,"RST003",true,"https://images.unsplash.com/photo-1559839697-399c3da38afa?w=400",2));
            foodItemRepository.save(mc("FD013","Salmon Sushi Set","8 pieces of fresh salmon sushi",2200.0,"RST004",true,false,"https://images.unsplash.com/photo-1553621042-f6e147245754?w=400",15));
            foodItemRepository.save(mc("FD014","Vegetarian Maki Roll","Cucumber and avocado maki",1200.0,"RST004",true,true,"https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=400",10));
            foodItemRepository.save(mc("FD017","Chicken Kottu","Classic chicken kottu roti",850.0,"RST005",true,false,"https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400",20));
            foodItemRepository.save(mc("FD018","Egg Kottu","Kottu with scrambled eggs",750.0,"RST005",true,true,"https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=400",20));
            foodItemRepository.save(bv("FD019","King Coconut Juice","Fresh chilled king coconut",180.0,"RST005",true,"https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?w=400",2));
        }
    }
}
