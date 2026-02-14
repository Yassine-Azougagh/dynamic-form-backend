package com.project.dynamicformbuilderbackend.controller;


import com.project.dynamicformbuilderbackend.dtos.BaseResponse;
import com.project.dynamicformbuilderbackend.dtos.FormDto;
import com.project.dynamicformbuilderbackend.dtos.FormRequestDto;
import com.project.dynamicformbuilderbackend.dtos.SignupRequestDto;
import com.project.dynamicformbuilderbackend.entities.Form;
import com.project.dynamicformbuilderbackend.service.AuthService;
import com.project.dynamicformbuilderbackend.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/form")
@Tag(name = "Form Controller", description = "Form managment api")
public class FormController {
    private final Logger log = LoggerFactory.getLogger(FormController.class);
    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping()
    @Operation(summary = "Create Form", description = "Create a new Form")
    public ResponseEntity<BaseResponse> createForm(@RequestBody FormRequestDto formRequestDto, Principal principal) {
        log.info("Create form by user : {}", principal.getName());
        return ResponseEntity.ok(formService.createForm(formRequestDto, principal.getName()));
    }

    @GetMapping("/list")
    @Operation(summary = "Get List Form", description = "Get List Form")
    public ResponseEntity<List<FormDto>> getListForm(Principal principal) {
        log.info("Get list form by user : {}", principal.getName());
        return ResponseEntity.ok(formService.getListForm());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Form by id", description = "Get Form by id")
    public ResponseEntity<FormDto> getFormById(@PathVariable String id, Principal principal) {
        log.info("Get list form by user : {}", principal.getName());
        return ResponseEntity.ok(formService.getFormById(id));
    }
}
