package com.lujia;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>Title: ${title}</h3>
 * <p>Description: </p>
 *
 * @author : lujia
 * @date : 2021-05-12
 **/
@RestController
public class TestController {

    @GetMapping("/test")
    @PreAuthorize(value = "hasRole('admin')")
    public String test() {

        System.out.println("test----");
        return "lujia";
    }
}
