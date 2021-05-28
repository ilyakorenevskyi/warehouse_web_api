package com.ilyakor.warehouse_web_api.resources;

import com.ilyakor.warehouse_web_api.entities.Product;
import com.ilyakor.warehouse_web_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductResource {

    @Autowired
    ProductRepository repository;

    @PreAuthorize("permitAll()")
    @GetMapping("products")
    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        products = (List<Product>) repository.findAll();
        return products;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable int id){
       Product product = new Product();
       product = repository.findById(id).orElse(null);
       return product;
    }


    @PreAuthorize("permitAll()")
    @GetMapping("category/{id}/products")
    public List<Product> getProducts(@PathVariable int id){
        List<Product> products = new ArrayList<>();
        products = (List<Product>) repository.findProductsByCategory(id);
        return products;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERVISOR')")
    @RequestMapping(value = "/products", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Product addProduct(@RequestBody(required=true)  Product product){
       System.out.println(product.toString());
        if(repository.existsById(product.getId())){
            return null;
        }
        else {
            repository.save(product);
        }
        return product;
   }

   @PreAuthorize("hasAnyAuthority('ADMIN','SUPERVISOR')")
   @PutMapping("products/{id}")
   public Product changeProduct(@RequestBody(required=true)  Product product,@PathVariable int id){
       repository.save(product);
       return product;
   }

   @PreAuthorize("hasAnyAuthority('ADMIN','SUPERVISOR')")
   @PutMapping("products/{id}/{quantity}")
   public Product addQuantity(@PathVariable(required=true) int id, @PathVariable(required=true) int quantity){
       Product product  = repository.findById(id).orElse(null);
       if(product == null){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product doesn't exists");
       }
       else{
           int new_quantity =  product.getQuantity() + quantity;
           if(new_quantity<0){
               throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Quantity is out of boundaries");
           }
           else{
               product.setQuantity(new_quantity);
               repository.save(product);
               return product;
           }
       }
   }

   @PreAuthorize("hasAnyAuthority('ADMIN','SUPERVISOR')")
   @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable int id){
        repository.deleteById(id);
   }

}
