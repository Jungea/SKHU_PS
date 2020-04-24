package net.skhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.Common;
import net.skhu.repository.CommonRepository;

@RestController
@RequestMapping("api")
public class APIController {
	@Autowired
	CommonRepository commonRepository;
	
	@RequestMapping("studentSignUp")
	public List<Common> commons() {
		return commonRepository.findAll();
	}

}
