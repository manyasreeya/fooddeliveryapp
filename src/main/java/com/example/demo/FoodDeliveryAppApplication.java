package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "com.example.model")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "com.example.repository")
public class FoodDeliveryAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryAppApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner loadData(com.example.repository.FoodRepository foodRepo) {
        return (args) -> {
            if (foodRepo.count() == 0) {
                com.example.model.FoodItem item1 = new com.example.model.FoodItem();
                item1.setName("Artisan Margherita Pizza");
                item1.setPrice(399.0);
                item1.setImageUrl("https://images.unsplash.com/photo-1574071318508-1cdbab80d002?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");
                
                com.example.model.FoodItem item2 = new com.example.model.FoodItem();
                item2.setName("Truffle Mushroom Burger");
                item2.setPrice(249.0);
                item2.setImageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");
                
                com.example.model.FoodItem item3 = new com.example.model.FoodItem();
                item3.setName("Classic Penne Arrabbiata");
                item3.setPrice(299.0);
                item3.setImageUrl("https://images.unsplash.com/photo-1621996311239-5aca724578b6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");
                
                com.example.model.FoodItem item4 = new com.example.model.FoodItem();
                item4.setName("Spicy Salmon Sushi Roll");
                item4.setPrice(499.0);
                item4.setImageUrl("https://images.unsplash.com/photo-1579871494447-9811cf80d66c?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");
                
                foodRepo.save(item1);
                foodRepo.save(item2);
                foodRepo.save(item3);
                foodRepo.save(item4);
            }
        };
    }
}