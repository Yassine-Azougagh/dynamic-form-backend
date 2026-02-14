package com.project.dynamicformbuilderbackend.service;


import com.project.dynamicformbuilderbackend.dtos.*;
import com.project.dynamicformbuilderbackend.entities.Form;
import com.project.dynamicformbuilderbackend.entities.Submission;
import com.project.dynamicformbuilderbackend.entities.User;
import com.project.dynamicformbuilderbackend.enums.FormStatus;
import com.project.dynamicformbuilderbackend.enums.SubmissionStatus;
import com.project.dynamicformbuilderbackend.repository.FormRepository;
import com.project.dynamicformbuilderbackend.repository.SubmissionRepository;
import com.project.dynamicformbuilderbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmissionService {
    private final Logger log = LoggerFactory.getLogger(SubmissionService.class);
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final FormRepository formRepository;
    private final SubmissionRepository submissionRepository;

    public SubmissionService(UserRepository userRepository,
                             ObjectMapper objectMapper,
                             FormRepository formRepository,
                             SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.formRepository = formRepository;
        this.submissionRepository = submissionRepository;
    }


    public BaseResponse createSubmission(SubmissionRequestDto submissionRequestDto, String userId) {

        Submission submission = new Submission();
        submission.setStatus(SubmissionStatus.DRAFT);
        User user = userRepository.findByUsername(userId);
        log.info("Creating form by user : {}", user);

        submission.setCreatedBy(user.getUsername());
        submission.setCreatedAt(LocalDate.now());

        Optional<Form> form = formRepository.findById(submissionRequestDto.formId());

        if(form.isEmpty())
            return BaseResponse.builder()
                    .success(false)
                    .code("ERROR")
                    .message("Form not found")
                    .build();

        submission.setForm(form.get());
        submission.setSubmittedBy(user);

        String schemaAsString = objectMapper.writeValueAsString(submissionRequestDto.fields());
        log.info("Creating form by schema : {}", schemaAsString);
        submission.setAnswersJson(schemaAsString);

        submissionRepository.save(submission);
        return BaseResponse.builder()
                .success(true)
                .code("SUCCESS")
                .message("Form created successfully")
                .build();

    }

    @Transactional
    public List<SubmissionDto> getListSubmission() {

        return submissionRepository.findAll()
                .stream()
                .map(this::getFormInfosDto)
                .toList();

    }

    private SubmissionDto getFormInfosDto(Submission submission) {
        SubmissionDto submissionDto = new SubmissionDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        submissionDto.setId(submission.getId());
        submissionDto.setSubmittedBy(submission.getSubmittedBy().getUsername());
        submissionDto.setStatus(submission.getStatus());
        submissionDto.setVersion(submission.getVersion());
        submissionDto.setCreatedBy(submission.getCreatedBy());
        submissionDto.setCreatedAt(submission.getCreatedAt().format(formatter));

        Optional<Form> formOptional = formRepository.findById(submission.getForm().getId());
        formOptional.ifPresent(form -> submissionDto.setFormId(form.getTitle()));

        return submissionDto;
    }

    private SubmissionDto getSubmissionDto(Submission submission) {
        SubmissionDto submissionDto = new SubmissionDto();
        try{
            submissionDto = getFormInfosDto(submission);

            TypeReference<List<FieldSubmissionDto>> typeRef = new TypeReference<List<FieldSubmissionDto>>() {};
            List<FieldSubmissionDto> schema = objectMapper.readValue(submission.getAnswersJson(), typeRef);


            submissionDto.setSchema(schema);

        }catch (JacksonException ex){
            log.error("exception while parsing form", ex);
            log.error(ex.getMessage());
        }
        return submissionDto;
    }

    public SubmissionDto getSubmissionById(String id) {
        return submissionRepository.findById(id)
                .map(this::getSubmissionDto)
                .orElse(null);

    }

}
