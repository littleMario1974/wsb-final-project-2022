package com.example.wsbfinalproject2022.config;

import com.example.wsbfinalproject2022.authorities.Authority;
import com.example.wsbfinalproject2022.authorities.AuthorityName;
import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import com.example.wsbfinalproject2022.person.PersonService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Bootstrap implements InitializingBean {

    private final AuthorityRepository authorityRepository;
    private final PersonService personService;

    public Bootstrap(AuthorityRepository authorityRepository, 
                     PersonService personService) {
        this.authorityRepository = authorityRepository;
        this.personService = personService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (AuthorityName authorityName : AuthorityName.values()) {
            Optional<Authority> authority = authorityRepository.findByAuthority(authorityName);

            if (authority.isEmpty()) {
                Authority a = new Authority(authorityName);
                authorityRepository.save(a);
            }

        }
// zapisanie AdminUser z pełnymi uprawnieniami - wywołanie metody z PersonService
        //personService.prepareAdminUser();
    }
}