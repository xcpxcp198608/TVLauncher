package com.smart.tvlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.tvlauncher.R;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.utils.AppUtils;

import java.util.List;

/**
 * Created by patrick on 2017/3/8.
 */

public class ShortcutAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> list;
    private LayoutInflater layoutInflater;
    private String shortcut;

    public ShortcutAdapter(Context context, List<AppInfo> list ,String shortcut) {
        this.context = context;
        this.list = list;
        this.shortcut = shortcut;
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
            convertView = layoutInflater.inflate(R.layout.item_app_select , null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo appInfo = list.get(position);
        viewHolder.ivIcon.setImageDrawable(AppUtils.getIcon(context,appInfo.getPackageName()));
        viewHolder.tvLabel.setText(AppUtils.getLabelName(context, appInfo.getPackageName()));
        if(shortcut.equals(appInfo.getShortcut())){
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvLabel;
        CheckBox checkBox;
    }
}
