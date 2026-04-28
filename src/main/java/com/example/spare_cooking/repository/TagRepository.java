package com.example.spare_cooking.repository;

import com.example.spare_cooking.model.table.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
