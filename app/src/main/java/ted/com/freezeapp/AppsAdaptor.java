package ted.com.freezeapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fy1 on 15/05/28.
 */
public class AppsAdaptor extends BaseAdapter implements AppStat.FreezStatChangedListener{

    Boolean userApps;
    AppsHelper apphelper;
    LayoutInflater inflater;
    Object thiiz;
    Context  ctx;

    public AppsAdaptor(Context context, PackageManager pm, Boolean userApps){
        this.userApps = userApps;
        apphelper = new AppsHelper(context, pm);
        thiiz = this;
        this.ctx = context;
        apphelper.addFreezStatChangedListener(new AppStat.FreezStatChangedListener(){
            @Override
            public void onFreezStatChanged() {
                ((AppsAdaptor)thiiz).notifyDataSetChanged();
            }
        });

        if(this.userApps)
            apphelper.loadUserAppsInfo();
        else
            apphelper.loadSysAppsInfo();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return apphelper.apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apphelper.apps.get(position);
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

            view.setTag(dt);
        }

        dt = (TagData)view.getTag();

        AppStat as = (AppStat)getItem(position);
        dt.icon.setImageDrawable(apphelper.icon(as.longName));
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

    @Override
    public void onFreezStatChanged() {
        this.notifyDataSetChanged();
    }

    public static class TagData {
        public ImageView icon;
        public TextView longname;
        public Switch sw;
        public TextView shortname;
        public AppStat as;
    }

    public void TogEnabled(TagData td){
        td.as.TogEnabled();
        //apphelper.sort();
    }
}
