package com.project.dynamicformbuilderbackend.dtos;

import java.util.List;

public record FormRequestDto(String title, List<InputSchema> schemas) {
}
