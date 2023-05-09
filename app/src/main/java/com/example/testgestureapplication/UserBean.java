package com.example.testgestureapplication;

import java.util.List;

public class UserBean {

    /**
     * id : 1655773245243359234
     * shopId : 7
     * mac : CC:EF:9E:A3:65:B3
     * nickName : Je
     * bluetoothName : Flex-EM08-310113
     * num : 7
     * versionNumber : 0
     * modelUrlList : ["https://rlkj-prod.oss-cn-hangzhou.aliyuncs.com/model/1683602077.5675542.tflite"," https://rlkj-prod.oss-cn-hangzhou.aliyuncs.com/model/1683602078.1406016.tflite"]
     * createTime : 2023-05-09 11:14:39
     * updateTime : 2023-05-09 11:14:39
     */

    private long id;
    private int shopId;
    private String mac;
    private String nickName;
    private String bluetoothName;
    private int num;
    private int versionNumber;
    private String createTime;
    private String updateTime;
    private List<String> modelUrlList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getModelUrlList() {
        return modelUrlList;
    }

    public void setModelUrlList(List<String> modelUrlList) {
        this.modelUrlList = modelUrlList;
    }
}
