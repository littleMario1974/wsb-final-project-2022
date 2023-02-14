package com.example.wsbfinalproject2022.person;

import com.example.wsbfinalproject2022.authorities.Authority;
import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class PersonService {

    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;

    public PersonService(AuthorityRepository authorityRepository, PersonRepository personRepository) {
        this.authorityRepository = authorityRepository;
        this.personRepository = personRepository;
    }

    public void prepareAdminUser() {
        if (personRepository.findByUsername("admin").isPresent()) {
            return;
        }

        Person person = new Person ("admin", "1234", true);

        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        person.setAuthorities(new HashSet<>(authorities));

        personRepository.save(person);
    }
}
