package com.smart.tvlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.tvlauncher.R;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.presenter.BasePresenter;
import com.smart.tvlauncher.utils.AppUtils;

import java.util.List;

/**
 * Created by patrick on 2017/3/8.
 */

public class AppsAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> list;
    private LayoutInflater layoutInflater;

    public AppsAdapter(Context context, List<AppInfo> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_apps , parent ,false);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo appInfo = list.get(position);
        viewHolder.ivIcon.setImageDrawable(AppUtils.getIcon(context , appInfo.getPackageName()));
        viewHolder.tvLabel.setText(AppUtils.getLabelName(context, appInfo.getPackageName()));
        return convertView;
    }

    class ViewHolder{
        public ImageView ivIcon;
        public TextView tvLabel;
    }
}
