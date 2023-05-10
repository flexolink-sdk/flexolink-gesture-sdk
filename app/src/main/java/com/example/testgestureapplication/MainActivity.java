package com.example.testgestureapplication;

import android.Manifest;
import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okrx2.adapter.ObservableBody;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import a.flexolink.dataacquisitionV2lib.DataAcquisitionSDK;
import a.flexolink.dataacquisitionV2lib.bean.BleBean;
import a.flexolink.dataacquisitionV2lib.bean.ConnectResultType;
import a.flexolink.dataacquisitionV2lib.bean.ScanResultEvent;
import a.flexolink.dataacquisitionV2lib.interfaces.ConnectListener;
import a.flexolink.dataacquisitionV2lib.interfaces.ScanListener;
import a.flexolink.dataacquisitionV2lib.sdklib.utils.RequestPermissionsUtil;
import a.flexolink.gesturelib.GestureSDK;
import a.flexolink.gesturelib.common.GestureListener;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private TextView start;
    private TextView findDevice;
    private ImageView gestureImg;
    private static Gson gson = new Gson();
    private ArrayList<UserBean> userBeans;
    private String modelPath1;
    private String modelPath2;
    private String bleName = "Flex-EM08-310113";
    private String sign;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRequestData();
        textView = (TextView) findViewById(R.id.gesture);
        start = (TextView) findViewById(R.id.start);
        findDevice = (TextView) findViewById(R.id.findDevice);
        gestureImg = (ImageView) findViewById(R.id.gestureImg);
        //通过appKey 和 appSecret生成签名
        HashMap<String, Object> map = new HashMap<>();
        map.put("appKey", "rl33338ba18d3v");  //管开发者获取
        map.put("appSecret", "b3a8ddefdfca3212631353017dd1b331"); //管开发者获取
        sign =  SignUtil.sign(map);
        //获取token,把token放到header里
        requestToken();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"手势推理开始",Toast.LENGTH_SHORT).show();
                //开始手势,前提是蓝牙连接和模型下载都完成了才可用
                GestureSDK.getInstance().startGesture(MainActivity.this, getFilesDir().getPath()+"/model1.tflite", getFilesDir().getPath()+"/model2.tflite", 7);
                GestureSDK.getInstance().setGestureListener(new GestureListener() {
                    @Override
                    public void onRealTimeDirectionData(int i) {

                    }

                    @Override
                    public void onRealTimeGestureData(int i) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (i) {
                                    case 0:
                                        gestureImg.setImageResource(R.mipmap.gesture0);
                                        textView.setText("食指");
                                        break;
                                    case 1:
                                        gestureImg.setImageResource(R.mipmap.gesture1);
                                        textView.setText("捏指");
                                        break;
                                    case 2:
                                        gestureImg.setImageResource(R.mipmap.gesture2);
                                        textView.setText("ok");
                                        break;
                                    case 3:
                                        gestureImg.setImageResource(R.mipmap.gesture3);
                                        textView.setText("握拳");
                                        break;
                                    case 4:
                                        gestureImg.setImageResource(R.mipmap.gesture4);
                                        textView.setText("左");
                                        break;
                                    case 5:
                                        gestureImg.setImageResource(R.mipmap.gesture5);
                                        textView.setText("右");
                                        break;
                                    case 6:
                                        gestureImg.setImageResource(R.mipmap.gesture6);
                                        textView.setText("竖大拇指");
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void onRealTimeWaveData(float[][] floats) {

                    }

                    @Override
                    public void onRestGestureData() {
                        textView.setText("请放松");
                        gestureImg.setImageResource(R.mipmap.gesture_rest);
                    }
                });

            }

        });

        findDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //蓝牙搜索
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

                    @Override
                    public void onScanFinish(ScanResultEvent scanResultEvent) {

                    }
                });
            }
        });




    }

    /**
     * 获取模型url
     */
    public void requestModel(String bluetoothName){
        getModelUrl(bluetoothName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ResponseData<ArrayList<UserBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void _onNext(ResponseData<ArrayList<UserBean>> userBeanResponseData) {
                        if (null != userBeanResponseData) {
                            userBeans = userBeanResponseData.getData();
                            if (userBeans.size()>=1){
                                modelPath1 = userBeans.get(0).getModelUrlList().get(0);
                                modelPath2 = userBeans.get(0).getModelUrlList().get(1);
                                download(getFilesDir().getPath(),modelPath1,modelPath2);
                            }
                        }
                    }

                    @Override
                    public void _onError(String errorMessage) {
                        Log.e("MainActivity", "_onError: 获取失败" );
                    }
                });
    }

    /**
     * 获取授权token
     */
    public void requestToken(){
        getToken().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ResponseData<TokenBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void _onNext(ResponseData<TokenBean> loginBeanResponseData) {
                        if (null != loginBeanResponseData) {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.put("token", loginBeanResponseData.getData().getAccessToken());
                            OkGo.getInstance().addCommonHeaders(httpHeaders);
                            requestModel(bleName);
                        }
                    }

                    @Override
                    public void _onError(String errorMessage) {
                        Log.e("MainActivity", "_onError: token获取失败" );
                    }
                });
    }

    //获取模型地址
    public  Observable<ResponseData<ArrayList<UserBean>>> getModelUrl(String bluetoothName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("bluetoothName", bluetoothName);
        map.put("sign", sign);
        return OkGo.<ResponseData<ArrayList<UserBean>>>post("http://openapi.flexolinkai.com/api/rlIotProductUserModel/findByList")
                .upRequestBody(ReqBody.getReqBody(map))
                .converter(new Converter<ResponseData<ArrayList<UserBean>>>() {
                    @Override
                    public ResponseData<ArrayList<UserBean>> convertResponse(Response response) throws Throwable {
                        Type type = new TypeToken<ResponseData<ArrayList<UserBean>>>() {
                        }.getType();
                        return gson.fromJson(response.body().string(), type);
                    }
                })
                .adapt(new ObservableBody<ResponseData<ArrayList<UserBean>>>());
    }

   //获取token
    public  Observable<ResponseData<TokenBean>> getToken() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sign", sign);  //管开发者获取
        map.put("appKey", "rl33338ba18d3v");  //管开发者获取
        map.put("appSecret", "b3a8ddefdfca3212631353017dd1b331"); //管开发者获取
        return OkGo.<ResponseData<TokenBean>>post("http://openapi.flexolinkai.com/api/auth/getToken")
                .upRequestBody(ReqBody.getReqBody(map))
                .converter(new Converter<ResponseData<TokenBean>>() {
                    @Override
                    public ResponseData<TokenBean> convertResponse(Response response) throws Throwable {
                        Type type = new TypeToken<ResponseData<TokenBean>>() {
                        }.getType();
                        return gson.fromJson(response.body().string(), type);
                    }
                })
                .adapt(new ObservableBody<ResponseData<TokenBean>>());
    }

    /**
     * 初始化权限
     */
    public void initRequestData() {
        RequestPermissionsUtil.Companion.getInstance().requestPermissions(MainActivity.this, new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        setRootDir(MainActivity.this);
                        return null;
                    }
                }, new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        return null;
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    /**
     * 创建一个应用存储模型的根目录
     */
    private void  setRootDir(Activity activity) {

        File dirRoot = new File(getFilesDir(), "model");
        if (!dirRoot.exists() || !dirRoot.isDirectory()) {
            dirRoot.mkdirs();
        }
    }

    private void download(String path,String url1,String url2){
        FileDownloadQueueSet queueSet = new FileDownloadQueueSet(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Log.e("TAG", "progress: "+(float)soFarBytes/(float)totalBytes );
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {

            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });

        //存储位置可以根据自己所需修改存放目录
        ArrayList<BaseDownloadTask> tasks = new ArrayList<>();
        String nowPAS = path +"/"+"model1.tflite";
        tasks.add(FileDownloader.getImpl().create(url1).setPath(nowPAS).setForceReDownload(true).setTag(1));
        String nowPAS2 = path +"/"+"model2.tflite";
        tasks.add(FileDownloader.getImpl().create(url1).setPath(nowPAS2).setForceReDownload(true).setTag(2));
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
}
