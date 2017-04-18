package com.feicui.speechdemo.speechdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 语音听写
public class RecoActivity extends AppCompatActivity {

    @BindView(R.id.tvShow)
    TextView tvShow;

    private SpeechRecognizer mSpeechRecognizer;

    // 用于处理结果
    private HashMap<String,String> mResults = new LinkedHashMap<>();
    private RecognizerDialog mRecognizerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);
        ButterKnife.bind(this);

        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(this,null);
        // 1. 创建UI的听写类
        mRecognizerDialog = new RecognizerDialog(this,null);

        // 2. 设置SDK参数
        setParameter();

        // 3. 开始听写
        /**
         * 使用UI效果的听写：
         //1.创建RecognizerDialog对象，用于展示UI效果的
         RecognizerDialog    iatDialog = new RecognizerDialog(this,mInitListener);
         //2.设置听写参数，同上节
         //3.设置回调接口
         iatDialog.setListener(recognizerDialogListener);
         //4.开始展示和听写
         iatDialog.show();
         */



    }
    // 设置SDK参数
    private void setParameter() {

        // 清空参数
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS,null);

        // 设置听写引擎：云端（在线）
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);

        // 设置听写的语言
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE,"json");

        // 设置语言类型：中文
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE,"zh_cn");

        // 标点:0无标点，1有
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,"1");

        // 设置语音前端点:用户多久没有说话，当超时处理
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS,"3000");

        // 设置语音后端点：用户停止说话多久自动录音停止
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS,"2000");
    }
    // 听写监听
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        // 声音变化的时候
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        // 开始录音
        @Override
        public void onBeginOfSpeech() {
            Toast.makeText(RecoActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
        }

        // 结束录音
        @Override
        public void onEndOfSpeech() {
            Toast.makeText(RecoActivity.this, "结束录音", Toast.LENGTH_SHORT).show();

        }

        //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.i("TAG",recognizerResult.getResultString());
            printResult(recognizerResult);
        }

        // 会话发生错误的时候
        @Override
        public void onError(SpeechError speechError) {

        }

        // 扩展用的接口
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
    // 处理结果  文本没有显示出来
//    private void printResult(RecognizerResult recognizerResult) {
//        String text = JsonParser.parseIatResult(recognizerResult.toString());
//        String sn = null;
//        try {
//            JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
//            sn = resultJson.optString("sn");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mResults.put(sn,text);
//        StringBuffer stringBuffer = new StringBuffer();
//        for (String key:mResults.keySet()){
//            stringBuffer.append(mResults.get(key));
//        }
//        // 拿到之后：stringBuffer
//        tvShow.setText(stringBuffer.toString());
//    }
    // 处理结果  文本显示出来Demo里的方法
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mResults.keySet()) {
            resultBuffer.append(mResults.get(key));
        }

        tvShow.setText(resultBuffer.toString());
    }

    @OnClick(R.id.btnStart)
    public void onClick() {

        // 开始进行录音和转换
//        mSpeechRecognizer.startListening(mRecognizerListener);
        tvShow.setText(null);
        mResults.clear();
        setParameter();
        // 带UI效果的
        mRecognizerDialog.setListener(dialogListener);
        mRecognizerDialog.show();

    }

    // UI听写的监听
    private RecognizerDialogListener dialogListener = new RecognizerDialogListener() {

        // 拿到结果
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.i("TAG",recognizerResult.getResultString());
            printResult(recognizerResult);
        }

        // 错误的时候
        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(RecoActivity.this, speechError.getPlainDescription(true), Toast.LENGTH_SHORT).show();
        }
    };
}
