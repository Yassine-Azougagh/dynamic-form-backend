package com.project.dynamicformbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "FORM_RESPONSE")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FormResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    private Form form;

    @ManyToOne(optional = false)
    private User submittedBy;

    @Lob    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String answersJson;

    @Column(nullable = false)
    private LocalDateTime submittedAt;
}
