package com.example.eSmartRecruit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "Applications")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Applications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "candidateid")
    private Integer candidateId;
    @Column(name = "positionid")
    private Integer positionId;
    @Column(name = "status")
    private String status;
    @Column(name = "cv")
    private String CV;
    @Column(name = "applicationdate")
    private Date applicationDate;

    public Applications(Integer candidateId, Integer positionId, String status, String CV, Date applicationDate) {
        this.candidateId = candidateId;
        this.positionId = positionId;
        this.status = status;
        this.CV = CV;
        this.applicationDate = applicationDate;
    }
}
