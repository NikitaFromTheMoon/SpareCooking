package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Tag;
import com.example.spare_cooking.repository.TagRepository;
import com.example.spare_cooking.service.TagService;
import org.springframework.stereotype.Controller;

@Controller
public class TagController {

    TagService service;


    public Tag createTag(Tag tag) {
        return service.createNewTag(tag);
    }
}
