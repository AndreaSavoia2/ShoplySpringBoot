package com.project.shoply.entity;

import com.project.shoply.entity.common.CreationUpdate;
import com.project.shoply.entity.enumerated.ReviewCondition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews_status")
public class ReviewStatus extends CreationUpdate {

    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false, unique = true)
    private ReviewCondition reviewCondition;

    private boolean reviewStatusDefault = false;
}
