package com.project.dynamicformbuilderbackend.dtos;

import java.util.List;

public record FormListDto(List<FormDto> list, int page, int size, long fullSize, String sortBy) {
}
