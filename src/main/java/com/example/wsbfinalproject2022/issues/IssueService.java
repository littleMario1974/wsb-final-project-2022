package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    final IssueRepository issueRepository;
    final PersonRepository personRepository;

    public List<Issue> findAll(IssueFilter filter) {
        return issueRepository.findAll(filter.buildQuery());

    }
    public List<Issue> findAllEnabled() {
        return issueRepository.findAllByEnabled(true);
    }

    public Set<Person> findAllCreators() {
        return findAllEnabled()
                .stream()
                .map(Issue::getCreator)
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