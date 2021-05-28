package com.ilyakor.warehouse_web_api.repositories;


import com.ilyakor.warehouse_web_api.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    public List<Product> findProductsByCategory(int category);
}
