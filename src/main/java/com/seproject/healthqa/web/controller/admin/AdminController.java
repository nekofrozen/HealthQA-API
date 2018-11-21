package com.seproject.healthqa.web.controller.admin;

import com.seproject.healthqa.security.CurrentUser;
import com.seproject.healthqa.security.UserPrincipal;
import com.seproject.healthqa.service.TopicService;
import com.seproject.healthqa.web.bean.AllTopics;
import com.seproject.healthqa.web.controller.TopicController;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("admin")
public class AdminController {

    private static Logger log = Logger.getLogger("InfoLogging");

    @Autowired
    private TopicService topicService;

    @GetMapping(value = "/all")
//  @PreAuthorize("hasRole('USER')")
    public boolean getTopicsRp() {

        return true;
    }

// สร้างมอคไว้เฉย ๆ     
//    @GetMapping(value = "/delete")
////  @PreAuthorize("hasRole('USER')")
//    public boolean deleteDoctor() {
//        
//        return true;
//    }
//    
//    @GetMapping(value = "/rp")
////  @PreAuthorize("hasRole('USER')")
//    public boolean confirmDelete() {
//        
//        return true;
//    }
    @GetMapping(value = "/report-topic")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AllTopics> getUserTopic(@CurrentUser UserPrincipal currentUser) {
        return topicService.getUserTopic(currentUser);
    }

}
