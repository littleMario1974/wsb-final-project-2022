package com.example.wsbfinalproject2022.person;

import com.example.wsbfinalproject2022.authorities.Authority;
import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class PersonService {

    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PersonService(AuthorityRepository authorityRepository,
                         PersonRepository personRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authorityRepository = authorityRepository;
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void prepareAdminUser() {
        if (personRepository.findByUsername("user4").isPresent()) {
            return;
        }

        Person person = new Person ("user4", "1234", true);

        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));

        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        person.setAuthorities(new HashSet<>(authorities));

        personRepository.save(person);
    }

    protected void savePerson(Person person) {
        String hashedPassword = bCryptPasswordEncoder.encode(person.getPassword());
        person.setPassword(hashedPassword);
        person.setEnabled(true);
        person.setDateCreated(Date.from(Instant.now()));
        personRepository.save(person);
    }
}
