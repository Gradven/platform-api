package com.channelsharing.common.service;


import com.channelsharing.common.entity.Paging;

/**
 *
 * @author liuhangjun
 *
 */
public interface CrudService<T> {
	
	T findOne(Long id);
	
	T findOne(T entity);

	Paging<T> findPaging(T entity);

	void add(T entity);

	T addWithResult(T entity);

	void modify(T entity);

	void delete(T entity);

}
