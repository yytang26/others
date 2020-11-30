package com.tyy.test;

import com.tyy.entity.User;
import com.tyy.entity.UserVO;
import com.tyy.mapping.UserMapping;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author:tyy
 * @date:2020/11/30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapStructTest {

    @Resource
    private UserMapping userMapping;

    @Test
    public void tetDomain2DTO() {
        User user = new User()
                .setId(1L)
                .setUsername("zhangsan")
                .setSex(1)
                .setPassword("abc123")
                .setCreateTime(LocalDateTime.now())
                .setBirthday(LocalDate.of(1999, 9, 27))
                .setConfig("[{\"field1\":\"Test Field1\",\"field2\":500}]");
        UserVO userVo = userMapping.sourceToTarget(user);
        log.info("User: {}", user);

        log.info("UserVo: {}", userVo);

    }

    @Test
    public void testDTO2Domain() {
        UserVO.UserConfig userConfig = new UserVO.UserConfig();
        userConfig.setField1("Test Field1");
        userConfig.setField2(500);

        UserVO userVo = new UserVO()
                .setId(1L)
                .setUsername("zhangsan")
                .setGender(2)
                .setCreateTime("2020-01-18 15:32:54")
                .setBirthday(LocalDate.of(1999, 9, 27))
                .setConfig(Collections.singletonList(userConfig));
        User user = userMapping.targetToSource(userVo);
        log.info("UserVo: {}", userVo);

        log.info("User: {}", user);

    }
}