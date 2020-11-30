package com.tyy.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * @author:tyy
 * @date:2020/11/30
 */
@Data
@Accessors(chain = true)
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private Integer gender;
    private LocalDate birthday;
    private String createTime;
    private List<UserConfig> config;
    private String test;

    @Data
    public static class UserConfig {
        private String field1;
        private Integer field2;
    }
}