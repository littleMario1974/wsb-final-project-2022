package com.example.wsbfinalproject2022.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @GetMapping
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView index(@ModelAttribute ProjectFilter filter) {

        List<Project> projects = projectService.findAll(filter);

        ModelAndView modelAndView = new ModelAndView("projects/index");
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("creators", projectService.findAllCreators());

        return modelAndView;
    }


    @GetMapping("/create")
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("projects/create");

        Project project = new Project();
        modelAndView.addObject("project", project);

        return modelAndView;
    }

    /**
     * https://www.baeldung.com/spring-boot-crud-thymeleaf
     *
     * @return
     */

    @GetMapping("/edit/{id}")
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView edit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("projects/create");

        Project project = projectRepository.findById(id).orElse(null);
        modelAndView.addObject("project", project);

        return modelAndView;
    }

    /**
     * Dokumentacja - https://spring.io/guides/gs/handling-form-submission/
     *
     * @param project
     * @return
     */

    @PostMapping("/save")
    @Secured("ROLE_MANAGE_PROJECT")
    String save(@ModelAttribute Project project, Principal principal) throws ParseException {
        projectService.save(project, principal.getName());
        return "redirect:/projects";
    }


    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_PROJECT")
    ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("projects/delete");

        Project project = projectRepository.findById(id).orElse(null);
        modelAndView.addObject("project", project);

        return modelAndView;
    }

/*
    // ta metoda delete kasuje projekt z bazy danych
    @PostMapping("/delete")
    @Secured("ROLE_MANAGE_PROJECT")
    String delete(@ModelAttribute Project project) {

        projectRepository.deleteById(project.getId());

        return "redirect:/projects";

    }

 */
// ta metoda delete wyłącza w rzeczywistości projekt, nie usuwa z bazy danych

    @PostMapping("/delete")
    @Secured("ROLE_MANAGE_PROJECT")
    String delete(@ModelAttribute Project project, Principal principal) throws ParseException {
         project.setEnabled(false);
        projectService.save(project, principal.getName());
        return "redirect:/projects";
    }

}