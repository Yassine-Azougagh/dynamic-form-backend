package com.project.dynamicformbuilderbackend.controller;


import com.project.dynamicformbuilderbackend.dtos.*;
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

    @GetMapping("/admin/list")
    @Operation(summary = "Get List Form", description = "Get List Form")
    public ResponseEntity<FormListDto> getAdminListForm(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy, Principal principal) {
        log.info("Get list form by user : {}", principal.getName());
        return ResponseEntity.ok(formService.getAdminListForm(page, size, sortBy, principal.getName()));
    }

    @GetMapping("/user/list")
    @Operation(summary = "Get List Form", description = "Get List Form")
    public ResponseEntity<FormListDto> getUserListForm(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy, Principal principal) {
        log.info("Get list form by user : {}", principal.getName());
        return ResponseEntity.ok(formService.getUserListForm(page, size, sortBy, principal.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Form by id", description = "Get Form by id")
    public ResponseEntity<FormDto> getFormById(@PathVariable String id, Principal principal) {
        log.info("Get list form by user : {}", principal.getName());
        return ResponseEntity.ok(formService.getFormById(id));
    }
}
