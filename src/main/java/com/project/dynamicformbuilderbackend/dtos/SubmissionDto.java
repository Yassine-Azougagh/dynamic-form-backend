package com.project.dynamicformbuilderbackend.dtos;

import com.project.dynamicformbuilderbackend.enums.FormStatus;
import com.project.dynamicformbuilderbackend.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SubmissionDto {
    String id;
    String formId;
    String createdBy;
    String createdAt;
    int version;
    SubmissionStatus status;
    String submittedBy;
    List<FieldSubmissionDto> schema;
}
