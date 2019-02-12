package com.yhx.manage.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_pid")
public class UserPid {
    private String pid;

    /**
     * 记录版本号
     */
    private Integer version;

    /**
     * 状态：0不可用 1可用
     */
    private Integer status;

    /**
     * 类型:1京东2拼多多
     */
    private Integer type;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * 获取记录版本号
     *
     * @return version - 记录版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置记录版本号
     *
     * @param version 记录版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取状态：0不可用 1可用
     *
     * @return status - 状态：0不可用 1可用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0不可用 1可用
     *
     * @param status 状态：0不可用 1可用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取类型:1京东2拼多多
     *
     * @return type - 类型:1京东2拼多多
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型:1京东2拼多多
     *
     * @param type 类型:1京东2拼多多
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}