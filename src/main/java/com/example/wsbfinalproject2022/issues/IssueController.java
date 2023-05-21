package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.authorities.AuthorityRepository;
import com.example.wsbfinalproject2022.person.PersonRepository;
import com.example.wsbfinalproject2022.projects.ProjectFilter;
import com.example.wsbfinalproject2022.projects.ProjectRepository;
import com.example.wsbfinalproject2022.projects.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final IssueService issueService;
    private final ProjectService projectsService;
    private final ProjectRepository projectRepository;


    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, AuthorityRepository authorityRepository, IssueService issueService, ProjectService projectService, ProjectRepository projectRepository) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.issueService = issueService;
        this.projectsService = projectService;
        this.projectRepository = projectRepository;
    }

    @GetMapping
    ModelAndView index(@ModelAttribute IssueFilter filter, ProjectFilter projectFilter, Pageable pageable) {

        Page<Issue> issues = issueService.findAll(filter, pageable);

        ModelAndView modelAndView = new ModelAndView("issues/index");
        modelAndView.addObject("issues", issues);// wyświetla wszystkie zadania +++++++++++++
        modelAndView.addObject("filter", filter); // ++++++++++++++++++
        modelAndView.addObject("assignees", issueService.findAllAssignees());// wyświetla w liście rozwijanej index.html wszystkich wykonawców
        modelAndView.addObject("statuses", issueService.findAllStatuses());
        modelAndView.addObject("projects", issueService.findAllProjects()); // przekazuje z powrotem do widoku

        return modelAndView;
    }

    @GetMapping("/create")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("issues/create");
        Issue issue = new Issue();
        modelAndView.addObject("issue", issue);
        modelAndView.addObject("projects", projectRepository.findAll(Sort.by(Sort.Order.by("code")).ascending())); // w widoku Zadań wyświetli wszystkie projekty
        modelAndView.addObject("person", personRepository.findAll(Sort.by(Sort.Order.by("username")).ascending())); //w widoku Zadań wyświetli wszystkie osoby

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
        // TODO: @Secured("ROLE_PROJECT_EDIT")
    ModelAndView edit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/create");

        Issue issue = issueRepository.findById(id).orElse(null);
        modelAndView.addObject("issue", issue);
        modelAndView.addObject("projects", projectRepository.findAll(Sort.by(Sort.Order.by("code")).ascending())); // w widoku Zadań wyświetli wszystkie projekty
        modelAndView.addObject("person", personRepository.findAll(Sort.by(Sort.Order.by("username")).ascending())); //w widoku Zadań wyświetli wszystkie osoby

        return modelAndView;
    }



    @PostMapping("/save")
    String save(@ModelAttribute Issue issue, Principal principal) {
        issueService.save(issue, principal.getName());
        return "redirect:/issues";
    }

    @GetMapping("/delete/{id}")
    ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/delete");

        Issue issue = issueRepository.findById(id).orElse(null);
        modelAndView.addObject("issue", issue);

        return modelAndView;
    }

    @PostMapping("/delete")
    String delete(@ModelAttribute Issue issue) {

        issueRepository.deleteById(issue.getId());

        return "redirect:/issues";


    }


}



