package com.project.dynamicformbuilderbackend.dtos;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BaseResponse {
    private boolean success;
    private String message;
    private String code;


}
