package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class IssueService {

    final IssueRepository issueRepository;
    final PersonRepository personRepository;

    public Issue save (Issue issue, String creatorName){
        if (issue.getId() == null)
            issue.setDateCreated(Date.from(Instant.now()));
        issue.setLastUpdated(Date.from(Instant.now()));
        Person creator = personRepository.findByUsername(creatorName).orElseThrow();
        issue.setCreator(creator);
        return issueRepository.save(issue);
    }
}
