package com.example.eSmartRecruit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
//import java.util.Date;
import java.sql.Date;

@Entity
@Table(name = "Positions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Positions implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "Title")
    private String title;
    @Column(name = "jobdescription")
    private String discription;
    @Column(name = "jobrequirements")
    private String requirements;
    @Column(name = "Salary")
    private Integer salary;
    @Column(name = "postdate")
    private Date post;
    @Column(name = "expiredate")
    private Date expire;
    @Column(name = "Location")
    private String location;
}
