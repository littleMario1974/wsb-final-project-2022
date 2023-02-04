package com.example.wsbfinalproject2022.projects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("/save")
    String save(@ModelAttribute Project project) {

        projectRepository.save(project);

        return "redirect:/projects";
    }
}
