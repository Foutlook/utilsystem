package com.foutin.design.pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description
 * @author xingkai.fan
 * @date 2022/8/4 16:39
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventParam {

    private String orderSn;

    private Long userId;

}
