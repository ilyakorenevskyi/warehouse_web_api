package com.ilyakor.warehouse_web_api.resources;

import com.ilyakor.warehouse_web_api.entities.Category;
import com.ilyakor.warehouse_web_api.entities.Product;
import com.ilyakor.warehouse_web_api.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryResource {

    @Autowired
    CategoryRepository repository;

    @GetMapping("categories")
    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();
        categories = (List<Category>) repository.findAll();
        return categories;
    }

    @GetMapping("categories/{id}")
    public Category getCategory(@PathVariable int id){
        Category category = new Category();
        category = repository.findById(id).orElse(null);
        return category;
    }

}
