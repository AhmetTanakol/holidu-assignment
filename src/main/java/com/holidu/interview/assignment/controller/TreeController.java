package com.holidu.interview.assignment.controller;

import org.springframework.web.bind.annotation.RestController;
import com.holidu.interview.assignment.model.Tree;
import com.holidu.interview.assignment.repository.TreeRepository;

@RestController
public class TreeController {
	private final TreeRepository repository;
	
	TreeController(TreeRepository repository) {
		this.repository = repository;
	}
}
