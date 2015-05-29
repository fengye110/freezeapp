package ted.com.freezeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

/**
 * Created by fy1 on 15/05/28.
 */
public class AppsAdaptor extends BaseAdapter implements AppStat.FreezStatChangedListener{

    Boolean userApps;
    AppsHelper apphelper;
    LayoutInflater inflater;
    Object thiiz;
    Context  ctx;

    View.OnClickListener uninstaler_btn_clicked_clicked = null;

    void _loadAppsInfo()
    {
        if(this.userApps)
            apphelper.loadUserAppsInfo();
        else
            apphelper.loadSysAppsInfo();
    }

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

        _loadAppsInfo();
        inflater = LayoutInflater.from(context);
    }


    public void setUninstalerHandler(View.OnClickListener l){
        uninstaler_btn_clicked_clicked = l;
    }

    public void uninstall_app(TagData td){
        ShellHelper.uninstall_app(td.as.longName);
        apphelper.apps.remove(td.as);
        //_loadAppsInfo();
        //notifyDataSetInvalidated();
        notifyDataSetChanged();
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
            dt.uninstall_btn = (ImageButton) view.findViewById(R.id.btn_uninstall);

            view.setTag(dt);

            dt.swiplayout = (SwipeLayout)view.findViewById(R.id.swipelayout);
            dt.swiplayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            dt.swiplayout.setDragEdge(SwipeLayout.DragEdge.Right);

            if( !this.userApps){
                dt.swiplayout.setSwipeEnabled(false);
            }
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


        //uninstaller app
        dt.uninstall_btn.setTag(dt);
        dt.uninstall_btn.setOnClickListener( uninstaler_btn_clicked_clicked);

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
        public ImageButton uninstall_btn;
        public AppStat as;
        public SwipeLayout swiplayout;
    }

    public void TogEnabled(TagData td){
        td.as.TogEnabled();
        //apphelper.sort();
    }
}
