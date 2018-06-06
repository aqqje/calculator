package com.example.administrator.aqqje;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 自定义一个计算器适配器
 * Created by aqqje on 2018/6/5.
 */

class CalculatorAdapter extends BaseAdapter {

    private Context context;
    private String[] strs = null;

    /**
     * @param context 上下文
     * @param strs    数据源
     */
    public CalculatorAdapter(Context context, String[] strs) {
        this.context = context;
        this.strs = strs;
    }

    // 获取数据源的总数
    @Override
    public int getCount() {
        return strs.length;
    }

    // 告诉适配器获取目前容器的数据对象
    @Override
    public Object getItem(int position) {
        return strs[position];
    }

    // 告诉适配器获取目前容器数据对象的ID
    @Override
    public long getItemId(int opsition) {
        return opsition;
    }

    // 获取当前要显示按键的View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 使用方法 inflate 实例化一个 View 对象
        View view = View.inflate(context, R.layout.item_button, null);
        // 通过 view 对象找到对象的 TextView 控件
        TextView textView = (TextView) view.findViewById(R.id.text_button);
        // 通过 position 获取按键对象的设置，并设置
        String mstr = strs[position];
        textView.setText(mstr);
        // 给 Back 和 CE 两的按键增加效果
        if (mstr.equals("Back")) {
            textView.setBackgroundResource(R.drawable.selector_button_backspace);
            textView.setTextColor(Color.WHITE);
        } else if (mstr.equals("CE")) {
            textView.setBackgroundResource(R.drawable.selector_button_backspace);
            textView.setTextColor(Color.WHITE);
        }
        return view;
    }
}
