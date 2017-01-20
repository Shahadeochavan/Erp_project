package com.nextech.erp.dao;

import java.util.List;
import com.nextech.erp.model.Vendor;

public interface VendorDao {
	public boolean addVendor(Vendor vendor) throws Exception;

	public Vendor getVendorById(long id) throws Exception;

	public List<Vendor> getVendorList() throws Exception;

	public boolean deleteVendor(long id) throws Exception;

	public Vendor updateVendor(Vendor vendor) throws Exception;
}
