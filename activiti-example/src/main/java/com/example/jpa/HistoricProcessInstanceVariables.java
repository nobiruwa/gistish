package com.example.jpa;

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

    @Column(name="DOUBLE_")
    private double double_;

    @Column(name="LONG_")
    private long long_;

    @Column(name="TEXT_")
    private String text;

    @Column(name="TEXT2_")
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
}
