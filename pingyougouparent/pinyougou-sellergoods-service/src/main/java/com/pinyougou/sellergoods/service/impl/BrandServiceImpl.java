package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


/**
 * @ClassName PageResultImpl
 * @Description TODO
 * @Author kevin_Azu
 * @Date 2018.12.22 18:45
 * @Version 1.0
 **/
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;


    /**
     * 分页
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult findPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);

        Page<TbBrand> pageResult = (Page<TbBrand>) brandMapper.selectByExample(null);

        return new PageResult(pageResult.getTotal(),pageResult.getResult());
    }

    /**
     * 新增
     * @param tbBrand
     */
    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    /**
     * 通过id查询实体类
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改
     * @param tbBrand
     */
    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     *根据一个或多个ID删除
     * @param longs
     */
    @Override
    public void delete(Long[] longs) {
        for (Long aLong : longs) {
            brandMapper.deleteByPrimaryKey(aLong);
        }
    }

    @Override
    public PageResult findPage(TbBrand tbBrand, int page, int size) {
        PageHelper.startPage(page,size);

        TbBrandExample example = new TbBrandExample();

        TbBrandExample.Criteria criteria = example.createCriteria();

        if (tbBrand!=null){
            if (tbBrand.getName()!=null && tbBrand.getName().length()>0){
                criteria.andNameLike("%"+tbBrand.getName()+"%");
            }
            if (tbBrand.getFirstChar()!=null && tbBrand.getFirstChar().length()>0){
                criteria.andFirstCharLike("%"+tbBrand.getFirstChar()+"%");
            }
        }

        Page<TbBrand> pageResults = (Page<TbBrand>) brandMapper.selectByExample(example);

        return new PageResult(pageResults.getTotal(),pageResults.getResult());
    }

    @Override
    public List<Map> selecOptionList() {
        List<Map> list = brandMapper.selectOptionList();
        return list;
    }


}
