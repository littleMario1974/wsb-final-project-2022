package com.example.wsbfinalproject2022.authorities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);

    Optional<Authority> findByAuthority(AuthorityName authorityName);

    @Query("select a from Person p join p.authorities a where p.username = :username")
    Iterable<Authority> findAllByUsername (String username);
}
