package com.pri.controller;

import com.pri.annotation.*;
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

    @WebUrl("/get/{userId}")
    public String getUser(@RequestParam String userId){

        return "userid";
    }

    @WebUrl("/get/{userId}/www")
    public String getUsers(@RequestParam String userId){

        return "www";
    }

    @WebUrl("/{userId}")
    public String getUsersq(@RequestParam String userId){

        return "xxsas";
    }

}
