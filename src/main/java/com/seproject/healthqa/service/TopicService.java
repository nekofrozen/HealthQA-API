package com.seproject.healthqa.service;

import com.seproject.healthqa.domain.entity.Comment;
import com.seproject.healthqa.domain.entity.HeadTopic;
import com.seproject.healthqa.domain.entity.Users;
import com.seproject.healthqa.domain.repository.CommentRepository;
import com.seproject.healthqa.exception.CustomException;
import com.seproject.healthqa.exception.ResourceNotFoundException;
import com.seproject.healthqa.security.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import com.seproject.healthqa.web.bean.Topic;
import com.seproject.healthqa.web.bean.AllTopics;
import com.seproject.healthqa.web.bean.Comments;
import com.seproject.healthqa.web.bean.Profile;
import java.sql.Timestamp;

import java.util.Date;
import java.util.Optional;
import javassist.tools.web.BadHttpRequest;
import javax.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import com.seproject.healthqa.domain.repository.HeadTopicRepository;
import com.seproject.healthqa.web.bean.ReportTopicResponse;

@Service
public class TopicService {

    private static Logger log = Logger.getLogger("InfoLogging");

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HeadTopicRepository topicRepository;
    @Autowired
    CommentRepository commentRepository;

    public ResponseEntity<?> getTopic(int id_topic) {
        StringBuffer queryStr = new StringBuffer("SELECT HD.HEAD_TOPIC_ID, HD.TOPIC_NAME, HD.TOPIC_TEXT, HD.WEIGHT, HD.HEIGHT, HD.AGE_Y, HD.AGE_M, HD.AGE_D,"
                + "		HD.SEX, HD.DISEASE, QUESTION_PURPOSE, HD.QUESTION_TYPE, USERS.username, CREATED_DATE, "
                + "        (SELECT COUNT(*) FROM comment WHERE HEAD_TOPIC_ID=HD.HEAD_TOPIC_ID AND IS_DELETED='F')\n"
                + "FROM head_topic HD LEFT JOIN users ON(users.id = HD.USER_ID)\n"
                + "WHERE (HD.HEAD_TOPIC_ID = " + id_topic + ") AND (HD.IS_DELETED = 'F')");
        Topic topic = new Topic();

        Query query = entityManager.createNativeQuery(queryStr.toString());
        List<Object[]> objectList = query.getResultList();

        if (objectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomException(new Timestamp(System.currentTimeMillis()), 404, "Not Found", "Topic Not Found"));
        }

        for (Object[] obj : objectList) {
            topic.setTopicId(obj[0].toString());
            topic.setTopicName(obj[1].toString());
            topic.setTopicText(obj[2].toString());
            topic.setWeight(Integer.parseInt(obj[3].toString()));
            topic.setHeight(Integer.parseInt(obj[4].toString()));
            topic.setAgeY(Integer.parseInt(obj[5].toString()));
            topic.setAgeM(Integer.parseInt(obj[6].toString()));
            topic.setAgeD(Integer.parseInt(obj[7].toString()));

            if ((obj[8].toString()).equals("M")) {
                topic.setGender("ชาย");
            } else {
                topic.setGender("หญิง");
            }

            topic.setDisease(obj[9].toString());
            topic.setQuestionPurpose(obj[10].toString());

            if ((obj[11].toString()).equals("D")) {
                topic.setQuestionType("คำถามเฉพาะทางแพทย์");
            } else {
                topic.setQuestionType("คำถามเฉพาะทางเภสัชกร");
            }

            topic.setName(obj[12].toString());
            topic.setCreateDate((java.sql.Timestamp) obj[13]);
            topic.setAnswerCount(obj[14].toString());
            topic.setComments(getComment(id_topic));
        }
        return ResponseEntity.ok(topic);

    }

    public List<Comments> getComment(int id_topic) {
        StringBuffer queryStr = new StringBuffer("SELECT COMMENT_ID,COMMENT_TEXT,CREATED_DATE, users.username,"
                + "		(SELECT authority.name"
                + "              FROM user_authority INNER JOIN authority ON(user_authority.authority_id=authority.id)"
                + "              WHERE users.id = user_authority.user_id)"
                + " FROM comment INNER JOIN users ON (comment.USER_ID=users.id)"
                + " WHERE (comment.HEAD_TOPIC_ID = " + id_topic + ") AND (comment.IS_DELETED = 'F')"
                + " ORDER BY CREATED_DATE");
        List<Comments> BeanList = new ArrayList<Comments>();
        Query query = entityManager.createNativeQuery(queryStr.toString());
        List<Object[]> objectList = query.getResultList();

        for (Object[] obj : objectList) {
            Comments Bean = new Comments();
            Bean.setCommentId(obj[0].toString());
            Bean.setCommentText(obj[1].toString());
//                Bean.setCreateDate(new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(obj[2].toString())));
            Bean.setCreateDate((Date) obj[2]);
            Bean.setName(obj[3].toString());
            Bean.setUserType(obj[4].toString());
            BeanList.add(Bean);

        }
        return BeanList;
    }

    public HeadTopic createTopic(HeadTopic headTopic, UserPrincipal currentUser) {
        Users user = new Users();
        user.setId(currentUser.getId());

        headTopic.setUserId(user);
        headTopic.setIsDeleted('F');
        headTopic.setReportStatus('F');
        return topicRepository.save(headTopic);
    }

    public List<AllTopics> getUserTopic(UserPrincipal currentUser) {
        StringBuffer queryStr = new StringBuffer("SELECT HD.HEAD_TOPIC_ID as ID ,HD.TOPIC_NAME,HD.TOPIC_TEXT,HD.QUESTION_TYPE "
                + " , (SELECT COUNT(*) FROM comment WHERE HEAD_TOPIC_ID=ID AND IS_DELETED='F') as commentCount "
                + " FROM head_topic HD WHERE HD.IS_DELETED = 'F' AND USER_ID = " + currentUser.getId()
                + " ORDER BY CREATED_DATE DESC");
        List<AllTopics> BeanList = new ArrayList<AllTopics>();
        Query query = entityManager.createNativeQuery(queryStr.toString());
        List<Object[]> objectList = query.getResultList();

        for (Object[] obj : objectList) {
            AllTopics Bean = new AllTopics();
            Bean.setTopicId(Integer.parseInt(obj[0].toString()));
            Bean.setTopicName(obj[1].toString());
            Bean.setTopicText(obj[2].toString());
//            Bean.setQuestion_type(obj[3].toString());
            if ((obj[3].toString()).equals("D")) {
                Bean.setQuestionType("คำถามเฉพาะทางแพทย์");
            } else {
                Bean.setQuestionType("คำถามเฉพาะทางเภสัชกร");
            }

            Bean.setAnswerCount(Integer.parseInt(obj[4].toString()));

            BeanList.add(Bean);
        }
        return BeanList;
    }

    public List<AllTopics> getUserHasComment(UserPrincipal currentUser) {

        StringBuffer queryStr = new StringBuffer(" SELECT HD.HEAD_TOPIC_ID as ID ,HD.TOPIC_NAME,HD.TOPIC_TEXT,HD.QUESTION_TYPE,\n"
                + " (SELECT COUNT(*) FROM comment WHERE HEAD_TOPIC_ID=HD.HEAD_TOPIC_ID AND IS_DELETED='F') as commenntCount \n"
                + " FROM head_topic HD \n"
                + " WHERE HD.IS_DELETED = 'F' AND(SELECT COUNT(*) FROM comment WHERE IS_DELETED='F' AND HEAD_TOPIC_ID = HD.HEAD_TOPIC_ID AND comment.USER_ID=" + currentUser.getId() + ") > 0 \n"
                + " ORDER BY CREATED_DATE DESC ");
        List<AllTopics> BeanList = new ArrayList<AllTopics>();
        Query query = entityManager.createNativeQuery(queryStr.toString());
        List<Object[]> objectList = query.getResultList();

        for (Object[] obj : objectList) {
            AllTopics Bean = new AllTopics();
            Bean.setTopicId(Integer.parseInt(obj[0].toString()));
            Bean.setTopicName(obj[1].toString());
            Bean.setTopicText(obj[2].toString());
            if ((obj[3].toString()).equals("D")) {
                Bean.setQuestionType("คำถามเฉพาะทางแพทย์");
            } else {
                Bean.setQuestionType("คำถามเฉพาะทางเภสัชกร");
            }

            Bean.setAnswerCount(obj[4].toString().isEmpty() ? 0 : Integer.parseInt(obj[4].toString()));

            BeanList.add(Bean);
        }
        return BeanList;
    }

    public Optional<HeadTopic> reportTopic(Integer id) {
        Optional<HeadTopic> headTopic = topicRepository.findById(id);
//        if (topicRepository.existsByHeadTopicId(id)) {

        if (!headTopic.isPresent()) {
            return headTopic;
        }

        HeadTopic topic = headTopic.get();
        topic.setReportStatus('T');

        return Optional.of(topicRepository.save(topic));

    }

    public Optional<HeadTopic> deleteTopic(Integer id) {
        Optional<HeadTopic> headTopic = topicRepository.findById(id);
//        if (topicRepository.existsByHeadTopicId(id)) {

        if (!headTopic.isPresent()) {
            return headTopic;
        }

        HeadTopic topic = headTopic.get();
        topic.setIsDeleted('T');

        return Optional.of(topicRepository.save(topic));

    }

    public Optional<HeadTopic> cancelReportTopic(Integer id) {
        Optional<HeadTopic> headTopic = topicRepository.findById(id);
//        if (topicRepository.existsByHeadTopicId(id)) {

        if (!headTopic.isPresent()) {
            return headTopic;
        }

        HeadTopic topic = headTopic.get();
        topic.setReportStatus('F');

        return Optional.of(topicRepository.save(topic));

    }

    public List<ReportTopicResponse> getReportTopic(UserPrincipal currentUser) {
        StringBuffer queryStr = new StringBuffer("SELECT HD.HEAD_TOPIC_ID as ID ,HD.TOPIC_NAME,HD.TOPIC_TEXT"
                + " FROM head_topic HD WHERE HD.IS_DELETED = 'F' AND HD.REPORT_STATUS = 'T'"
                + " ORDER BY CREATED_DATE DESC");

        List<ReportTopicResponse> BeanList = new ArrayList<ReportTopicResponse>();

        Query query = entityManager.createNativeQuery(queryStr.toString());
        List<Object[]> objectList = query.getResultList();

        for (Object[] obj : objectList) {
            ReportTopicResponse Bean = new ReportTopicResponse();
            Bean.setId(Long.parseLong(obj[0].toString()));
            Bean.setTopicName(obj[1].toString());
            Bean.setTopicText(obj[2].toString());
//            Bean.setQuestion_type(obj[3].toString());
            BeanList.add(Bean);
        }
        return BeanList;
    }

    public List<ReportTopicResponse> getReportComment(UserPrincipal currentUser) {
        StringBuffer queryStr = new StringBuffer("SELECT HD.HEAD_TOPIC_ID as ID ,HD.TOPIC_NAME,HD.TOPIC_TEXT"
                + " FROM head_topic HD WHERE HD.IS_DELETED = 'F' AND HD.REPORT_STATUS = 'T'"
                + " ORDER BY CREATED_DATE DESC");

        List<ReportTopicResponse> BeanList = new ArrayList<ReportTopicResponse>();

        Query query = entityManager.createNativeQuery(queryStr.toString());
        List<Object[]> objectList = query.getResultList();

        for (Object[] obj : objectList) {
            ReportTopicResponse Bean = new ReportTopicResponse();
            Bean.setId(Long.parseLong(obj[0].toString()));
            Bean.setTopicName(obj[1].toString());
            Bean.setTopicText(obj[2].toString());
//            Bean.setQuestion_type(obj[3].toString());
            BeanList.add(Bean);
        }
        return BeanList;
    }
}
