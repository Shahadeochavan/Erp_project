package com.nextech.erp.controller;

import java.sql.Date;
import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productinventory;
import com.nextech.erp.model.Productinventoryhistory;
import com.nextech.erp.model.ProductionPlan;
import com.nextech.erp.model.Productionplanning;
import com.nextech.erp.model.Qualitycheckrawmaterial;
import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.model.Rawmaterialinventory;
import com.nextech.erp.model.Rawmaterialinventoryhistory;
import com.nextech.erp.model.Status;
import com.nextech.erp.service.ProductService;
import com.nextech.erp.service.ProductinventoryService;
import com.nextech.erp.service.ProductinventoryhistoryService;
import com.nextech.erp.service.ProductionplanningService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/productionplanning")
public class ProductionplanningController {


	@Autowired
	ProductionplanningService productionplanningService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	ProductinventoryService productinventoryService;
	
	@Autowired
	ProductinventoryhistoryService productinventoryhistoryService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addProductionplanning(@Valid @RequestBody Productionplanning productionplanning,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			
			productionplanningService.addEntity(productionplanning);
			return new UserStatus(1, "Productionplanning added Successfully !");
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
	@RequestMapping(value = "/createProductionForCurrentMonth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus createProductionPlanningForCurrentMonth(@Valid @RequestBody Productionplanning productionplanning,
			BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if (productionplanningService.getProductionPlanningforCurrentMonthByProductIdAndDate(
					productionplanning.getProduct().getId(),
					productionplanning.getDate())== null){
				productionplanning.setCreatedBy((int) Long.parseLong(messageSource.getMessage(ERPConstants.CREATED_BY, null, null)));
				productionplanningService
						.addEntity(productionplanning);
			}
			else{
				return new UserStatus(1,
						messageSource.getMessage(ERPConstants.PRODUCT_DATE_EXIT, null, null));
			}
			return new UserStatus(1,
					"Productrawmaterialassociation added Successfully !");
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
	public @ResponseBody Productionplanning getProductionplanning(@PathVariable("id") long id) {
		Productionplanning productionplanning = null;
		try {
			productionplanning = productionplanningService.getEntityById(Productionplanning.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productionplanning;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateProductionplanning(@RequestBody Productionplanning productionplanning) {
		try {
			productionplanningService.updateEntity(productionplanning);
			return new UserStatus(1, "Productionplanning update Successfully !");
		} catch (Exception e) {
			 e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}
	
	
	@RequestMapping(value = "/updateProductionPlan", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateProductionplanningForCurrentMonth(@RequestBody List<ProductionPlan> productionplanningList) {
		try {
			productionplanningService.updateProductionplanningForCurrentMonth(productionplanningList);
			return new UserStatus(1, "Productionplanning update Successfully !");
		} catch (Exception e) {
			 e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Productionplanning> getProductionplanning() {

		List<Productionplanning> productionplanningList = null;
		try {
			productionplanningList = productionplanningService.getEntityList(Productionplanning.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanningList;
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "getProductionplanningByMonth/{month}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Productionplanning> getProductionplanningByMonth(@PathVariable("month") Date month) {

		List<Productionplanning> productionplanningList = null;
		try {
		//	productionplanningList = productionplanningService.getEntityList(Productionplanning.class);
			productionplanningList = productionplanningService.getProductionplanningByMonth(month);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanningList;
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "getProductionPlanForCurrentMonth", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<ProductionPlan> getProductionPlanMonthYear() {

		List<ProductionPlan> productionplanningList = null;
		try {
			productionplanningList = productionplanningService.getProductionPlanForCurrentMonth();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanningList;
	}
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "updateProductionPlanMonthYear/{MONTH-YEAR}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody List<Productionplanning> updateProductionPlanMonthYear(@PathVariable("MONTH-YEAR") String month_year) {

		List<Productionplanning> productionplanningList = null;
		try {
			productionplanningList = productionplanningService.updateProductionPlanByMonthYear(month_year);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanningList;
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "createProductionPlanMonthYear/{MONTH-YEAR}", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Productionplanning> createProductionPlanMonthYear(@PathVariable("MONTH-YEAR") String month_year) {

		List<Productionplanning> productionplanningList = null;
		List<Product> productList = null;
		try {
			productList = productService.getEntityList(Product.class);
			productionplanningList = productionplanningService.createProductionPlanMonthYear( productList, month_year);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanningList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteProductionplanning(@PathVariable("id") long id) {

		try {
			Productionplanning productionplanning = productionplanningService.getEntityById(Productionplanning.class,id);
			productionplanning.setIsactive(false);
			productionplanningService.updateEntity(productionplanning);
			return new UserStatus(1, "Productionplanning deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
	
	@RequestMapping(value = "getProductionPlanDateAndPId/{date}/{pID}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Productionplanning getProductionPlanDateAndProductId(@PathVariable("date") Date date,@PathVariable("pID")long pId) {

		Productionplanning productionplanning = null;
		try {
			productionplanning = productionplanningService.getProductionplanByDateAndProductId(date,pId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanning;
	}
	
	@RequestMapping(value = "getProductionPlanDate/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Productionplanning> getProductionPlanDate(@PathVariable("date") Date date) {

		List<Productionplanning> productionplanning = null;
		try {
			productionplanning = productionplanningService.getProductionplanByDate(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return productionplanning;
	}
	
	private Productinventory updateProductInventory(Productionplanning productionplanning,Product product) throws Exception{
		Productinventory productinventory =  productinventoryService.getProductinventoryByProductId(productionplanning.getProduct().getId());
		if(productinventory == null){
			productinventory = new Productinventory();
			productinventory.setProduct(product);
			productinventory.setIsactive(true);
			productinventory.setCreatedBy((int) Long.parseLong(messageSource.getMessage(ERPConstants.CREATED_BY, null, null)));
			productinventoryService.addEntity(productinventory);
		}
		productinventory.setQuantityavailable(productionplanning.getAchivedQuantity());
		productinventory.setUpdatedDate(new Timestamp(new Date(0).getTime()));
		return productinventoryService.updateEntity(productinventory);
	}
	private void addProductInventoryHistory(Productinventory productinventory) throws Exception{
		Productinventoryhistory productinventoryhistory = new Productinventoryhistory();
		productinventoryhistory.setProductinventory(productinventory);
		productinventoryhistory.setIsactive(true);
		productinventoryhistory.setCreatedDate(new Timestamp(new Date(0).getTime()));
		productinventoryhistoryService.addEntity(productinventoryhistory);
		
	}
}
