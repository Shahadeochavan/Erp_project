package com.nextech.erp.controller;

import java.util.ArrayList;
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
import com.nextech.erp.dto.ProductNewAssoicatedList;
import com.nextech.erp.model.Bom;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productinventory;
import com.nextech.erp.model.Productionplanning;
import com.nextech.erp.model.Productorderassociation;
import com.nextech.erp.model.Productrawmaterialassociation;
import com.nextech.erp.service.BomService;
import com.nextech.erp.service.ProductRMAssoService;
import com.nextech.erp.service.ProductService;
import com.nextech.erp.service.ProductinventoryService;
import com.nextech.erp.service.ProductionplanningService;
import com.nextech.erp.service.ProductorderassociationService;
import com.nextech.erp.status.Response;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	ProductinventoryService productinventoryService;

	@Autowired
	ProductRMAssoService productRMAssoService;
	
	@Autowired
	BomService bomService;
	
	@Autowired
	ProductorderassociationService productorderassociationService;
	
	@Autowired 
	ProductionplanningService productionplanningService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addProduct(
			@Valid @RequestBody Product product, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if (productService.getProductByName(product.getName()) == null) {

			} else {
				return new UserStatus(0, messageSource.getMessage(ERPConstants.PRODUCT_NAME, null, null));
			}
			if (productService.getProductByPartNumber(product.getPartNumber()) == null) {
			} else {
				return new UserStatus(0, messageSource.getMessage(ERPConstants.PART_NUMBER, null, null));
			}
			product.setIsactive(true);
			product.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			productService.addEntity(product);
			addProductInventory(product, Long.parseLong(request.getAttribute("current_user").toString()));
			return new UserStatus(1, "product added Successfully !");
		} catch (ConstraintViolationException cve) {
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
	public @ResponseBody Product getProduct(@PathVariable("id") long id) {
		Product product = null;
		try {
			product = productService.getEntityById(Product.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateProduct(@RequestBody Product product,HttpServletRequest request,HttpServletResponse response) {
		try {
			Product oldProductInfo = productService.getEntityById(Product.class, product.getId());
			if(product.getName().equals(oldProductInfo.getName())){ 	
				} else { 
					if (productService.getProductByName(product.getName()) == null) {
				    }else{  
				    	return new UserStatus(0, messageSource.getMessage(ERPConstants.PRODUCT_NAME, null, null));
					}
				 }
	            if(product.getPartNumber().equals(oldProductInfo.getPartNumber())){  			
				} else { 
					if (productService.getProductByPartNumber(product.getPartNumber()) == null) {
				    }else{  
				    	return new UserStatus(0, messageSource.getMessage(ERPConstants.PART_NUMBER, null, null));
					}
				 }
			product.setIsactive(true);
			product.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			productService.updateEntity(product);
			return new UserStatus(1, "Product update Successfully !");
		} catch (Exception e) {
			 e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Product> getProduct() {

		List<Product> ProductList = null;
		try {
			ProductList = productService.getEntityList(Product.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ProductList;
	}
	@RequestMapping(value = "/list/newProductRMAssociation", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Response getNewProductRMAsso() {

		List<Product> productList = null;
		List<ProductNewAssoicatedList> productNewAssoicatedLists = new ArrayList<ProductNewAssoicatedList>();
		try {
			productList = productService.getEntityList(Product.class);
			for(Product product : productList){
				List<Productrawmaterialassociation> productrawmaterialassociations = productRMAssoService.getProductRMAssoListByProductId(product.getId());
				if(productrawmaterialassociations==null){
					ProductNewAssoicatedList productNewAssoicatedList = new ProductNewAssoicatedList();
					productNewAssoicatedList.setId(product.getId());
					productNewAssoicatedList.setPartNumber(product.getPartNumber());
					productNewAssoicatedLists.add(productNewAssoicatedList);
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Response(1,"New Productrawmaterialassociation List",productNewAssoicatedLists);
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteProduct(@PathVariable("id") long id) {

		try {
			Product product = productService.getEntityById(Product.class,id);
			List<Productorderassociation> productorderassociations = productorderassociationService.getProductorderassociationByProdcutId(product.getId());
			for (Productorderassociation productorderassociation : productorderassociations) {
				productorderassociation.setIsactive(false);
				productorderassociationService.updateEntity(productorderassociation);
				}
			List<Productionplanning> productionplannings = productionplanningService.getProductionplanListByProductId(product.getId());
			for (Productionplanning productionplanning : productionplannings) {
				productionplanning.setIsactive(false);
				productionplanningService.updateEntity(productionplanning);
			}
			
			Productrawmaterialassociation productrawmaterialassociation = productRMAssoService.getProductRMListByProductId(product.getId());
			productrawmaterialassociation.setIsactive(false);
			productRMAssoService.updateEntity(productrawmaterialassociation);
			
		    Productinventory productinventory = productinventoryService.getProductinventoryByProductId(product.getId());
		    productinventory.setIsactive(false);
		    productinventoryService.updateEntity(productinventory);
		     
		    Bom bom  = bomService.getBomByProductId(product.getId());
		    bom.setIsactive(false);
		    bomService.updateEntity(bom);
		    		
			product.setIsactive(false);
			productService.updateEntity(product);
			return new UserStatus(1, "Product deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}

	private void addProductInventory(Product product,long userId) throws Exception{
		Productinventory productinventory = new Productinventory();
		productinventory.setProduct(product);
		productinventory.setQuantityavailable(0);
		productinventory.setCreatedBy(userId);
		productinventory.setIsactive(true);
		productinventoryService.addEntity(productinventory);
	}
}
