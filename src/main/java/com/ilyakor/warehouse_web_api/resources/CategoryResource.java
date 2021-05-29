package com.ilyakor.warehouse_web_api.resources;

import com.ilyakor.warehouse_web_api.entities.Category;
import com.ilyakor.warehouse_web_api.entities.Product;
import com.ilyakor.warehouse_web_api.repositories.CategoryRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("categories")
    public Category addCategory(@RequestBody Category category) {
        repository.save(category);
        return category;
    }

    @PutMapping("categories/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable int id){
       Category category1 =  repository.findById(id).orElse(null);
       if(category1!=null){
           category1.copyInfo(category);
           repository.save(category1);
           return  category1;
       }
       else{
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category doesn't exists");
       }
    }
    @DeleteMapping("categories/{id}")
    public void deleteCategory(@PathVariable int id){
        Category category =  repository.findById(id).orElse(null);
        if(category!=null){
            repository.deleteById(id);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category doesn't exists");
        }
    }

}
