package com.project.dynamicformbuilderbackend.dtos;

import lombok.With;

import java.util.List;

@With
public record InputSchema(String id,
                          String type,
                          String title,
                          String placeholder,
                          InputConditions conditions,
                          List<InputOption> options) {
}
