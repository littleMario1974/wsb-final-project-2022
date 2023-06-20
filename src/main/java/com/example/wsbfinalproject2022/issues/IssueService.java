package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.mail.Mail;
import com.example.wsbfinalproject2022.mail.MailService;
import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.person.PersonRepository;
import com.example.wsbfinalproject2022.projects.Project;
import com.example.wsbfinalproject2022.projects.ProjectRepository;
import com.example.wsbfinalproject2022.projects.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IssueService {

    final MailService mailService;
    final IssueRepository issueRepository;
    final PersonRepository personRepository;
    final ProjectRepository projectRepository;
    final ProjectService projectService;

    public Page<Issue> findAll(IssueFilter filter, Pageable pageable) {
        return issueRepository.findAll(filter.buildQuery(), pageable);

    }
    public List<Issue> findAll(Specification spec){
        return issueRepository.findAll(spec);
    }

    public List<Issue> findAllEnabled() {
        return issueRepository.findAllByEnabled(true);
    }

    public Set<Project> findAllProjects() {
        return findAllEnabled()
                .stream()
                .map(Issue::getProject)
                .sorted(Comparator.comparing(Project::getName))
                .collect(Collectors.toCollection(TreeSet::new));
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

    public List<Issue> findAllIssuesByAssignee(Person assignee){
        return issueRepository.findAllByAssignee(assignee);
    }

    public Issue save(Issue issue, String creatorName) {
        if (issue.getId() == null) {
            issue.setDateCreated(Date.from(Instant.now()));
        }
        issue.setLastUpdated(Date.from(Instant.now()));
        Person creator = personRepository.findByUsername(creatorName).orElseThrow();
        issue.setCreator(creator);


        Person assignee = issue.getAssignee();
        if (assignee != null && assignee.getEnabled()) {
                Mail mail = new Mail();
                mail.setRecipient(issue.getAssignee().getEmail());
                mail.setSubject("Uwaga ! Nowe zgłoszenie !");
                mail.setContent("Przypisano do Ciebie nowe zgłoszenie pod nazwą " + "'" + issue.getName() + "'" + "." + "\n\nBierz się do roboty !");

                mailService.sendMail(mail);

            }
        return issueRepository.save(issue);
    }
/*
    public Issue save2(Issue issue, String creatorName) {

        if (issue.getId() == null)
        { issue.setDateCreated(Date.from(Instant.now()));Person creator = personRepository.findByUsername(creatorName).orElseThrow();
            issue.setCreator(creator);}

        Optional<Issue> issueOptional = issueRepository.findById(issue.getId());
        issueOptional.ifPresent(issueZBazy -> {
            if(issueZBazy.getAssignee()!=issue.getAssignee())
                // todo wyślij maila do typa z obiektu issue.assignee
                return;
        });
        issue.setLastUpdated(Date.from(Instant.now()));

        return issueRepository.save(issue);
    }
*/


}