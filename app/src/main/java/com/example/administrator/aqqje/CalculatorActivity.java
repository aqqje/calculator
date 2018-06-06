package com.example.administrator.aqqje;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.wltea.expression.ExpressionEvaluator;

/**
 * Created by aqqje on 2018/6/4.
 */

public class CalculatorActivity extends AppCompatActivity {

    private GridView mgridButtons = null; // 计算器按键
    private EditText meditInput = null; // 表达式输入框
    private BaseAdapter mAdapter = null; // 按键适配器

    private String mPreStr =  ""; // 表达式的历史部分, 为空 或 以 <br/> 换行
    private String mLastStr = ""; // 表达式前面深颜色的部分

    private Boolean mIsExcuteNow = false; // 判断是否刚刚执行了一个表达式, 新表达式时需要在这前加上换行符
    private String newLine = "<br\\>"; //  换行符

    private String[] mTextButtons = new String[]{
            "Back","(",")","CE",
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","+",
            "0",".","=","-"};

    private long mExitTime; // 存放上一点击“返回键”的时刻

    /**
     * meditInput 视图显示样式设置，并增加 html 颜色样式
     */
    private void setText(){
        final String[] tags = new String[]{"<font color='#858585'>", "<font color='#CD2626'>", "</font> "};
        StringBuilder builder = new StringBuilder();
        // 添加颜色标签
        builder.append(tags[0]); builder.append(mPreStr); builder.append(tags[2]);
        builder.append(tags[1]); builder.append(mLastStr); builder.append(tags[2]);
        meditInput.setText(Html.fromHtml(builder.toString()));
        meditInput.setSelection(meditInput.getText().length());
        // 表示获取焦点
        meditInput.requestFocus();
    }

    public void excuteExpression(){
        Object result = null;
        try {
            result = ExpressionEvaluator.evaluate(mLastStr);
        }catch (Exception e){
            // 设置错误信息
            meditInput.setError(e.getMessage());
            meditInput.requestFocus();
            mIsExcuteNow = false;
            return;
        }
        mIsExcuteNow = true;
        mLastStr += "=" + result;
        meditInput.setError(null);
        // 显示视图
        setText();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calculator);
        // 查找控件
        mgridButtons = (GridView)findViewById(R.id.grid_buttons);
        meditInput = (EditText)findViewById(R.id.edit_input);
        // 配置适配器
        mAdapter = new CalculatorAdapter(this, mTextButtons);
        mgridButtons.setAdapter(mAdapter);
        // 禁止 meditInput 从键盘上输入
        meditInput.setKeyListener(null);
        // 新建监听器 OnButtonItemClickListener 为每一个 mTextButtons 中的按键设置监听
        OnButtonItemClickListener  onButtonItemClickListener = new OnButtonItemClickListener();
        mgridButtons.setOnItemClickListener(onButtonItemClickListener);
    }


    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String text = (String)adapterView.getAdapter().getItem(position);
            // 判断是什么按键，并执行什么样的功能
            if(text.equals("=")){
                excuteExpression();
            }else if(text.equals("Back")){
                // 对于新表达式不空的情况进行判断
                if(mLastStr.length() == 0){
                    // 历史记录不为空，必然是以换行符结尾
                    if(mPreStr.length() != 0){
                        // 首先找历史表达式的后 newLine 符
                        mPreStr = mPreStr.substring(0,mPreStr.length() - newLine.length());
                        // 设置表达式的 html 颜色
                        mIsExcuteNow = true;
                        // 找历史表达式前的 newLine 符
                        int index = mPreStr.lastIndexOf(newLine);
                        // 如果 index 是 -1 则表明当前历史的表达式只有当前的
                        if(index == -1){
                            mLastStr = mPreStr;
                            mPreStr = "";
                            }else{
                                mLastStr = mPreStr.substring(index + newLine.length(), mPreStr.length());
                                mPreStr = mPreStr.substring(0,index);
                            }
                        }
                    }else{
                        // 如果新表达式长度不为 0 直接去掉一个字符就好了
                        mLastStr = mLastStr.substring(0,mLastStr.length() - 1);
                    }
                    setText();
                }else if(text.equals("CE")){
                    mLastStr = "";
                    mPreStr = "";
                    mIsExcuteNow = false;
                    meditInput.setText("");
                }else{
                    if(mIsExcuteNow){
                        mPreStr = mLastStr + newLine;
                        mIsExcuteNow = false;
                        mLastStr = text;
                    }else{
                        mLastStr += text;
                    }
                    setText();
                }
            }
        }



    /**
     * 再按一次退出程序
     * @param keyCode: 按键的代码
     * @param event：点击事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断是否点击了“返回键”
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // 大于 2000ms 则认为是误操作， 使用 Toast 进行提示
            if((System.currentTimeMillis() - mExitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 记录本次点击的时间，以便下次判断
                mExitTime = System.currentTimeMillis();
            }else{
                // 小于2000ms 则认为希望退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
