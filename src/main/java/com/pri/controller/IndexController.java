package com.pri.controller;

import com.pri.annotation.*;
import com.pri.constant.HttpMethod;
import com.pri.service.IndexService;

@Controller
@WebUrl("/index")
public class IndexController {

    @Inject
    private IndexService indexService;

    @HttpLog
    @WebUrl("/user")
    public String currentUser() {
        return "success:" + indexService.get();
    }

    @WebUrl(value = "/post",method = HttpMethod.POST)
    public String post(@RequestBody String body){
        return "sucess";
    }

}
