package com.example.wsbfinalproject2022.projects;

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
public class ProjectService {

    final PersonRepository personRepository;
    final ProjectRepository projectRepository;

    public List<Project> findAll(ProjectFilter filter) {
        return projectRepository.findAll(filter.buildQuery());
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

    public Project save(Project project, String creatorName) {
        if (project.getId() == null) // to można wyrzucić
            project.setDateCreated(Date.from(Instant.now()));
        Person creator = personRepository.findByUsername(creatorName).orElseThrow();
        project.setCreator(creator);
        return projectRepository.save(project);
    }

}
