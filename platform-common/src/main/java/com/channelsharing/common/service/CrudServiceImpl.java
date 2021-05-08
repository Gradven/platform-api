package com.channelsharing.common.service;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.common.entity.BaseEntity;
import com.channelsharing.common.entity.Paging;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author liuhangjun
 * @date 2017年2月4日
 * @param <D>
 * @param <T>
 */
public abstract class CrudServiceImpl<D extends CrudDao<T>, T extends BaseEntity> implements CrudService<T> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	//@Autowired 默认按照type注入，@Resource默认按照name注入，此处类型不固定，名称是固定的，应该按照类型注入
	@Autowired
	public D dao;

    public T findOne(@NonNull T entity){
        return dao.findOne(entity);
    }

	public Paging<T> findPaging(@NonNull T entity) {
		Paging<T> paging = new Paging<>();
		paging.setCount(dao.findAllCount(entity));
		paging.setRows(dao.findList(entity));

		return paging;
	}

	public void add(@NonNull T entity){
		dao.insert(entity);

	}

    public T addWithResult(T entity){
		dao.insert(entity);

        return this.findOne(entity);
    }

	public void modify(@NonNull T entity){
		dao.update(entity);
	}


	public void delete(@NonNull T entity){
		dao.delete(entity);
	}
}
