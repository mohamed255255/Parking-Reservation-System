package com.garage_system.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class ControllerTest {

   @GetMapping("test")
   public String getMethodName( ) {
       return new StringBuilder("Hello, World!").toString();
   }
   
}
