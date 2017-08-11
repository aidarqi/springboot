package com.aidar.repository;


import com.aidar.domain.Product;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by upsmart on 17-6-5.
 */
@Repository
@CacheConfig(cacheNames = "products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
     List<Product> findAll();

    @Cacheable(key = "#p0")
    Product findByPId(int pId);

    @Cacheable(key = "#p0.pName")
    List<Product> findByPNameLike(String pName);

    @CachePut(key = "#p0.pName")
    Product save(Product product);
}
