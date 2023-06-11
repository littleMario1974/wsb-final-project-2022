package com.example.wsbfinalproject2022.projects;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    final PersonRepository personRepository;
    final ProjectRepository projectRepository;

    public Page<Project> findAll(ProjectFilter filter, Pageable pageable) {
        return projectRepository.findAll(filter.buildQuery(), pageable);
    }

    public List<Project> findAllEnabled() {
        return projectRepository.findAllByEnabled(true);
    }


    public Set<Person> findAllCreators() {
        return findAllEnabled()
                .stream()
                .map(Project::getCreator)
                .collect(Collectors.toSet());
    }

    public Project save(Project project, String creatorName) throws ParseException {

        if (project.getId() == null)
            project.setDateCreated(Date.from(Instant.now()));
        Person creator = personRepository.findByUsername(creatorName).orElseThrow();
        project.setCreator(creator);
        return projectRepository.save(project);
    }

}
