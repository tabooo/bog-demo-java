package com.bog.demo.domain.product;

import com.bog.demo.domain.file.File;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sells")
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "one_price")
    private Double onePrice;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "card_info")
    private String cardInfo;

    @Column(name = "state")
    private Integer state;

    @Column(name = "buyer_user_id")
    private Integer buyerUserId;

    @Column(name = "seller_user_id")
    private Integer sellerUserId;

    @Column(name = "create_date")
    private Date createDate;
}