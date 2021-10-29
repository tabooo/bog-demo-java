package com.bog.demo.repository.sell;

import com.bog.demo.domain.product.Sell;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellRepository extends CrudRepository<Sell, Integer> {
}