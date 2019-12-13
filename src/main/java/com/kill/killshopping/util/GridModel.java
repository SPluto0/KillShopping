package com.kill.killshopping.util;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Date 2019/12/13 10:06
 */
@Data
public class GridModel<T> implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7252085778810679755L;

    private Integer page;
    //总记录数
    private Integer records;
    //总页数
    private Integer total = 0;

    private List<T> rows;

    public GridModel() {

    }

    public GridModel(PageList<T> pageList) {
        this.page = pageList.getPaginator().getPage();
        this.records = pageList.getPaginator().getTotalCount();
        this.total = pageList.getPaginator().getTotalPages();
        this.rows = pageList;
    }
}
