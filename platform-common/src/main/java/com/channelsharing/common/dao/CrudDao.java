package com.channelsharing.common.dao;

import java.util.List;

/**
 *
 * @author liuhangjun
 * @date 2017年2月2日
 * @param <T>
 */
public interface CrudDao<T> {


	/**
	 * 获取单条数据
	 *
	 * @param entity
	 * @return
	 */
	T findOne(T entity);

	/**
	 * 查询数据量
	 * @param entity
	 * @return
	 */
	Integer findAllCount(T entity);

	/**
	 * 分页查询数据列表
	 *
	 * @param entity
	 * @return
	 */
	List<T> findList(T entity);

	/**
	 * 插入数据
	 *
	 * @param entity
	 * @return
	 */
	Long insert(T entity);

	/**
	 * 更新数据
	 *
	 * @param entity
	 * @return
	 */
	void update(T entity);


	/**
	 * 删除数据
	 * 在生成mapper时根据表中是否有del_flag自动判断是物理删除还是逻辑删除
	 *
	 * @param entity
	 * @see public int delete(String id)
	 * @return
	 */
	void delete(T entity);


}
