package com.example.wsbfinalproject2022.projects;

import com.example.wsbfinalproject2022.person.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
public class Project implements Comparable<Project> {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, max = 30)
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 7)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss[.SSS][.SS]")
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable =false)
    private Person creator;

    @Override
    public int compareTo(Project other) {
        return this.name.compareTo(other.name);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
