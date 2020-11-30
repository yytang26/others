package com.tyy.demo2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author:tyy
 * @date:2020/11/30
 */
@RestController
@RequestMapping(value = "/permission")
public class TestController {
    @RequestMapping(value = "/check", method = RequestMethod.POST)
        // 添加这个注解
    @PermissionAnnotation
    public JSONObject getGroupList(@RequestBody JSONObject request) {
        return JSON.parseObject("{\"message\":\"SUCCESS\",\"code\":200}");
    }
}
