package com.example.repository;

import com.example.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodItem, Long> {
}