package com.project.dynamicformbuilderbackend.service;


import com.project.dynamicformbuilderbackend.dtos.*;
import com.project.dynamicformbuilderbackend.entities.Form;
import com.project.dynamicformbuilderbackend.entities.User;
import com.project.dynamicformbuilderbackend.enums.FormStatus;
import com.project.dynamicformbuilderbackend.repository.FormRepository;
import com.project.dynamicformbuilderbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FormService {
    private final Logger log = LoggerFactory.getLogger(FormService.class);
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final FormRepository formRepository;

    public FormService(UserRepository userRepository, ObjectMapper objectMapper, FormRepository formRepository) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.formRepository = formRepository;
    }

    public BaseResponse createForm(FormRequestDto formRequestDto, String userId) {
        Form form = new Form();
        form.setTitle(formRequestDto.title());
        form.setStatus(FormStatus.DRAFT);
        User user = userRepository.findByUsername(userId);
        log.info("Creating form by user : {}", user);

        form.setCreatedBy(user.getUsername());
        form.setCreatedAt(LocalDate.now());

        List<InputSchema> schema =  formRequestDto.schemas().stream()
                .map(s -> s.withId(UUID.randomUUID().toString()))
                .toList();

        String schemaAsString = objectMapper.writeValueAsString(schema);
        log.info("Creating form by schema : {}", schemaAsString);
        form.setSchemaJson(schemaAsString);

        formRepository.save(form);
        return BaseResponse.builder()
                .success(true)
                .code("SUCCESS")
                .message("Form created successfully")
                .build();

    }

    @Transactional
    public FormListDto getAdminListForm(int page, int size, String sortBy, String username) {
        String sortCriteria = StringUtils.isEmpty(sortBy) ? "createdAt" : sortBy;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortCriteria).descending());
        Page<Form> formPage = formRepository.findAllByCreatedBy(username, pageable);

        List<FormDto> formDtos = formPage.getContent()
                .stream()
                .map(this::getFormInfosDto)
                .toList();

        return new FormListDto(formDtos, page, size, formPage.getTotalElements(), sortCriteria);
    }

    @Transactional
    public FormListDto getUserListForm(int page, int size, String sortBy, String username) {
        String sortCriteria = StringUtils.isEmpty(sortBy) ? "createdAt" : sortBy;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortCriteria).descending());
        Page<Form> formPage = formRepository.findAll(pageable);

        List<FormDto> formDtos = formPage.getContent()
                .stream()
                .map(this::getFormInfosDto)
                .toList();

        return new FormListDto(formDtos, page, size, formPage.getTotalElements() ,sortCriteria);
    }

    private FormDto getFormInfosDto(Form form) {
        FormDto formDto = new FormDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        formDto.setId(form.getId());
        formDto.setTitle(form.getTitle());
        formDto.setStatus(form.getStatus());
        formDto.setVersion(form.getVersion());
        formDto.setCreatedBy(form.getCreatedBy());
        formDto.setCreatedAt(form.getCreatedAt().format(formatter));

        return formDto;
    }

    private FormDto getFormDto(Form form) {
        FormDto formDto = new FormDto();
        try{
            formDto = getFormInfosDto(form);

            TypeReference<List<InputSchema>> inputSchemaTypeRef = new TypeReference<List<InputSchema>>() {};
            List<InputSchema> schema = objectMapper.readValue(form.getSchemaJson(), inputSchemaTypeRef);
            formDto.setSchema(schema);

        }catch (JacksonException ex){
            log.error("exception while parsing form", ex);
            log.error(ex.getMessage());
        }
        return formDto;
    }

    public FormDto getFormById(String id) {
        return formRepository.findById(id)
                .map(this::getFormDto)
                .orElse(null);

    }
}
