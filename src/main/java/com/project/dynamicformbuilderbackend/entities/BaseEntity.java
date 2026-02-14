package com.project.dynamicformbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "CREATED_AT")
    private LocalDate createdAt;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "VERSION")
    @Version
    private Integer version;
}
