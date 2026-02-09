package com.project.dynamicformbuilderbackend.service;


import com.project.dynamicformbuilderbackend.dtos.BaseResponse;
import com.project.dynamicformbuilderbackend.dtos.FormDto;
import com.project.dynamicformbuilderbackend.dtos.FormRequestDto;
import com.project.dynamicformbuilderbackend.dtos.InputSchema;
import com.project.dynamicformbuilderbackend.entities.Form;
import com.project.dynamicformbuilderbackend.entities.User;
import com.project.dynamicformbuilderbackend.enums.FormStatus;
import com.project.dynamicformbuilderbackend.repository.FormRepository;
import com.project.dynamicformbuilderbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

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

        form.setCreatedBy(user);

        String schema = objectMapper.writeValueAsString(formRequestDto.schemas());
        log.info("Creating form by schema : {}", schema);
        form.setSchemaJson(schema);

        formRepository.save(form);
        return BaseResponse.builder()
                .success(true)
                .code("SUCCESS")
                .message("Form created successfully")
                .build();

    }

    @Transactional
    public List<FormDto> getListForm() {

        return formRepository.findAll()
                .stream()
                .map(this::getFormDto)
                .toList();

    }

    private FormDto getFormDto(Form form) {
        FormDto formDto = new FormDto();

        try{
            formDto.setId(form.getId());
            formDto.setTitle(form.getTitle());
            formDto.setStatus(form.getStatus());
            formDto.setVersion(form.getVersion());
            formDto.setCreatedBy(form.getCreatedBy().getUsername());

            List<InputSchema> schema = objectMapper.readValue(form.getSchemaJson(), new TypeReference<List<InputSchema>>() {});
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
