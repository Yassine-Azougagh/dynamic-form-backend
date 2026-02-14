package com.project.dynamicformbuilderbackend.entities;


import com.project.dynamicformbuilderbackend.enums.FormStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "FORM")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Form extends BaseEntity{
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Lob    @Basic(fetch = FetchType.LAZY)
    @Column(name = "SCHEMA")
    private String schemaJson;
    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY)
    private List<Submission> submissions;
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormStatus status = FormStatus.DRAFT;
}
