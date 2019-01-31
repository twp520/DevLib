package com.colin.tools;

import android.os.Environment;
import com.amazonaws.services.s3.model.Region;

//常量类
public interface Constants {
    //图片压缩路径
    String photoCompressDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LesingCache";

    String SP_USER_ID = "userId";
    String AWS_PIC_URL = "https://s3-ap-southeast-1.amazonaws.com/jfy2018/";
    String BUCKET_NAME = "";
    String BUCKET_REGION = Region.AP_Singapore.toString();
    String COGNITO_POOL_ID = "ap-southeast-1:677bbdc5-06be-4378-8eca-010b9911a1cd";
    String COGNITO_POOL_REGION = Region.AP_Singapore.toString();
    String CHANNEL_ID_PUSH = "channel_id_push";

}
