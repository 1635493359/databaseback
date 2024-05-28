package com.example.databasebackup.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backup")
public class BackupController {

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public Map<String, String> payApp() {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("msg", "ok");
        return parameter;
    }

    @RequestMapping(path = "/get" , method = RequestMethod.POST)
    public String getMessage(@RequestBody List<String> answer) {
        System.out.println(answer.toString());
        return answer.toString();
    }
    @ResponseBody
    @RequestMapping(value = "/get2", method = RequestMethod.POST)
    public String getByJSON(@RequestBody JSONObject jsonParam) {
        // 直接将json信息打印出来
        System.out.println(jsonParam.toJSONString());
        return  "success";
    }

}
