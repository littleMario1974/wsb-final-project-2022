package com.example.wsbfinalproject2022.projects;

import com.example.wsbfinalproject2022.person.Person;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
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

}
