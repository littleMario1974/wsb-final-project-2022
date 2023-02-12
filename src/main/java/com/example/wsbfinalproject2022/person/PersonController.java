package com.example.wsbfinalproject2022.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
                       @RequestParam String password) {
        Person person = new Person(username, password, true);
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
}