package com.project.dynamicformbuilderbackend.dtos;

import java.util.List;

public record SubmissionRequestDto(String formId, List<FieldSubmissionDto> fields) {
}
