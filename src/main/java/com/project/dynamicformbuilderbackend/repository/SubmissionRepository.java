package com.project.dynamicformbuilderbackend.repository;


import com.project.dynamicformbuilderbackend.entities.Form;
import com.project.dynamicformbuilderbackend.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {

    @Query("select f.id, f.title, f.createdBy, f.version, f.status, f.schemaJson from Form f where f.createdBy = :username")
    List<Map<String, Object>> findAllByUsername(@Param("username") String username);
}
