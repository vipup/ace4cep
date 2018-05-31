package com.mycompany.hellokafka;
 
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.MyKafkaDefaultConsumer;
import com.mycompany.MyKafkaDefaultProducer;
 
@Controller 
@Scope("singleton")
public class KafkaController {

    @Autowired
    private MyKafkaDefaultConsumer kafkaReader;

    @Autowired
    private MyKafkaDefaultProducer kafkaWriter;    

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody String testMestod(HttpServletRequest request){
       request.getSession().setAttribute("kafkaWriter",kafkaWriter);
       request.getSession().setAttribute("kafkaReader",kafkaReader);
       return "inited";
    }    
     
    /**
    * Rest web service
    */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public @ResponseBody  boolean postToKafka(String message) {
    	System.out.println("-----------------"+message+":::"+kafkaReader+"/"+kafkaWriter);
    	try {
			kafkaWriter.send(message);
			return true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }
}