package com.example.wsbfinalproject2022.person;

import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Person> findAllEnabled() {
        return personRepository.findAllByEnabled(true);
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

    protected void savePerson(Person person) {
        if (person.getId() != null) {
            // Pobierz istniejącą osobę z bazy danych
            Optional<Person> existingPersonOptional = personRepository.findById(person.getId());
            if (existingPersonOptional.isPresent()) {
                Person existingPerson = existingPersonOptional.get();
                String existingPassword = existingPerson.getPassword();

                // Sprawdź, czy hasło zostało zmienione
                if (!existingPassword.equals(person.getPassword())) {
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

}
