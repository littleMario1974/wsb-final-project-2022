package com.example.wsbfinalproject2022.projects;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProjectService {

    final ProjectRepository projectRepository;
    final PersonRepository personRepository;

    public Project save (Project project, String creatorName){
        if (project.getId() == null) // to można wyrzucić
            project.setDateCreated(Date.from(Instant.now()));
        Person creator = personRepository.findByUsername(creatorName).orElseThrow();
        project.setCreator(creator);
        return projectRepository.save(project);
    }
}
