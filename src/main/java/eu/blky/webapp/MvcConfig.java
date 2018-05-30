package eu.blky.webapp;

import org.springframework.context.annotation.*;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mycompany.MyKafkaDefaultConsumer;
 
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "eu.blky.cep.weso.ace4cep",  "com.mycompany"})
public class MvcConfig extends WebMvcConfigurerAdapter {
	
  @Bean(name = "kafkaDefaultConsumer")
  @Primary
  public MyKafkaDefaultConsumer getMultipartResolver() {
      return new MyKafkaDefaultConsumer();
  }	
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ace-builds/**").addResourceLocations("/ace-builds/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jsp");
        return bean;
    }
// 
//    @Bean(name = "multipartResolver")
//    public CommonsMultipartResolver getMultipartResolver() {
//        return new CommonsMultipartResolver();
//    }
// 
//    @Bean(name = "messageSource")
//    public ReloadableResourceBundleMessageSource getMessageSource() {
//        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
//        resource.setBasename("classpath:messages");
//        resource.setDefaultEncoding("UTF-8");
//        return resource;
//    }
 
}