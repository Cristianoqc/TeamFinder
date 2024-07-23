package com.yupi.usercenterbackend.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;


public class ImportExcel {

    public static void main(String[] args) {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName = "D:\\星球项目\\user-center-backend\\src\\main\\resources\\testExcel.xlsx";

        readByListener(fileName);
        synchronousRead(fileName);
    }

    public static void readByListener(String fileName){
        EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doRead();
    }

    public static void synchronousRead(String fileName){
        List<XingQiuTableUserInfo> totalDataList = EasyExcel.read(fileName).head(XingQiuTableUserInfo.class).sheet().doReadSync();
        for (XingQiuTableUserInfo xingQiuTableUserInfo : totalDataList) {
            System.out.println(xingQiuTableUserInfo);
        }
    }
}
