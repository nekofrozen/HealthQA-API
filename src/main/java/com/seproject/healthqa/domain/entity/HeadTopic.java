/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seproject.healthqa.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 59050320
 */
@Entity
@Table(name = "head_topic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HeadTopic.findAll", query = "SELECT h FROM HeadTopic h")
    , @NamedQuery(name = "HeadTopic.findByHeadTopicId", query = "SELECT h FROM HeadTopic h WHERE h.headTopicId = :headTopicId")
    , @NamedQuery(name = "HeadTopic.findByTopicName", query = "SELECT h FROM HeadTopic h WHERE h.topicName = :topicName")
    , @NamedQuery(name = "HeadTopic.findByTopicText", query = "SELECT h FROM HeadTopic h WHERE h.topicText = :topicText")
    , @NamedQuery(name = "HeadTopic.findByHeight", query = "SELECT h FROM HeadTopic h WHERE h.height = :height")
    , @NamedQuery(name = "HeadTopic.findByWeight", query = "SELECT h FROM HeadTopic h WHERE h.weight = :weight")
    , @NamedQuery(name = "HeadTopic.findByAge", query = "SELECT h FROM HeadTopic h WHERE h.age = :age")
    , @NamedQuery(name = "HeadTopic.findBySex", query = "SELECT h FROM HeadTopic h WHERE h.sex = :sex")
    , @NamedQuery(name = "HeadTopic.findByDisease", query = "SELECT h FROM HeadTopic h WHERE h.disease = :disease")
    , @NamedQuery(name = "HeadTopic.findByCreatedDate", query = "SELECT h FROM HeadTopic h WHERE h.createdDate = :createdDate")
    , @NamedQuery(name = "HeadTopic.findByQuestionType", query = "SELECT h FROM HeadTopic h WHERE h.questionType = :questionType")
    , @NamedQuery(name = "HeadTopic.findByQuestionPurpose", query = "SELECT h FROM HeadTopic h WHERE h.questionPurpose = :questionPurpose")
    , @NamedQuery(name = "HeadTopic.findByIsDeleted", query = "SELECT h FROM HeadTopic h WHERE h.isDeleted = :isDeleted")
    , @NamedQuery(name = "HeadTopic.findByReportStatus", query = "SELECT h FROM HeadTopic h WHERE h.reportStatus = :reportStatus")})
public class HeadTopic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HEAD_TOPIC_ID")
    private Integer headTopicId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TOPIC_NAME")
    private String topicName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10000)
    @Column(name = "TOPIC_TEXT")
    private String topicText;
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "WEIGHT")
    private Integer weight;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AGE")
    private Float age;
    @Column(name = "SEX")
    private Character sex;
    @Size(max = 500)
    @Column(name = "DISEASE")
    private String disease;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUESTION_TYPE")
    private Character questionType;
    @Size(max = 255)
    @Column(name = "QUESTION_PURPOSE")
    private String questionPurpose;
    @Column(name = "IS_DELETED")
    private Character isDeleted;
    @Column(name = "REPORT_STATUS")
    private Character reportStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "headTopic")
    private List<Comment> commentList;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private User userId;

    public HeadTopic() {
    }

    public HeadTopic(Integer headTopicId) {
        this.headTopicId = headTopicId;
    }

    public HeadTopic(Integer headTopicId, String topicName, String topicText, Character questionType) {
        this.headTopicId = headTopicId;
        this.topicName = topicName;
        this.topicText = topicText;
        this.questionType = questionType;
    }

    public Integer getHeadTopicId() {
        return headTopicId;
    }

    public void setHeadTopicId(Integer headTopicId) {
        this.headTopicId = headTopicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicText() {
        return topicText;
    }

    public void setTopicText(String topicText) {
        this.topicText = topicText;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Float getAge() {
        return age;
    }

    public void setAge(Float age) {
        this.age = age;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Character getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Character questionType) {
        this.questionType = questionType;
    }

    public String getQuestionPurpose() {
        return questionPurpose;
    }

    public void setQuestionPurpose(String questionPurpose) {
        this.questionPurpose = questionPurpose;
    }

    public Character getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Character isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Character getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Character reportStatus) {
        this.reportStatus = reportStatus;
    }

    @XmlTransient
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (headTopicId != null ? headTopicId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HeadTopic)) {
            return false;
        }
        HeadTopic other = (HeadTopic) object;
        if ((this.headTopicId == null && other.headTopicId != null) || (this.headTopicId != null && !this.headTopicId.equals(other.headTopicId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seproject.healthqa.domain.entity.HeadTopic[ headTopicId=" + headTopicId + " ]";
    }
    
}
