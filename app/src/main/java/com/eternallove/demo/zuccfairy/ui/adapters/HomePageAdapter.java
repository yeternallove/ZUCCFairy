package com.eternallove.demo.zuccfairy.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.modle.HomePage;

import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 * 重写了父类的一组构造函数，用于将上下文、ListView子项布局的id和数据都传递进来。
 * 另外又重写了getView（），这个方法在每个子项被滚动到屏幕内的时候会被调用。
 * 在getView方法中，首先通过getItem（）方法得到当前项的实例，然后使用LayoutInflater来为这个子项加载传入的布局
 * 调用View的findViewById（）方法分别获取到ImageView和TextView的实例
 * 调用它们的setImageResource（）和setText（）方法来设置显示的图片和文字 最后将布局返回 就完成了自定义适配器
 */
public class HomePageAdapter extends ArrayAdapter<HomePage> {
    private int resourceId;

    public HomePageAdapter(Context context, int textViewResourceId, List<HomePage> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //提高效率，如果convertview为空。
        // 则使用layoutinflater加载布局，
        // 如果不为空则直接对convertview进行重用
        HomePage homePage = getItem(position);
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
            viewHolder.homePageImage = (ImageView) view.findViewById(R.id.homepage_image);
            viewHolder.homePageName = (TextView) view.findViewById(R.id.homepage_text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.homePageImage.setImageResource(homePage.getImageId());
        viewHolder.homePageName.setText(homePage.getName());
        return view;
    }

    class ViewHolder {
        ImageView homePageImage;
        TextView homePageName;
    }
}
