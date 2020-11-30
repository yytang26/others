package com.tyy.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tyy.entity.UserVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public class JsonObjectMapping {
    public String asString(List<UserVO.UserConfig> list) {
        return JSON.toJSONString(list);
    }

    public List<UserVO.UserConfig> asObject(String string) {
        return JSON.parseObject(string, new TypeReference<List<UserVO.UserConfig>>() {
        });
    }
}