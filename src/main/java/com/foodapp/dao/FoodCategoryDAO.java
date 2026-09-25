package com.foodapp.dao;

import com.foodapp.entity.FoodCategory;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FoodCategoryDAO extends GenericDAO<FoodCategory> {
    public FoodCategoryDAO() { super(FoodCategory.class); }
}