package com.nextech.erp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.dto.Mail;
import com.nextech.erp.model.Bomrmvendorassociation;
import com.nextech.erp.model.Notification;
import com.nextech.erp.model.Notificationuserassociation;
import com.nextech.erp.model.Rawmaterialorder;
import com.nextech.erp.model.Rawmaterialorderinvoice;
import com.nextech.erp.model.Rawmaterialvendorassociation;
import com.nextech.erp.model.User;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.service.BOMRMVendorAssociationService;
import com.nextech.erp.service.MailService;
import com.nextech.erp.service.NotificationService;
import com.nextech.erp.service.NotificationUserAssociationService;
import com.nextech.erp.service.RMVAssoService;
import com.nextech.erp.service.RawmaterialorderService;
import com.nextech.erp.service.RawmaterialorderinvoiceService;
import com.nextech.erp.service.UserService;
import com.nextech.erp.service.VendorService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	VendorService vendorService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	UserService userService;

	@Autowired
	NotificationUserAssociationService notificationUserAssService;
	
	@Autowired
	RMVAssoService rMVAssoService;
	
	@Autowired
	RawmaterialorderService rawmaterialorderService;
	
	@Autowired
	RawmaterialorderinvoiceService rawmaterialorderinvoiceService;
	
	@Autowired
	BOMRMVendorAssociationService bOMRMVendorAssociationService;
	

	@Autowired
	MailService mailService;


	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addVendor(Model model,
			@Valid @RequestBody Vendor vendor, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				model.addAttribute("vendor", vendor);
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}

			if (vendorService.getVendorByCompanyName(vendor.getCompanyName()) == null) {

			} else {
				return new UserStatus(2, messageSource.getMessage(ERPConstants.COMPANY_NAME_EXIT, null, null));
			}
			if (vendorService.getVendorByEmail(vendor.getEmail()) == null) {
			} else {
				return new UserStatus(2,messageSource.getMessage(ERPConstants.EMAIL_ALREADY_EXIT, null, null));
			}
			vendor.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			vendor.setIsactive(true);
			vendorService.addEntity(vendor);
			mailSending(vendor, request, response);
			return new UserStatus(1, "vendor added Successfully !");
		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Vendor getVendor(@PathVariable("id") long id) {
		Vendor vendor = null;
		try {
			vendor = vendorService.getEntityById(Vendor.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vendor;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateVendor(@RequestBody Vendor vendor,HttpServletRequest request,HttpServletResponse response) {
		try {
			Vendor oldVendorInfo = vendorService.getEntityById(Vendor.class, vendor.getId());
			System.out.println(oldVendorInfo);
			if(vendor.getCompanyName().equals(oldVendorInfo.getCompanyName())){  	
			} else { 
				if (vendorService.getVendorByCompanyName(vendor.getCompanyName()) == null) {
			    }else{  
				return new UserStatus(2, messageSource.getMessage(ERPConstants.COMPANY_NAME_EXIT, null, null));
				}
			 }
            if(vendor.getEmail().equals(oldVendorInfo.getEmail())){  	
			} else { 
				if (vendorService.getVendorByEmail(vendor.getEmail()) == null) {
			    }else{  
				return new UserStatus(2, messageSource.getMessage(ERPConstants.EMAIL_ALREADY_EXIT, null, null));
				}
			 }
			vendor.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			vendor.setIsactive(true);
			vendorService.updateEntity(vendor);
			mailSendingUpdate(vendor, request, response);
			return new UserStatus(1, "Vendor update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Vendor> getVendor() {

		List<Vendor> userList = null;
		try {
			userList = vendorService.getEntityList(Vendor.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteVendor(@PathVariable("id") long id) {

		try {
			Vendor vendor = vendorService.getEntityById(Vendor.class, id);
			List<Rawmaterialvendorassociation> rawmaterialvendorassociations = rMVAssoService.getRawmaterialvendorassociationByVendorId(vendor.getId());
			for (Rawmaterialvendorassociation rawmaterialvendorassociation : rawmaterialvendorassociations) {
				rawmaterialvendorassociation.setIsactive(false);
				rMVAssoService.updateEntity(rawmaterialvendorassociation);
			}
			List<Rawmaterialorder> rawmaterialorders = rawmaterialorderService.getRMOrderByVendor(vendor.getId());
			for (Rawmaterialorder rawmaterialorder : rawmaterialorders) {
				rawmaterialorder.setIsactive(false);
				rawmaterialorderService.updateEntity(rawmaterialorder);
			}
			List<Rawmaterialorderinvoice> rawmaterialorderinvoices = rawmaterialorderinvoiceService.getRawmaterialorderinvoiceByVendorName(String.valueOf(vendor.getId()));
			for (Rawmaterialorderinvoice rawmaterialorderinvoice : rawmaterialorderinvoices) {
				rawmaterialorderinvoice.setIsactive(false);
				rawmaterialorderinvoiceService.updateEntity(rawmaterialorderinvoice);
			}
			List<Bomrmvendorassociation> bomrmvendorassociations = bOMRMVendorAssociationService.getBomRMListByVendorId(vendor.getId());
			for (Bomrmvendorassociation bomrmvendorassociation : bomrmvendorassociations) {
				bomrmvendorassociation.setIsactive(false);
				bOMRMVendorAssociationService.updateEntity(bomrmvendorassociation);
			}
			
			vendor.setIsactive(false);
			vendorService.updateEntity(vendor);
			return new UserStatus(1, "Vendor deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
	private void mailSending(Vendor vendor,HttpServletRequest request,HttpServletResponse response) throws Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationService.getEntityById(Notification.class,Long.parseLong(messageSource.getMessage(ERPConstants.VENDOR_ADDED_SUCCESSFULLY, null, null)));
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user = userService.getEmailUserById(notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				  mail.setMailTo(vendor.getEmail());
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user.getEmail());
			  }
			
		}
	        mail.setMailTo(vendor.getEmail());
	        mail.setMailSubject(notification.getSubject());
	        Map < String, Object > model = new HashMap < String, Object > ();
	        model.put("firstName", vendor.getFirstName());
	        model.put("lastName", vendor.getLastName());
	        model.put("email", vendor.getEmail());
	        model.put("contactNumber", vendor.getContactNumberMobile());
	        model.put("location", "Pune");
	        model.put("signature", "www.NextechServices.in");
	        mail.setModel(model);

		mailService.sendEmailWithoutPdF(mail, notification);
	}
	private void mailSendingUpdate(Vendor vendor,HttpServletRequest request,HttpServletResponse response) throws Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationService.getEntityById(Notification.class,Long.parseLong(messageSource.getMessage(ERPConstants.VENDOR_UPDATE_SUCCESSFULLY, null, null)));
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user = userService.getEmailUserById(notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				  mail.setMailTo(vendor.getEmail()); 
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user.getEmail());
			  }
			
		}
	        
	        mail.setMailSubject(notification.getSubject());

	        Map < String, Object > model = new HashMap < String, Object > ();
	        model.put("firstName", vendor.getFirstName());
	        model.put("lastName", vendor.getLastName());
	        model.put("email", vendor.getEmail());
	        model.put("contactNumber", vendor.getContactNumberMobile());
	        model.put("location", "Pune");
	        model.put("signature", "www.NextechServices.in");
	        mail.setModel(model);

		mailService.sendEmailWithoutPdF(mail, notification);
	}


}
