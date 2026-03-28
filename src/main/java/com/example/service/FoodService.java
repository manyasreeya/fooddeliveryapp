package com.example.service;

import com.example.model.FoodItem;
import com.example.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<FoodItem> getAllFoodItems() {
        return foodRepository.findAll();
    }

    public void seedDatabaseIfEmpty() {
        if (foodRepository.count() < 15) {
            foodRepository.deleteAll(); // Clear old small menu
            
            // Indian
            foodRepository.save(createItem("Butter Chicken with Garlic Naan", 350.0, "https://images.unsplash.com/photo-1603894584373-5ac82bea3d82?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Paneer Tikka Masala", 290.0, "https://images.unsplash.com/photo-1565557623262-b51c2513a641?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Hyderabadi Chicken Biryani", 320.0, "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            
            // Italian
            foodRepository.save(createItem("Artisan Margherita Pizza", 399.0, "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Classic Penne Arrabbiata", 299.0, "https://images.unsplash.com/photo-1621996311239-5aca724578b6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Creamy Truffle Mushroom Pasta", 350.0, "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            
            // Burgers & Fast Food
            foodRepository.save(createItem("Truffle Mushroom Burger", 249.0, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Double Cheeseburger Combo", 399.0, "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Crispy Onion Rings", 129.0, "https://images.unsplash.com/photo-1639024471283-03518883512d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            
            // Asian
            foodRepository.save(createItem("Spicy Salmon Sushi Roll", 499.0, "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Kung Pao Chicken", 320.0, "https://images.unsplash.com/photo-1525755662778-989d0524087e?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Vegetable Manchurian", 220.0, "https://images.unsplash.com/photo-1585937421612-70a008356fbe?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));

            // Desserts & Drinks
            foodRepository.save(createItem("Hazelnut Chocolate Lava Cake", 199.0, "https://images.unsplash.com/photo-1624353365286-3f8d62daad51?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Red Velvet Cupcake", 149.0, "https://images.unsplash.com/photo-1614707267537-b85aaf00c4b7?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Fresh Mango Smoothie", 149.0, "https://images.unsplash.com/photo-1625515252875-10118fb485bb?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
            foodRepository.save(createItem("Signature Iced Caramel Latte", 189.0, "https://images.unsplash.com/photo-1572442388796-11668a67e53d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"));
        }
    }

    private FoodItem createItem(String name, double price, String imageUrl) {
        FoodItem item = new FoodItem();
        item.setName(name);
        item.setPrice(price);
        item.setImageUrl(imageUrl);
        return item;
    }
}
