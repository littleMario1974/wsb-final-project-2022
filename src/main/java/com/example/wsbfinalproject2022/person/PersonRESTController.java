package com.example.wsbfinalproject2022.person;

import com.example.wsbfinalproject2022.authorities.Authority;
import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/people")
public class PersonRESTController {

    private final PersonRepository personRepository;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public PersonRESTController(PersonRepository personRepository, AuthorityRepository authorityRepository) {
        this.personRepository = personRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/")
    public Iterable<Person> list() {
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Person> find(@PathVariable Long id) {
        return personRepository.findById(id);
    }

    @PostMapping("/")
    public Person save(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String userRealName,
                       @RequestParam String email) {
        Person person = new Person(username, password, userRealName, true, email);
        return personRepository.save(person);

    }

    @GetMapping("/show")
    public Optional<Person> show(@RequestParam String username) {
        return personRepository.findByUsername(username);
    }

    @GetMapping("/show-enabled")
    public Optional<Person> showEnabled(@RequestParam String username) {
        return personRepository.findByUsernameAndEnabled(username, true);
    }


    @GetMapping("/show-by-username-or-password")
    public Iterable<Person> showByUsernameOrPassword(@RequestParam String username, @RequestParam String password) {
        return personRepository.findAllByUsernameOrPassword(username, password);
    }

    @GetMapping("/list-created-after")
    public Iterable<Person> listCreatedAfter(@RequestParam String dateString) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        return personRepository.findAllByDateCreatedAfter(date);
    }
    @GetMapping("/authorities")
    public Iterable<Authority> getAuthorities(@RequestParam String username) {
        return authorityRepository.findAllByUsername(username);
    }

    @PostMapping("{username}/authorities")
    public Person addAuthority(@PathVariable String username, @RequestParam String authority) {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);

        if (optionalPerson.isEmpty()) {
            throw new InvalidParameterException("Nie znaleziono użytkownika.");
        }
        Optional<Authority> optionalAuthorityInstance=authorityRepository.findByAuthority(authority);
        if (optionalAuthorityInstance.isEmpty()) {
            throw new InvalidParameterException("Nie znaleziono uprawnienia.");
        }

        Person person = optionalPerson.get();
        person.authorities.add(optionalAuthorityInstance.get());
        personRepository.save(person);

        return person;
    }
}