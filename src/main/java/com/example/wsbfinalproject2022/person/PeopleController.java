package com.example.wsbfinalproject2022.person;

import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

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
        modelAndView.addObject("people", personService.findAllEnabled());
        return modelAndView;
    }
    @GetMapping("/edit/self")
    ModelAndView self(){
        ModelAndView modelAndView = new ModelAndView("people/self");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        Person person = personService.findByName(username);
        modelAndView.addObject("person", person);

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

        Person person = personService.findById(id).orElse(null);
        modelAndView.addObject("person", person);

        return modelAndView;
    }

    @PostMapping("/save")
        //@Secured("ROLE_MANAGE_USERS")
    ModelAndView save(@RequestParam("password") String password, @RequestParam("repeatedPassword") String repeatedPassword, @ModelAttribute @Valid Person person, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("people/create");

        //metoda sprawdzająca, czy hasła się różnią - pobrano z formularza @RequestParam
        if (!password.equals(repeatedPassword)) {
            bindingResult.rejectValue("repeatedPassword", "the.passwords.are.different");
            return modelAndView;

        }


        if (bindingResult.hasErrors()) {
            modelAndView.addObject("person", person);
            return modelAndView;
        }


        // walidacja - przy tworzeniu nowego użytkownika jeśli użytkownik o tej nazwie istnieje w polu wpisz komunikat

        if (person.getId() == null) {

            if (isPersonNameExists(person)) {
                bindingResult.rejectValue("username", "duplicate.username");
                return modelAndView;
            }



        }
        personService.savePerson(person);

        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }


    //metoda sprawdzajaca czy Person o danej nazwie istnieje
    private boolean isPersonNameExists(Person person) {
        Optional<Person> existingPerson = personRepository.findByUsername(person.getUsername());
        return existingPerson.isPresent();
    }



    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_USERS")
    ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("people/delete");

        Person person = personService.findById(id).orElse(null);
        modelAndView.addObject("person", person);

        return modelAndView;
    }

    @PostMapping("/delete")
    @Secured("ROLE_MANAGE_USERS")
    String delete(@ModelAttribute Person person) {
//        person.setEnabled(false);
//        personRepository.save(person);
        personService.disablePerson(person);

        return "redirect:/people/";

    }



}
