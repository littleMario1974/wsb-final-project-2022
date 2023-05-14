package com.example.wsbfinalproject2022.issues;

import com.example.wsbfinalproject2022.person.Person;
import com.example.wsbfinalproject2022.projects.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
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

    public String toQueryString(Integer page, Sort sort) {
        return "sort=" + toSortString(sort) +
                "&page=" + page +
                (project != null ? "&project=" + project.getId() : "") +
                (assignee != null ? "&assignee=" + assignee.getId() : "") +
                (status != null ? "&status=" + status : "") +
                (globalSearch != null ? "&globalSearch=" + globalSearch : "");
    }

    public String toSortString(Sort sort) {
        Sort.Order order = sort.getOrderFor("name");
        String sortString = "";
        if (order != null) {
            sortString += "name," + order.getDirection();
        }

        return sortString;
    }

    public Sort findNextSorting(Sort currentSorting) {
        Sort.Direction currentDirection = currentSorting.getOrderFor("name") != null ? currentSorting.getOrderFor("name").getDirection() : null;

        if (currentDirection == null) {
            return Sort.by("name").ascending();
        } else if (currentDirection.isAscending()) {
            return Sort.by("name").descending();
        } else {
            return Sort.unsorted();
        }
    }
}
