package com.nextech.erp.controller;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nextech.erp.model.Statustransition;
import com.nextech.erp.service.StatustransitionService;
import com.nextech.erp.status.UserStatus;

@RestController
@RequestMapping("/statustransition")
public class StatustransitionController {

	@Autowired
	StatustransitionService statustransitionService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addStatustransition(
			@Valid @RequestBody Statustransition statustransition, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			statustransitionService.addStatustransition(statustransition);
			return new UserStatus(1, "Statustransition added Successfully !");
		} catch (ConstraintViolationException cve) {
			System.out.println("Inside ConstraintViolationException");
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			System.out.println("Inside PersistenceException");
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Statustransition getStatustransition(@PathVariable("id") long id) {
		Statustransition statustransition = null;
		try {
			statustransition = statustransitionService.getStatustransitionById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statustransition;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateStatustransition(@RequestBody Statustransition statustransition) {
		try {
			statustransitionService.updateStatustransition(statustransition);
			return new UserStatus(1, "Statustransition update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Statustransition> getStatustransition() {

		List<Statustransition> statustransitionList = null;
		try {
			statustransitionList = statustransitionService.getStatustransitionList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statustransitionList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteStatustransition(@PathVariable("id") long id) {

		try {
			Statustransition statustransition = statustransitionService.getStatustransitionById(id);
			statustransition.setIsactive(false);
			statustransitionService.updateStatustransition(statustransition);
			return new UserStatus(1, "Statustransition deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
}

