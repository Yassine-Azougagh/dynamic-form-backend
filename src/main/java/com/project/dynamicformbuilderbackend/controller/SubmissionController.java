package com.project.dynamicformbuilderbackend.controller;


import com.project.dynamicformbuilderbackend.dtos.BaseResponse;
import com.project.dynamicformbuilderbackend.dtos.FormDto;
import com.project.dynamicformbuilderbackend.dtos.SubmissionDto;
import com.project.dynamicformbuilderbackend.dtos.SubmissionRequestDto;
import com.project.dynamicformbuilderbackend.enums.FormStatus;
import com.project.dynamicformbuilderbackend.enums.SubmissionStatus;
import com.project.dynamicformbuilderbackend.service.FormService;
import com.project.dynamicformbuilderbackend.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/submission")
@Tag(name = "Submission Controller", description = "Submission managment api")
public class SubmissionController {
    private final Logger log = LoggerFactory.getLogger(SubmissionController.class);
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping()
    @Operation(summary = "Create Form Submission", description = "Create a new Form Submission")
    public ResponseEntity<BaseResponse> createSubmission(@RequestBody SubmissionRequestDto submissionRequestDto, Principal principal) {
        log.info("Create form Submission by user : {}", principal.getName());
        return ResponseEntity.ok(submissionService.createSubmission(submissionRequestDto, principal.getName()));
    }

    @GetMapping("/list")
    @Operation(summary = "Get List Form Submission", description = "Get List Form Submission")
    public ResponseEntity<List<SubmissionDto>> getListSubmission(@RequestParam String createdBy, Principal principal) {
        log.info("Get list form Submission by user : {}", principal.getName());
        return ResponseEntity.ok(submissionService.getListSubmission(createdBy));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Form Submission by id", description = "Get Form Submission by id")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable String id, Principal principal) {
        log.info("Get list form Submission by user : {}", principal.getName());
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @PostMapping("/submit/{id}")
    @Operation(summary = "Submit Submission by id", description = "Submit Submission by id")
    public ResponseEntity<BaseResponse> submitSubmission(@PathVariable String id, Principal principal) {
        log.info("Submit Submission by user : {}", principal.getName());
        return ResponseEntity.ok(submissionService.changeStatus(id, SubmissionStatus.SUBMITTED));
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "delete Submission by id", description = "delete Submission by id")
    public ResponseEntity<BaseResponse> deleteSubmission(@PathVariable String id, Principal principal) {
        log.info("delete Submission by user : {}", principal.getName());
        return ResponseEntity.ok(submissionService.changeStatus(id, SubmissionStatus.DELETED));
    }

    @PostMapping("/validate/{id}")
    @Operation(summary = "validate Submission by id", description = "validate Submission by id")
    public ResponseEntity<BaseResponse> validateSubmission(@PathVariable String id, Principal principal) {
        log.info("validate Submission by user : {}", principal.getName());
        return ResponseEntity.ok(submissionService.changeStatus(id, SubmissionStatus.VALIDATED));
    }
}
