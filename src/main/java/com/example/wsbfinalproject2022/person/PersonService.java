package com.example.wsbfinalproject2022.person;

import com.example.wsbfinalproject2022.issues.Issue;
import com.example.wsbfinalproject2022.issues.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IssueService issueService;

    private static void accept(Issue issue) {
        issue.setAssignee(null);
    }

    public List<Person> findAllEnabled() {
        return personRepository.findAllByEnabled(true)
                .stream()
                .map(filterPassword())
                .toList();
    }
    /*public void prepareAdminUser() {
        if (personRepository.findByUsername("admin").isPresent()) {
            return;
        }

        Person person = new Person ("admin", "1234", true, "admin@gmail.com");

        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));

        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        person.setAuthorities(new HashSet<>(authorities));

        personRepository.save(person);
    }*/

    public Person findByName(String name) {
        return personRepository.findByUsernameAndEnabled(name, true)
                .map(filterPassword())
                .orElseThrow();
    }

    protected void savePerson(Person person) {
        if (person.getId() != null) {
            // Pobierz istniejącą osobę z bazy danych
            Optional<Person> existingPersonOptional = personRepository.findById(person.getId());
            if (existingPersonOptional.isPresent()) {
                Person existingPerson = existingPersonOptional.get();
                String existingPassword = existingPerson.getPassword();

                // Sprawdź, czy hasło zostało zmienione
                if (!"".equals(person.getPassword())) {
                    // Hasło zostało zmienione, zahaszuj je ponownie
                    String hashedPassword = bCryptPasswordEncoder.encode(person.getPassword());
                    person.setPassword(hashedPassword);
                } else {
                    // Hasło nie zostało zmienione, nie trzeba go ponownie zapisywać
                    person.setPassword(existingPassword);
                }
            }
        } else {
            // Nowy użytkownik, zahaszuj hasło
            String hashedPassword = bCryptPasswordEncoder.encode(person.getPassword());
            person.setPassword(hashedPassword);
        }

        person.setEnabled(true);
        person.setDateCreated(Date.from(Instant.now()));
        personRepository.save(person);
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id).map(filterPassword());
    }

    private static Function<Person, Person> filterPassword() {
        return person -> {
            person.setPassword(null);
            return person;
        };
    }

    public void disablePerson(Person person) {
        Person daoPerson = personRepository.findById(person.getId()).orElseThrow();
        List<Issue> assignedIssues = issueService.findAllIssuesByAssignee(daoPerson);
        assignedIssues
                .stream()
                .peek(issue -> issue.setAssignee(null))
                .forEach(issue -> issueService.save(issue, issue.getCreator().getUsername()));
        daoPerson.setEnabled(false);
        String randomPassword = RandomPasswordGenerator.generateRandomString(RandomPasswordGenerator.PASSWORD_LENGTH);
        daoPerson.setPassword(randomPassword);
        personRepository.save(daoPerson);
    }

    public static class RandomPasswordGenerator {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        private static final int PASSWORD_LENGTH = 60;

        public static void main(String[] args) {
            String randomPassword = generateRandomString(PASSWORD_LENGTH);

        }

        public static String generateRandomString(int length) {
            SecureRandom secureRandom = new SecureRandom();
            StringBuilder sb = new StringBuilder(length);

            for (int i = 0; i < length; i++) {
                int randomIndex = secureRandom.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }

            return sb.toString();
        }
    }
}
