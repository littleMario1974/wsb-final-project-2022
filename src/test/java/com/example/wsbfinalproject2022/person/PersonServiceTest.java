package com.example.wsbfinalproject2022.person;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @InjectMocks
    PersonService personService;

    @Test
    void personShouldExistAndHaveNoPassword() {
//given
        when(personRepository.findById(any())).thenReturn(Optional.of(Person.builder().id(1l).username("waclaw").password("hg8iuov5t3n4875t34gvibv5ntei4t63w86nf53tni87o23").build()));


//when
        Optional<Person> testObj = personService.findById(1l);


//then
        assertEquals("waclaw", testObj.get().getUsername());
        assertNull(testObj.get().getPassword());

    }
}