package com.example.spare_cooking.service;

import com.example.spare_cooking.model.table.Tag;
import com.example.spare_cooking.repository.TagRepository;

public class TagService {
    TagRepository repository;

    public Tag createNewTag(Tag tag) {

        return repository.save(tag);
    }
}
