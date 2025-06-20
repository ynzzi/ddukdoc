package com.example.ddukdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="d_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

}
