package com.project.shoply.repository;

import com.project.shoply.entity.Brand;
import com.project.shoply.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, long id);

}
