package com.pri.controller;

import com.pri.annotation.Controller;
import com.pri.annotation.Inject;
import com.pri.annotation.WebUrl;
import com.pri.service.IndexService;

@Controller
@WebUrl("/index")
public class IndexController {

    @Inject
    private IndexService indexService;

    @WebUrl("/user")
    public String currentUser(){
        return "success:"+indexService.get();
    }

}
