package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import com.example.wsbfinalproject2022.person.PersonRepository;
import com.example.wsbfinalproject2022.projects.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final IssueService issueService;


    @Autowired
    public IssueController(ProjectRepository projectRepository, PersonRepository personRepository, PersonRepository personRepository1, AuthorityRepository authorityRepository, IssueService issueService) {
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;
        this.issueService = issueService;
    }


    @GetMapping("/create")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("issues/create");
        Issue issue = new Issue();
        modelAndView.addObject("issue", issue);
        modelAndView.addObject("projects", projectRepository.findAll()); // w widoku Zadań wyświetli wszystkie projekty
        modelAndView.addObject("person", personRepository.findAll(Sort.by(Sort.Order.by("username")).ascending())); //w widoku Zadań wyświetli wszystkie osoby

        return modelAndView;
    }

    @PostMapping("/save")
    String save(@ModelAttribute Issue issue, Principal principal) {
        issueService.save(issue, principal.getName());
        return "redirect:/projects";
    }
}
