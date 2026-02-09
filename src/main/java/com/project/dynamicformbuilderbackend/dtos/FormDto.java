package com.project.dynamicformbuilderbackend.dtos;

import com.project.dynamicformbuilderbackend.enums.FormStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FormDto {
    String id;
    String title;
    String createdBy;
    int version;
    FormStatus status;
    List<InputSchema> schema;
}
