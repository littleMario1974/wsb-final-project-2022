package com.example.wsbfinalproject2022.projects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // todo: @Secured("ROLE_PROJECTS_TAB")
    @GetMapping
    ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("projects/index");

        modelAndView.addObject("projects", projectRepository.findAll());
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
    String save(@ModelAttribute Project project) {
        boolean isNew = project.getId() == null;

        projectRepository.save(project);

        if (isNew) {
            return "redirect:/projects";
        } else {
            return "redirect:/projects/edit/" + project.getId();
        }
    }
}