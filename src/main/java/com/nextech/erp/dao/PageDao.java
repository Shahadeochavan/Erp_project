package com.nextech.erp.dao;

import java.util.List;

import com.nextech.erp.model.Page;

public interface PageDao {
	public Long addPage(Page page) throws Exception;

	public Page getPageById(long id) throws Exception;

	public List<Page> getPageList() throws Exception;

	public boolean deletePage(long id) throws Exception;

	public Page updatePage(Page page) throws Exception;
}
