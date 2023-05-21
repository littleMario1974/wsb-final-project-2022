package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.person.PersonRepository;
import com.example.wsbfinalproject2022.projects.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IssueService {


    // public Boolean notAssigned = false;

    final IssueRepository issueRepository;
    final PersonRepository personRepository;

    public Page<Issue> findAll(IssueFilter filter, Pageable pageable) {
        return issueRepository.findAll(filter.buildQuery(), pageable);

    }

    public List<Issue> findAllEnabled() {
        return issueRepository.findAllByEnabled(true);
    }

    public Set<Project> findAllProjects() {
        return findAllEnabled()
                .stream()
                .map(Issue::getProject)
                .collect(Collectors.toSet());
    }

    public Set<Person> findAllAssignees() {
        Set<Person> set = new HashSet<>();
        for (Issue issue : findAllEnabled()) {
            Person assignee = issue.getAssignee();
                set.add(assignee);
            }
        return set;
    }


    public Set<Status> findAllStatuses() {
        return findAllEnabled()
                .stream()
                .map(Issue::getStatus)
                .collect(Collectors.toSet());

    }

    public Issue save(Issue issue, String creatorName) {
        if (issue.getId() == null)
            issue.setDateCreated(Date.from(Instant.now()));
        issue.setLastUpdated(Date.from(Instant.now()));
        Person creator = personRepository.findByUsername(creatorName).orElseThrow();
        issue.setCreator(creator);
        return issueRepository.save(issue);
    }

}