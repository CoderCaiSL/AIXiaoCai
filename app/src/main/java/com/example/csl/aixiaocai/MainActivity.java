package com.example.csl.aixiaocai;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.csl.aixiaocai.enity.BaiduEnity;
import com.example.csl.aixiaocai.enity.InputTuLing;
import com.example.csl.aixiaocai.enity.ResultTuLing;
import com.example.csl.aixiaocai.httpRetrofitClient.InputTuLingHttp;
import com.example.csl.aixiaocai.util.AiUtil;
import com.example.csl.aixiaocai.util.AiUtilText;
import com.example.csl.aixiaocai.util.CustomProgressDialog;
import com.google.gson.Gson;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements EventListener {
    protected TextView txtLog;
    protected TextView txtResult;
    protected Button btn;
    protected Button stopBtn;
    protected Button btnTwo;
    protected Button btnWakeUp;
    protected Button btnSet;
    private static String DESC_TEXT = "";
    private String TTSTEXT = "";//识别出来的语句

    private EventManager asr;

    private boolean logTime = true;

    private boolean enableOffline = true; // 测试离线命令词，需要改成true
    protected int TTSLength = 512;//百度语音合成一次性合成的

    ListenDialog dialog ;

    CustomProgressDialog customProgressDialog;
    private QMUIListPopup mListPopup;
    private AiUtil aiUtil;
    /**
     * 测试参数填在这里
     */
    public void start() {
        txtLog.setText("");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        //唤醒功能
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        params.put(SpeechConstant.PID,19361);//普通话远场景模型
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        printLog("输入参数：" + json);
    }
    private void stop() {
        printLog("停止识别：ASR_STOP");
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }
    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }
    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_mini);
        initView();
        dialog = new ListenDialog(MainActivity.this,"");
        customProgressDialog =new CustomProgressDialog(MainActivity.this,"识别中....",R.drawable.myprogressframe);
        initPermission();
        asr = EventManagerFactory.create(this, "asr");
        aiUtil = new AiUtil(MainActivity.this);
        asr.registerListener(this); //  EventListener 中 onEvent方法
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn.setText("请说话。。。。");
                start();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                aiUtil.isMultiSim(MainActivity.this);
                aiUtil.call(MainActivity.this,0,"10086");
                stop();
                btn.setText("点击开始和瓦力说话");
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.ActionStart(MainActivity.this);
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAllRecog.ActionStart(MainActivity.this);
            }
        });
        btnWakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityWakeUpRecog.ActionStart(MainActivity.this);
            }
        });
        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }
    //   EventListener  回调方法
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }
        Gson gson = new Gson();
        BaiduEnity baiduEnity;
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_SERIALNUMBER)) {
            //dialog.show();
            customProgressDialog.show();
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)) {
            //dialog.dismiss();
            customProgressDialog.dismiss();
        }
        //BaiduEnity baiduEnity = gson.fromJson(params,BaiduEnity.class);
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    String text = new String(data, offset, length);
                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
                    dialog.dismiss();
                    baiduEnity = gson.fromJson(text,BaiduEnity.class);
                    TTSTEXT = baiduEnity.getMerged_res().getSemantic_form().getRaw_text();
                    switch (AiTodo(TTSTEXT)){
                        case 1:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                aiUtil.callPhone(TTSTEXT);
                            }else {
                                MainActivityPermissionsDispatcher.callPhoneNWithPermissionCheck(MainActivity.this);
                            }
                            break;
                        case 2:

                            break;
                        default:
                            ChatToTuLing(baiduEnity.getMerged_res().getSemantic_form().getRaw_text());
                            break;
                    }
                }
            }
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        printLog(logTxt);
    }
    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
        txtLog.append(text + "\n");
    }
    private void initView() {
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtLog = (TextView) findViewById(R.id.txtLog);
        btn = (Button) findViewById(R.id.btn);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        btnSet = (Button) findViewById(R.id.btn_set);
        btnWakeUp = (Button) findViewById(R.id.btn_awaken);
        txtLog.setText(DESC_TEXT + "\n");
    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 启动简单识别引擎
     */
    private void startASR() {
        txtLog.setText("");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        //唤醒功能
        params.put(SpeechConstant.PID,19361);//普通话远场景模型
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        printLog("输入参数：" + json);
    }
    /**
     * 启动唤醒识别引擎
     */
    private void startWakeUp() {
        txtLog.setText("");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        //唤醒功能
        params.put(SpeechConstant.PID,19361);//普通话远场景模型
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        printLog("输入参数：" + json);
    }

    /**
     * 与图灵聊天
     */
    public void ChatToTuLing(String text){
        //dialog.setImgGif("thinking");
        InputTuLingHttp inputTuLingHttp = new InputTuLingHttp();
        InputTuLing inputTuLing = new InputTuLing();
        InputTuLing.UserInfoBean userInfoBean = new InputTuLing.UserInfoBean();
        InputTuLing.PerceptionBean perceptionBean = new InputTuLing.PerceptionBean();
        InputTuLing.PerceptionBean.InputTextBean inputTextBean = new InputTuLing.PerceptionBean.InputTextBean();
        inputTextBean.setText(text);
        userInfoBean.setApiKey("1bd5d266eb984c07aec359981084b935");
        userInfoBean.setUserId("androidAi");
        inputTuLing.setReqType(0);
        inputTuLing.setUserInfo(userInfoBean);
        perceptionBean.setInputText(inputTextBean);
        inputTuLing.setPerception(perceptionBean);
        inputTuLingHttp.InputTuLingText(inputTuLing);
        inputTuLingHttp.setGetRetrofitListener(new InputTuLingHttp.GetRetrofitListener() {
            @Override
            public void getResultSuccess(Response<ResultTuLing> response) {
                //获取成功
                if (response.code() == 200){
                    //语音播放
                    ResultTuLing resultTuLing = response.body();
                    for (ResultTuLing.ResultsBean enity : resultTuLing.getResults()){
                        if (enity.getResultType().equals("text")){
                            Log.e("获取",enity.getValues().getText());
                            //dialog.dismiss();
                            String text = enity.getValues().getText();
                            customProgressDialog.dismiss();
                            if (text.length() < 512){
                                ChatDialog chatDialog = new ChatDialog(MainActivity.this,enity.getValues().getText());
                                chatDialog.setTitle("语音播放ing。。。。");
                                chatDialog.SetDimssListener(new ChatDialog.DimssListener() {
                                    @Override
                                    public void setOnDimss() {
                                        start();
                                    }
                                });
                                stop();
                                chatDialog.show();
                            }else {
                                //获取的文字超出一次性合成的最大限制，进行分批处理
                                final List<String> testList = new ArrayList<>();
                                for (int i = 0;i < text.length() / TTSLength;i++){
                                    if (i == text.length() / TTSLength){
                                        testList.add(text.substring(i*(text.length() / TTSLength),
                                                text.length()-1));
                                    }else {
                                        testList.add(text.substring(i*(text.length() / TTSLength),
                                                (i+1)*(text.length() / TTSLength)));
                                    }
                                }
                                //对拿到的信息进行分批语音合成
                                for (int i = 0;i < testList.size();i++){
                                    ChatDialog chatDialog = new ChatDialog(MainActivity.this,enity.getValues().getText());
                                    chatDialog.setTitle("语音播放ing。。。。");
                                    final int finalI = i;
                                    chatDialog.SetDimssListener(new ChatDialog.DimssListener() {
                                        @Override
                                        public void setOnDimss() {
                                            if (finalI == testList.size()){
                                                start();
                                            }
                                        }
                                    });
                                    stop();
                                    chatDialog.show();
                                }
                            }
                        }
                    }
                }else {
                    //语音播放
                    Log.e("获取",response.code()+""+response.raw()+"");
                }
            }
            @Override
            public void getResultFailure(String message) {
                //获取失败
                Log.e("输出",message);
            }
        });
    }
    /**
     * 悬浮层初始化
     */
    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    "前台语音",
                    "后台唤醒",
            };
            List<String> data = new ArrayList<>();

            Collections.addAll(data, listItems);

            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,R.layout.simple_item, data);

            mListPopup = new QMUIListPopup(MainActivity.this, QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(MainActivity.this, 250),
                    QMUIDisplayHelper.dp2px(MainActivity.this, 200),
                    new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(MainActivity.this, "Item " + (i + 1), Toast.LENGTH_SHORT).show();
                    mListPopup.dismiss();
                }
            });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    /*mActionButton2.setText(getContext().getResources().getString(R.string.popup_list_action_button_text_show));*/
                }
            });
        }
    }

    /**
     * 进行特殊指令判断
     * @param text
     * @return
     */
    protected int AiTodo(String text){
        AiUtilText aiUtilText = new AiUtilText();
        for (int i = 0;i < aiUtilText.CALL.length;i++){
            if (text.contains(aiUtilText.CALL[i])){
                return 1;
            }
        }
        for (int i = 0;i < aiUtilText.MUSIC.length;i++){
            if (text.contains(aiUtilText.MUSIC[i])){
                return 2;
            }
        }
        return 0;
    }


    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void callPhoneN() {
        aiUtil.callPhone(TTSTEXT);
    }
    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void callPhoneSR(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("读取相册的权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //再次执行请求
                        request.proceed();
                    }
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void callPhonePD() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void callPhoneNA() {
        Toast.makeText(this, "不再询问", Toast.LENGTH_SHORT).show();
    }
}