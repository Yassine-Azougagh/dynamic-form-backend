package com.project.dynamicformbuilderbackend.entities;

import com.project.dynamicformbuilderbackend.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "SUBMISSION")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Submission extends BaseEntity{


    @ManyToOne(optional = false)
    private Form form;

    @ManyToOne(optional = false)
    private User submittedBy;

    @Lob    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String answersJson;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.DRAFT;
}
