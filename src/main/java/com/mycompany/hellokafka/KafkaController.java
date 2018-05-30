package com.mycompany.hellokafka;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.MyKafkaDefaultConsumer;
 
@Controller
public class KafkaController {
 
    @Autowired
    private MyKafkaDefaultConsumer kafka;
  
    /**
    * Request mapping for user
    */
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView getUsersView() {
        ModelAndView mv= new ModelAndView("usersView");
//        mv.addObject("usersModel", userService.findAll());
        return mv;
    }
     
    /**
    * Rest web service
    */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public @ResponseBody  boolean postToKafka(String message) {
    	System.out.println("-----------------"+message+":::"+kafka);
        return true;
    }
}