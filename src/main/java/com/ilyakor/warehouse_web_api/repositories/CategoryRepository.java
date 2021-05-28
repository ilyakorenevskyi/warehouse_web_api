package com.ilyakor.warehouse_web_api.repositories;

import com.ilyakor.warehouse_web_api.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    public List<Category> findByParentId(int parentId);
}
