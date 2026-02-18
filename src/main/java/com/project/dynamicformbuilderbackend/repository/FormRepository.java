package com.project.dynamicformbuilderbackend.repository;


import com.project.dynamicformbuilderbackend.dtos.FormDto;
import com.project.dynamicformbuilderbackend.entities.Form;
import com.project.dynamicformbuilderbackend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {

    @Query("select f.id, f.title, f.createdBy, f.version, f.status, f.schemaJson from Form f where f.createdBy = :username")
    List<Map<String, Object>> findAllByUsername(@Param("username") String username);

    Page<Form> findAllByCreatedBy(String createdBy, Pageable pageable);
}
