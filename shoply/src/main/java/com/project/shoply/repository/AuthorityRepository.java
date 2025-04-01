package com.project.shoply.repository;

import com.project.shoply.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthorityDefaultTrue();
}
