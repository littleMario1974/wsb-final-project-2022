package com.example.wsbfinalproject2022.person;

import com.example.wsbfinalproject2022.authorities.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Comparable<Person> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, length = 30)
    @Size(min = 3, max = 30)
    private String username;

    @Column(length = 100)
    @Size(min = 4, max = 100)
    @Pattern(regexp = "(?=.*\\d)(?=.*[A-Z]).+", message = "{password.incorrect}")
    private String password;

    @Transient
    String repeatedPassword;

    @Size(min = 2, max = 100)
    private String userRealName;

    private Boolean enabled;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss[.SSS][.SS]")
    private Date dateCreated;

    @Email
    private String email;

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "person_authorities",
    joinColumns = @JoinColumn(name = "person_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id"))
    Set<Authority> authorities;

    public Person(String username, String password, String userRealName, Boolean enabled, String email) {
        this.username = username;
        this.password = password;
        this.userRealName = userRealName;
        this.enabled = enabled;
        this.dateCreated = new Date();
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {return repeatedPassword;}

    public void setRepeatedPassword(String password) {
        this.repeatedPassword = repeatedPassword;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(Person other) {
        return this.username.compareTo(other.username);
    }
}
