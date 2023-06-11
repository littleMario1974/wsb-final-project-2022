package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.projects.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
public class Issue implements Comparable<Issue> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueType type = IssueType.TASK;

    @Column(nullable = false)
    @Size(min = 5, max = 20)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 7)
    private String code;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Person assignee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss[.SSS][.SS]")
    private Date dateCreated;

    @Column(nullable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss[.SSS][.SS]")
    private Date lastUpdated;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Override
    public int compareTo(Issue other) {
        return this.name.compareTo(other.name);
    }
}
