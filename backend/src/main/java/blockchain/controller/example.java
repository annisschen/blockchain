package blockchain.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class example {
    @RequestMapping("/home")
    public String hello(){
        return "hello, 陈伟豪98:)";
    }
}
