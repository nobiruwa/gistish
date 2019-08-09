package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Date;

import javax.persistence.Column;

@Entity
@Table(name="ACT_HI_VARINST")
public class HistoricProcessInstanceVariables {
    @Id
    @Column(name="ID_")
    private String id;

    @Column(name="PROC_INST_ID_")
    private String procInstId;

    @Column(name="EXECUTION_ID_")
    private String executionId;

    @Column(name="TASK_ID_")
    private String taskId;

    @Column(name="NAME_")
    private String name;

    @Column(name="VAR_TYPE_")
    private String varType;

    @Column(name="REV_")
    private int  rev;

    @Column(name="BYTEARRAY_ID_")
    private String bytearrayId;

    @Column(name="DOUBLE_", nullable=true)
    private Double double_;

    @Column(name="LONG_", nullable=true)
    private Long long_;

    @Column(name="TEXT_", nullable=true)
    private String text;

    @Column(name="TEXT2_", nullable=true)
    private String text2;

    @Column(name="CREATE_TIME_")
    private Date createTime;

    @Column(name="LAST_UPDATED_TIME_")
    private Date lastUpdatedTime;

    protected HistoricProcessInstanceVariables() {}

    public HistoricProcessInstanceVariables(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getLong_() {
        return long_;
    }

    public void setLong_(long long_) {
        this.long_ = long_;
    }

    public double getDouble_() {
        return double_;
    }

    public void setDouble_(double double_) {
        this.double_ = double_;
    }

    public String getBytearrayId() {
        return bytearrayId;
    }

    public void setBytearrayId(String bytearrayId) {
        this.bytearrayId = bytearrayId;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public String getVarType() {
        return varType;
    }

    public void setVarType(String varType) {
        this.varType = varType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
