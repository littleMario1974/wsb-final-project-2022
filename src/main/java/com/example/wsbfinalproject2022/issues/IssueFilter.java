package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.projects.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueFilter {


    private Project project;

    private Person assignee;

    private Status status;

    private String globalSearch;

    public Specification<Issue> buildQuery() {
        return Specification.anyOf(
                ilike("name", globalSearch),
                ilike("description", globalSearch),
                ilike("code", globalSearch)


        ).and(
                Specification.allOf(

                        equalTo("project", project),
                        equalTo("enabled", true),
                        equalTo("assignee", assignee),
                        equalTo("status", status)
                )
        );
    }

    private Specification<Issue> equalTo(String property, Object value) {
        if (value == null) {
            return Specification.where(null);
        }

        return (root, query, builder) -> builder.equal(root.get(property), value);
    }

    private Specification<Issue> ilike(String property, String value) {
        if (value == null) {
            return Specification.where(null);
        }

        return (root, query, builder) -> builder.like(builder.lower(root.get(property)), "%" + value.toLowerCase() + "%");
    }

    public String toQueryString(Integer page) {
        return "page=" + page +
                (project != null ? "&project=" + project.getId() : "") +
                (assignee != null ? "&assignee=" + assignee.getId() : "") +
                (status != null ? "&status=" + status : "") +
                (globalSearch != null ? "&globalSearch=" + globalSearch : "");
    }

}
