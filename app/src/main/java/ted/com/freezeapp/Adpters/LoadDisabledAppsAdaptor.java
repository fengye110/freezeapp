package ted.com.freezeapp.Adpters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import ted.com.freezeapp.other.AppStat;
import ted.com.freezeapp.R;
import ted.com.freezeapp.helper.AppsHelper;
import ted.com.freezeapp.other.TagData;

/**
 * Created by fy1 on 15/05/28.
 */
public class LoadDisabledAppsAdaptor extends BaseAdapter {
    LayoutInflater inflater;
    Context  ctx;
    SwipeLayout openedSwipeLayout = null;
    ArrayList<AppStat> pre_disabled_sysApps;
    PackageManager pm;

    public void reflush()
    {
        notifyDataSetChanged();
    }

    public LoadDisabledAppsAdaptor(Context context, PackageManager pm){
        pre_disabled_sysApps = AppsHelper.loaddisabledSysApps(pm);
        this.ctx = context;
        this.pm = pm;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(pre_disabled_sysApps != null)
            return pre_disabled_sysApps.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return pre_disabled_sysApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TagData dt;
        if( view == null){
            view = inflater.inflate(R.layout.lv_item, parent, false);
            dt = new TagData();
            dt.icon = (ImageView) view.findViewById(R.id.iv_icon);
            dt.shortname = (TextView) view.findViewById(R.id.iv_short_name);
            dt.longname = (TextView) view.findViewById(R.id.iv_longname);
            dt.sw = (Switch)view.findViewById(R.id.sw_enabled);
            dt.uninstall_btn = (ImageButton) view.findViewById(R.id.btn_uninstall);

            view.setTag(dt);
        }

        dt = (TagData)view.getTag();

        AppStat as = (AppStat)getItem(position);
        dt.icon.setImageDrawable(AppsHelper.loadicon(as.longName, pm));
        dt.longname.setText(as.longName);
        dt.shortname.setText(as.shortName);
        dt.as = as;

        dt.sw.setChecked(!as.isFreezed());
        dt.sw.setTag(as);
        dt.sw.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppStat as = ((AppStat)v.getTag());
                as.TogEnabled();
                Toast.makeText(ctx, as.longName, Toast.LENGTH_SHORT).show();
                //apphelper.sort();
            }
        });

        return view;
    }

    public void TogEnabled(TagData td){
        td.as.TogEnabled();
    }
}
