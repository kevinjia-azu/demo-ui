package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ItemSearchServiceImpl
 * @Description TODO
 * @Author kevin_Azu
 * @Date 2019.1.15 18:40
 * @Version 1.0
 **/
@Service(timeout=5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
        //1.根据用户输入的查询信息查询商品列表，并返回
        map.putAll(searchList(searchMap));//putAll方法将原有map中的数据追加到当前map集合中
        //2.根据用户输入的查询信息查询分组列表，并返回
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);
        //3.根据分类查询品牌和规格
        Map brandAndSpecList = searchBrandAndSpecList(categoryList.get(0));
        map.putAll(brandAndSpecList);//默认显示第一个分类的数据
        return map;
    }

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据分类查询品牌和规格
     * @return
     */
    private Map searchBrandAndSpecList(String category){
        Map map = new HashMap();
        //从缓存中根据分类名称取出模板id
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if(templateId != null){
            //从缓存中根据模板id取出品牌列表列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList",brandList);//存入map集合中

            //从缓存中根据模板id取出规格及选项列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList",specList);
        }
        return map;
    }

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * @param searchMap 根据多条件全文检索
     * @return
     */
    private Map searchList(Map searchMap) {

        Map map  = new HashMap();
        HighlightQuery query = new SimpleHighlightQuery();

        //设置高亮域
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");

        //前缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");

        //后缀
        highlightOptions.setSimplePostfix("</em>");

        query.setHighlightOptions(highlightOptions);

        //1.1 关键字查询
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2 按商品分类过滤
        if(!"".equals(searchMap.get("category"))  )	{//如果用户选择了分类
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.3 按品牌过滤
        if(!"".equals(searchMap.get("brand"))  )	{//如果用户选择了品牌
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.4 按规格过滤
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map<String, String>) searchMap.get("spec");
            for(String key :specMap.keySet()){

                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key)  );
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);

            }

        }

        //获取高亮结果集
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();

        for (HighlightEntry<TbItem> entry : entryList) {
            //获取高亮域列表（高亮域的个数）
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();
            if(highlights.size()>0 && (highlights.get(0).getSnipplets().size())>0) {
                TbItem item = entry.getEntity();
                item.setTitle(highlights.get(0).getSnipplets().get(0));
            }
        }

        map.put("rows",page.getContent());

        return map;
    }

    //查询商品分组列表
    private List<String> searchCategoryList(Map searchMap){
        List<String> list = new ArrayList<>();
        //1.关键字查询
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //2.设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //3.获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //4.获取分组结果
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //5.获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //6.获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
        for (GroupEntry<TbItem> entry : entryList) {
            list.add(entry.getGroupValue());//将分组结果添加到list集合中
        }
        return list;
    }

}
