package com.tyy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author:tyy
 * @date:2020/11/30
 */
@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String username;
    private String password;
    private Integer sex;
    private LocalDate birthday;
    private LocalDateTime createTime;
    private String config;
    private String test;
}
