package com.nextech.erp.controller;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.model.Client;
import com.nextech.erp.service.ClientService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/client")
public class ClientController {

	
	@Autowired
	ClientService clientService;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addClient(
			@Valid @RequestBody Client client, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if (clientService.getClientByCompanyName(client.getCompanyname()) == null) {

			} else {
				return new UserStatus(1, messageSource.getMessage(ERPConstants.COMPANY_NAME_EXIT, null, null));
				
			}
			if (clientService.getClientByEmail(client.getEmailid()) == null) {
			} else {
				return new UserStatus(1, messageSource.getMessage(ERPConstants.EMAIL_ALREADY_EXIT, null, null));
			}
			client.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			client.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			client.setIsactive(true);
			clientService.addEntity(client);
			return new UserStatus(1, messageSource.getMessage(ERPConstants.CLIENT_ADDED, null, null));
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
	public @ResponseBody Client getClient(@PathVariable("id") long id) {
		Client client = null;
		try {
			client = clientService.getEntityById(Client.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateClient(@RequestBody Client client) {
		try {
			clientService.updateEntity(client);
			return new UserStatus(1, messageSource.getMessage(ERPConstants.CLIENT_UPDATE, null, null));
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Client> getClient() {

		List<Client> clientList = null;
		try {
			clientList = clientService.getEntityList(Client.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteClient(@PathVariable("id") long id) {

		try {
			Client client = clientService.getEntityById(Client.class,id);
			client.setIsactive(false);
			clientService.updateEntity(client);
			return new UserStatus(1,messageSource.getMessage(ERPConstants.CLIENT_DELETE, null, null));
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
}
