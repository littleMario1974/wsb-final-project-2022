package com.example.wsbfinalproject2022.authorities;

import jakarta.persistence.*;

@Entity
public class Authority {

    @Id
    @GeneratedValue
    Long Id;


    @Column(nullable = false, unique = true)
            @Enumerated(EnumType.STRING)
    AuthorityName authority;

    public Authority(AuthorityName authority) {
        this.authority = authority;
    }

    public Authority() {

    }

    public Long getId() {
        return Id;
    }

    public AuthorityName getAuthority() {
        return authority;
    }
}
