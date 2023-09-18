package com.pinyougou.search.service;

import java.util.Map;

/**
 * @ClassName ItemSearchService
 * @Description TODO
 * @Author kevin_Azu
 * @Date 2019.1.15 18:33
 * @Version 1.0
 **/
public interface ItemSearchService {

    /**
     * @param searchMap 根据多条件全文检索
     * @return  后台返回给前台的搜索结果也包含多种类型
     */
    public Map search(Map searchMap);
}
