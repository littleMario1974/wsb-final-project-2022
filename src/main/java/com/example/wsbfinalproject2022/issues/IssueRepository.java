package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {
    @Query(value = "select * from issue where enabled = :enabled",
            nativeQuery = true)
    //List<Issue> findByEnabledNative(@Param("enabled") Boolean enabled);

    List<Issue> findAllByEnabled(Boolean enabled);

    List<Issue> findAllByAssignee(Person person);

    Issue findByCode(String code);
}