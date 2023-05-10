# 柔灵肌电SDK简介


肌电SDK是柔灵科技为了方便开发者接入肌电手环控制而推出的肌电SDK。该平台的主要目的是为了让开发者能够轻松地使用柔灵科技的肌电手环产品。通过使用SDK 开发者可以轻松获取和处理从柔灵科技产品收集到的生物信号数而无需具备丰富的肌电背景知识，从而让开发者能够更加专注于产品相关的行业应用开发。
该SDK提供了开发工具和文档，以帮助开发者快速地接入柔灵科技的产品，并开始构建自己的应用程序。其中，主要包括以下几个方面：
1. API文档：肌电SDK提供了详细的API文档，以帮助开发者了解如何使用肌电接口。这些文档包括接口说明、参数说明、示例代码等等，非常适合开发者参考。
2. SDK：肌电SDK主要提供的开发语言为JAVA
3. 支持服务：肌电SDK还提供了支持服务，包括技术支持和咨询服务。开发者可以通过电话、邮件或在线聊天等方式与柔灵科技的技术支持团队联系，获得快速有效的支持。


##### 购买柔灵产品 
肌电SDK以访问密钥（appKey）识别调用者身份，提供自动签名等功能，方便您通过SDK接入柔灵脑电产品。实现云SDK功能需要您购买柔灵脑电产品，并申请appKey和appSecret。

### 以下是sdk文档链接 
https://openplatform.flexolinkai.com/#/platform/overview

### 快速接入SDK
```
DataAcquisitionSDK.getInstance().scanDevice(MainActivity.this, new ScanListener() {
@Override
public void onScanResult(BleBean bleBean) {
Log.e("TAG", "蓝牙" + bleBean.getName());
if (bleBean.getName().equals(bleName)) {
DataAcquisitionSDK.getInstance().connectDevice(MainActivity.this, bleBean, new ConnectListener() {
@Override
public void onConnectResult(int i) {
if (i == ConnectResultType.SUCCESS.getValue()) {
Log.e("TAG", "onConnectResult: 连接成功");
} else {
Log.e("TAG", "onConnectResult: 连接失败");
}
}
});
}
}
```


@Override
public void onScanFinish(ScanResultEvent scanResultEvent) {

}
                });

### demo流程
1.联系业务员获取appKey和appSecret

2.通过appKey和appSecret生成sign签名（通过demo的SignUtil生成）

3.用appKey和appSecret和sign签名获取token（接口demo里有备注）

4.把token放到header里面，通过调用findByList接口（传入蓝牙名称参数）获取模型文件（前提得通过电脑端训练后上传成功才能获取到）

5.把拿到的模型文件下载到本地，可以通过自己的方式下载和保存（demo有例子）

6.蓝牙搜索连接

7.把模型文件的存储路径传到GestureSDK(在蓝牙连接和模型下载成功之后方可正常使用)

