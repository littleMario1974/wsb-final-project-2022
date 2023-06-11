package com.example.wsbfinalproject2022.person;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonRepository personRepository;
    private final PersonService personService;

    public PeopleController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("/")
    @Secured({"ROLE_MANAGE_USERS", "ROLE_USER_TAB"})
    ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("people/index");
        modelAndView.addObject("people", personRepository.findAllByEnabled(true));
        return modelAndView;
    }

    @GetMapping("/create")
    @Secured("ROLE_MANAGE_USERS")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("people/create");
        modelAndView.addObject("person", new Person());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_MANAGE_USERS")
    ModelAndView edit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("people/create");

        Person person = personRepository.findById(id).orElse(null);
        modelAndView.addObject("person", person);

        return modelAndView;
    }

    @PostMapping("/save")
    @Secured("ROLE_MANAGE_USERS")
    ModelAndView save(@ModelAttribute Person person) {

        personService.savePerson(person);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/people/");

        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_USERS")
    ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("people/delete");

        Person person = personRepository.findById(id).orElse(null);
        modelAndView.addObject("person", person);

        return modelAndView;
    }

    @PostMapping("/delete")
    @Secured("ROLE_MANAGE_USERS")
    String delete(@ModelAttribute Person person) {
        person.setEnabled(false);
        personRepository.save(person);

        return "redirect:/people/";

    }



}
