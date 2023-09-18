package com.pinyougou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageResult
 * @Description TODO
 * @Author kevin_Azu
 * @Date 2018.12.22 18:40
 * @Version 1.0
 **/
public class PageResult implements Serializable{

    private long total;//总记录数
    private List rows;//当前页展示的数据

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
