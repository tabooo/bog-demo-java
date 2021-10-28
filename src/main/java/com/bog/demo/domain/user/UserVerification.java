package com.bog.demo.domain.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_verifications")
public class UserVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "state")
    private Integer state;
}