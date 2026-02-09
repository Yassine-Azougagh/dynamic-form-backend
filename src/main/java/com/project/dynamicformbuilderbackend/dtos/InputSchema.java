package com.project.dynamicformbuilderbackend.dtos;

import java.util.List;

public record InputSchema(String type, String title, String placeholder, InputConditions conditions,
                          List<String> options) {
}
