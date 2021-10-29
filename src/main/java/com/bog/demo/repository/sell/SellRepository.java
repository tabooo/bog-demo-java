package com.bog.demo.repository.sell;

import com.bog.demo.domain.product.Sell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SellRepository extends CrudRepository<Sell, Integer> {
    @Query("select s from Sell s " +
            "where s.createDate between :min and :max " +
            "and s.state=1")
    List<Sell> getSellsBetweenDates(Date min, Date max);
}