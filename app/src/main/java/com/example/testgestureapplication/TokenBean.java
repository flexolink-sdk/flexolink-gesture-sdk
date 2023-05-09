package com.example.testgestureapplication;

import java.util.List;

public class TokenBean {

    /**
     * accessToken : 0ab7036e2bad46fba4b4e9d7a2544fc2
     * expiredAt : 1679550548723
     * roleList : ["eegByteToString","imuByteToString","dataConvertEEG","dataConvertIMU","filterSetup","filter","writeFileHeader","writeData","writeShortData","writeAnnotation","setAnnotationNumber","closeFile","SleepStage","loadModel","SignalQu"]
     */

    private String accessToken;
    private long expiredAt;
    private List<String> roleList;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(long expiredAt) {
        this.expiredAt = expiredAt;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
