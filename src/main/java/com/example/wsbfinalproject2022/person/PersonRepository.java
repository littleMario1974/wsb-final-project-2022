package com.example.wsbfinalproject2022.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);

    Optional<Person> findByUsernameAndEnabled(String username, Boolean enabled);

    Iterable<Person> findAllByUsernameOrPassword(String s1, String s2);

    Iterable<Person> findAllByDateCreatedAfter(Date d);
}
