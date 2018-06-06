package com.example.administrator.aqqje;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎界面的控制类
 * Created by aqqje on 2018/6/6.
 */

public class SplashActivity extends AppCompatActivity {
    private TextView tv_splash_version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // 获取组件
        RelativeLayout r1_splash = (RelativeLayout)findViewById(R.id.r1_splash);
        tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号" + getVersion());
        // 背景透明度变化3s内从 0.3 --> 0.1
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(3000);
        r1_splash.startAnimation(alphaAnimation);

        // 创建 Time 对象
        Timer time = new Timer();
        // 创建 TimerTask 对象
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, CalculatorActivity.class);
                startActivity(intent);
                finish();
            }
        };
        // 使用 timer.schedule() 方法调用 timerTask， 定时3s 后执行 run
        time.schedule(timerTask, 3000);

    }

    /**
     * 获取当前版本号
     * @return version
     */
    public String getVersion() {
        // 得到系统的包管理器，已经得到了 apk 的面向对象包装
        PackageManager pm = this.getPackageManager();
        try {
            /* getPackageName() : 当前应用程序的包名
                0: 可选的附加信息， 这里用不到，可以定义为0
             */
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
