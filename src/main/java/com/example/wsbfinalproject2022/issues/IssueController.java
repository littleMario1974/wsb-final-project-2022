package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.PersonRepository;
import com.example.wsbfinalproject2022.projects.ProjectRepository;
import com.example.wsbfinalproject2022.projects.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, IssueService issueService, ProjectService projectService, ProjectRepository projectRepository) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.issueService = issueService;
        this.projectsService = projectService;
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView index(@ModelAttribute IssueFilter filter, Pageable pageable) {

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
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("issues/create");
        Issue issue = new Issue();
        modelAndView.addObject("issue", issue);
        modelAndView.addObject("projects", projectRepository.findAllByEnabled(true)); // w widoku Zadań wyświetli wszystkie projekty
        modelAndView.addObject("person", personRepository.findAll(Sort.by(Sort.Order.by("username")).ascending())); //w widoku Zadań wyświetli wszystkie osoby

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView edit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/create");

        Issue issue = issueRepository.findById(id).orElse(null);
        modelAndView.addObject("issue", issueRepository.findAllByEnabled(true));
        modelAndView.addObject("issue", issue);
        modelAndView.addObject("projects", projectRepository.findAllByEnabled(true)); // w widoku Zadań wyświetli wszystkie projekty
        modelAndView.addObject("person", personRepository.findAllByEnabled(true)); //w widoku Zadań wyświetli wszystkie osoby

        return modelAndView;
    }

    @PostMapping("/save")
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView save(@ModelAttribute @Valid Issue issue,
                      BindingResult bindingResult, Principal principal) {

        ModelAndView modelAndView = new ModelAndView("issues/create");

        if (bindingResult.hasErrors()) {

            modelAndView.addObject("issue", issue);
            modelAndView.addObject("projects", projectRepository.findAllByEnabled(true)); // w widoku Zadań wyświetli wszystkie projekty
            modelAndView.addObject("person", personRepository.findAll(Sort.by(Sort.Order.by("username")).ascending())); //w widoku Zadań wyświetli wszystkie osoby
            return modelAndView;
        }
        // walidacja - jeśli issue o tym kodzie istnieje w polu code wpisz komunikat

        if (issue.getId() == null) {

            if (isIssueNameExists(issue)) {
                bindingResult.rejectValue("name", "duplicate.name");
                return modelAndView;
            }

            if (isIssueCodeExists(issue)) {
                bindingResult.rejectValue("code", "duplicate.code");
                return modelAndView;
            }
        }
        issueService.save(issue, principal.getName());

        modelAndView.setViewName("redirect:/issues");

        return modelAndView;
    }

    //metoda sprawdzajaca czy Issue z danym kodem istnieje
    private boolean isIssueCodeExists(Issue issue) {
        Issue existingIssue = issueRepository.findByCode(issue.getCode());
        return existingIssue != null;
    }

    //metoda sprawdzajaca czy Issue o danej nazie istnieje
    private boolean isIssueNameExists(Issue issue) {
        Issue existingIssue = issueRepository.findByName(issue.getName());
        return existingIssue != null;
    }






    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/delete");

        Issue issue = issueRepository.findById(id).orElse(null);
        modelAndView.addObject("issue", issue);

        return modelAndView;
    }

    @PostMapping("/delete")
    @Secured("ROLE_MANAGE_PROJECT")
    String delete(@ModelAttribute Issue issue) {
        issue.setEnabled(false);
        issueRepository.save(issue);

        return "redirect:/issues";

    }


    /* rzeczywiście usuwa zadanie z bazy danych
    @PostMapping("/delete")
    String delete(@ModelAttribute Issue issue) {

        issueRepository.deleteById(issue.getId());

        return "redirect:/issues";

    }
    */


}



