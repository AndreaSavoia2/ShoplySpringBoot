package com.project.shoply.entity;

import com.project.shoply.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "products")
public class Product extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 65535)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 100)
    private String material;

    @Column(length = 50)
    private String color;

    @Column(precision = 6, scale = 2)
    private BigDecimal width;

    @Column(precision = 6, scale = 2)
    private BigDecimal height;

    @Column(precision = 6, scale = 2)
    private BigDecimal depth;

    @Column
    private Integer stock;

    @Column(length = 50)
    private String imageUrl;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public Product(BigDecimal price, String name) {
        this.price = price;
        this.name = name;
    }

    public Product(String name, String description,
                   BigDecimal price, String material,
                   String color, BigDecimal width,
                   BigDecimal height, BigDecimal depth,
                   Integer stock, String imageUrl,
                   Category category, Brand brand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.material = material;
        this.color = color;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
        this.brand = brand;
    }
}
