package com.tyy.mapping;

import com.alibaba.fastjson.JSON;
import com.tyy.entity.User;
import com.tyy.entity.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author:tyy
 * @date:2020/11/30
 */
@Mapper(componentModel = "spring")
public interface UserMapping extends BaseMapping<User, UserVO> {

    @Mapping(target = "gender", source = "sex")
    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Override
    UserVO sourceToTarget(User var1);

    @Mapping(target = "sex", source = "gender")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")

    @Override
    User targetToSource(UserVO var1);

    default List<UserVO.UserConfig> strConfigToListUserConfig(String config) {
        return JSON.parseArray(config, UserVO.UserConfig.class);
    }

    default String listUserConfigToStrConfig(List<UserVO.UserConfig> list) {
        return JSON.toJSONString(list);
    }
}