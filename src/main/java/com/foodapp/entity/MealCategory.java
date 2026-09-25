package com.foodapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "meal_categories")
public class MealCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_category_id")
    private Long mealCategoryId;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // Getters and Setters
    public Long getMealCategoryId() { return mealCategoryId; }
    public void setMealCategoryId(Long mealCategoryId) { this.mealCategoryId = mealCategoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}