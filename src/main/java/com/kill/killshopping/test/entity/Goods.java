package com.kill.killshopping.test.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Date 2019/12/12 13:46
 */
@Data
public class Goods implements Serializable {
    private Integer cid;
    private String name;
    private Integer is_parent;
    private Integer parent_id;
    private Integer level;
    private String pathid;
    private String path;
    private Timestamp timestamp;
}
