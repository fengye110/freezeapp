package ted.com.freezeapp.Adpters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.Collections;

import ted.com.freezeapp.R;
import ted.com.freezeapp.helper.AppsHelper;
import ted.com.freezeapp.helper.Ls;
import ted.com.freezeapp.other.AppStat;
import ted.com.freezeapp.other.TagData;

/**
 * Created by fy1 on 15/05/28.
 */
public class InstallAppAdaptor extends BaseAdapter {
    LayoutInflater inflater;
    Context  ctx;
    SwipeLayout openedSwipeLayout = null;
    ArrayList<AppStat> appsInDir =  new ArrayList<>();
    ArrayList<String> toInstallApps =  new ArrayList<>();
    PackageManager pm;

    public void reflush()
    {
        notifyDataSetChanged();
    }

    public InstallAppAdaptor(Context context){
        this.ctx = context;
        this.pm = context.getPackageManager();
        inflater = LayoutInflater.from(context);
    }

    public void loadData(String dir){
        appsInDir.removeAll(appsInDir);
        toInstallApps.removeAll(toInstallApps);

        PackageInfo ifo;
        ApplicationInfo ainfo;
        for(String fpath: Ls.ls(dir, ".apk")){
            ifo = pm.getPackageArchiveInfo(fpath, PackageManager.GET_ACTIVITIES);
            ainfo = ifo.applicationInfo;
            if(ifo != null){
                AppStat as = new AppStat();
                as.shortName = fpath.substring(fpath.lastIndexOf('/')+1).split(".apk")[0];
                as.longName = (String) ainfo.loadLabel(pm);
                as.apkpath = fpath;

                ainfo.publicSourceDir = fpath;
                ainfo.sourceDir = fpath;
                as.ainfo = ainfo;
                as.isEnabled = true;
                appsInDir.add(as);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(appsInDir != null)
            return appsInDir.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return appsInDir.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TagData dt;
        if( view == null){
            view = inflater.inflate(R.layout.lv_install_item, parent, false);
            dt = new TagData();
            dt.icon = (ImageView) view.findViewById(R.id.iv_icon);
            dt.shortname = (TextView) view.findViewById(R.id.iv_short_name);
            dt.longname = (TextView) view.findViewById(R.id.iv_longname);
            dt.sw = (Switch)view.findViewById(R.id.sw_enabled);

            view.setTag(dt);
        }

        dt = (TagData)view.getTag();

        AppStat as = (AppStat)getItem(position);
        dt.icon.setImageDrawable(as.ainfo.loadIcon(pm));
        dt.longname.setText(as.longName);
        dt.shortname.setText(as.shortName);
        dt.as = as;

        dt.sw.setChecked(as.isEnabled);
        dt.sw.setTag(dt);
        dt.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TagData td = (TagData) buttonView.getTag();
                td.as.isEnabled = isChecked;
                if(isChecked){
                    toInstallApps.add(td.as.apkpath);
                }else{
                    toInstallApps.remove(td.as.apkpath);
                }
            }
        });

        return view;
    }

    public void doInstall(){
        for(String f: toInstallApps){
            Log.d("---", "install: " + f);
        }
    }

    public void TogEnabled(TagData td){
        td.sw.toggle();
    }
}
