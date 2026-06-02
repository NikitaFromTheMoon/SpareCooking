package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Tag;
import com.example.spare_cooking.model.table.WebUser;
import com.example.spare_cooking.repository.TagRepository;
import com.example.spare_cooking.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    TagService service;

    @GetMapping("/stats")
    public List<Tag> getTags() {
        //Pageable pageable = ;
        return service.getAllTags();
    }
}
