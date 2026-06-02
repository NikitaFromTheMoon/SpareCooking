package com.example.spare_cooking.repository;

import com.example.spare_cooking.model.table.Tag;
import com.example.spare_cooking.model.table.WebUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findAll(Pageable pageable);
}
