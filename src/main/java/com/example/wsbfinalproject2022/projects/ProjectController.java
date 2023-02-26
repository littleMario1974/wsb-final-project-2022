package com.example.wsbfinalproject2022.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Secured("ROLE_MANAGE_PROJECT")
    @GetMapping
    ModelAndView index(@ModelAttribute ProjectFilter filter) {

        List<Project> projects = projectService.findAll(filter);

        ModelAndView modelAndView = new ModelAndView("projects/index");
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("creators", projectService.findAllCreators());

        return modelAndView;
    }


    @GetMapping("/create")
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
    // TODO: @Secured("ROLE_PROJECT_EDIT")
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
    String save(@ModelAttribute Project project, Principal principal) {
        projectService.save(project, principal.getName());
        return "redirect:/projects";
    }


    @GetMapping("/delete/{id}")
    ModelAndView delete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("projects/delete");

        Project project = projectRepository.findById(id).orElse(null);
        modelAndView.addObject("project", project);

        return modelAndView;
    }

    @PostMapping("/delete")
    String delete(@ModelAttribute Project project) {
        //boolean isNew = project.getId() == null;

        projectRepository.deleteById(project.getId());

        return "redirect:/projects";


    }
}