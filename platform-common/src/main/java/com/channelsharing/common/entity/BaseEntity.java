package com.channelsharing.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liuhangjun on 2018/1/19.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -8648114101066903117L;

    protected Long id;
    protected Date createTime;
    protected Date updateTime;

    @JsonIgnore
    private String className = this.getClass().getSimpleName();

    @JsonIgnore
    private Integer offset = 0;

    @JsonIgnore
    private Integer limit = 10;
    
    protected Integer delFlag;

}
