package com.odison.saleview.util;

import android.content.Context;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

/**
 * Created by odison on 2015/12/28.
 *
 * 在这里调用初始化需要一定时间，改为在AppContext里面实现
 *
 */
public class SpeechUtil {
    public static void simpleSpeech(Context context, String str){
        //1.创建 SpeechSynthesizer 对象, 第二个参数： 本地合成时传 InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(context, new InitListener() {
            @Override
            public void onInit(int i) {
                new ToastMessageTask().execute("speech init:"+i);
                if( i != ErrorCode.SUCCESS){
                    TLog.log("FLYTEK:init",""+i);
                }else{

                }

            }
        });
//2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
//设置发音人（更多在线发音人，用户可参见 附录12.2
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
        mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        mTts.startSpeaking(str, null);
    }
}
