package com.project.shoply.entity;

import com.project.shoply.entity.enumerated.AuthorityName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities")
public class Authority {

    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false, unique = true)
    private AuthorityName authorityName;

    private boolean authorityDefault = false;

    public Authority(AuthorityName authorityName) {
        this.authorityName = authorityName;
    }
}
