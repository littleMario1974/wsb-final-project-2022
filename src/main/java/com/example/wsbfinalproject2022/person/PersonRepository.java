package com.example.wsbfinalproject2022.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByEnabled(Boolean enabled);

    Optional<Person> findByUsername(String username);

    Optional<Person> findByUsernameAndEnabled(String username, Boolean enabled);

    Iterable<Person> findAllByUsernameOrPassword(String s1, String s2);

    Iterable<Person> findAllByDateCreatedAfter(Date d);

}
