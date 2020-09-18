package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DocsController{

//    @RequestMapping("/test1")
//    private String test1(Model model) {
//        return "html/test";
//    }
//
//    @RequestMapping("/test2")
//    private String test2(Model model) {
//        return "jsp/test";
//    }
//
    @RequestMapping(value="/jsp/{pathName}")
    String getStaticJsp(@PathVariable String pathName){
        System.out.println("jsp: " + pathName);
        return "jsp/" + pathName;
    }

    @RequestMapping(value="/html/{pathName}")
    String getStaticHtml(@PathVariable String pathName){
        System.out.println("html: " + pathName);
        return pathName;
    }
}
