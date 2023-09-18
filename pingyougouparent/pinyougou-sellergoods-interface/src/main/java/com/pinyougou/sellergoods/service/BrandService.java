package com.pinyougou.sellergoods.service;


import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;


/**
 * 品牌接口
 * @author Administrator
 *
 */
public interface BrandService {

	
	/**
	 * 品牌分页
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(Integer pageNum, Integer pageSize);
	
	/**
	 * 增加 
	 * @param brand
	 */
	public void add(TbBrand brand);
	
	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * 修改
	 * @param brand
	 */
	public void update(TbBrand brand);
	
	
	/**
	 * 删除
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	
	/**
	 * 品牌分页
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);

	/**
	 * 商品下拉列表
	 * @return
	 */
	public List<Map> selecOptionList();
	
}
