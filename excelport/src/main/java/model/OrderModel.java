package model;

import lombok.Data;

import java.util.Date;

/**
 * @author:tyy
 * @date:2020/12/1
 */

@Data
public class OrderModel {
    private String orderNo;

    private String amount;

    private Date createTime;


}
