package com.yupi.usercenterbackend.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class XingQiuTableUserInfo {

    @ExcelProperty("成员编号")
    private String planetCode;

    @ExcelProperty("成员昵称")
    private String username;
}