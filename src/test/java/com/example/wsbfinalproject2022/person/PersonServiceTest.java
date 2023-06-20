package com.example.wsbfinalproject2022.person;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    PersonRepository personRepository;


    @InjectMocks
    PersonService personService;

    @Test
    void personShouldExist() {
//given
        when(personRepository.findById(any())).thenReturn(Optional.of(Person.builder().id(1l).username("waclaw").email("email@gmail.com").build()));


//when
        Optional<Person> testObj = personService.findById(1l);


//then
        assertEquals("waclaw", testObj.get().getUsername());
        assertEquals("email@gmial.com", testObj.get().getEmail());

    }
}