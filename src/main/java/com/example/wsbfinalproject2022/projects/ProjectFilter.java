package com.example.wsbfinalproject2022.projects;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFilter {

private String name;


    public Specification<Project> buildQuery() {

return (root, query, builder) -> builder.equal(root.get("name"), name);
    }
}
