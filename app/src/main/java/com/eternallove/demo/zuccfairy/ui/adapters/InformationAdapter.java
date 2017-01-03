package com.eternallove.demo.zuccfairy.ui.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.modle.Information;
import com.eternallove.demo.zuccfairy.ui.activities.LoginActivity;
import com.eternallove.demo.zuccfairy.ui.activities.MainActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class InformationAdapter extends ArrayAdapter<Information> {
    private int resourceId;
    private Context mcontext;

    public InformationAdapter(Context context, int textViewResourceId, List<Information> objects) {
        super(context, textViewResourceId, objects);
        this.mcontext = context;
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //提高效率，如果convertview为空。
        // 则使用layoutinflater加载布局，
        // 如果不为空则直接对convertview进行重用
        Information information = getItem(position);
        View view;
        ViewHolder viewHolder;
        //viewHolder用户对控件的实例进行缓存
        // 当convertview为空的时候，创建一个viewholder对象，并将控件的实例都存放在viewholder里
        //调用View的setTag（）方法，将ViewHolder对象存储在View中。
        // 当convertview不为空的时候则调用View的getTag（）方法，把ViewHolder重新取出
        //所有控件的实例都缓存在了ViewHolder里没必要每次都通过findViewById（）方法来获取控件实例了

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            //如果converView为空,则使用LayoutInFlater去加载布局，如果不为空则直接对convertView进行重用
            //这样就大大提高了ListView的运行效率，在快速滚动的时候也可以表现出更好的性能
            viewHolder = new ViewHolder();
            //内部类ViewHolder用于对控件的实例进行缓存
            // 当convertView为空的时候，创建一个ViewHolder对象，将控件的实例都存放在ViewHolder里，
            // 然后调用View的setTag（）方法，将ViewHolder对象存储在View中
            //当convertView不为空的时候则调用View的getTag（），把ViewHolder重新取出
            //所有空间的实例都缓存在了ViewHolder里
            viewHolder.informationImage = (ImageView) view.findViewById(R.id.information_image);
            viewHolder.informationName = (TextView) view.findViewById(R.id.information_name);
            viewHolder.informationMessage = (TextView) view.findViewById(R.id.information_message);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (information.getImageId() != -1) {
            viewHolder.informationImage.setImageResource(information.getImageId());
        }else{
            viewHolder.informationImage.setVisibility(View.GONE);
        }
        viewHolder.informationName.setText(information.getName());
        if (information.getMessage() != null) {
            viewHolder.informationMessage.setText(information.getMessage());
        }else{
            viewHolder.informationMessage.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {
        ImageView informationImage;
        TextView informationName;
        TextView informationMessage;
    }
}
