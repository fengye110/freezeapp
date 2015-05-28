package ted.com.freezeapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fy1 on 15/05/28.
 */
public class AppsHelper {
    public ArrayList<AppStat> apps = new ArrayList<AppStat>();

    Context context;
    PackageManager pm;


    public AppsHelper(Context a_context, PackageManager a_pm){
        context = a_context;
        pm = a_pm;
    }

    void _loadAppsInfo(Boolean isuser){
        ApplicationInfo ifo;

        List<PackageInfo> ls_apps = pm.getInstalledPackages(0);

        // remove all items
        apps.removeAll(apps);

        // get user/sys app list
        for (PackageInfo pinfo : ls_apps) {
            ifo = pinfo.applicationInfo;
            AppStat as = new AppStat();
            as.longName = ifo.packageName;
            as.apkpath = ifo.publicSourceDir;
            as.shortName = (String)pm.getApplicationLabel(ifo);

            as.setType( (ifo.flags & ApplicationInfo.FLAG_SYSTEM) == 0, ifo.enabled);

            if(as.isUserApp() == isuser){
                apps.add(as);
            }
        }
    }
    public void loadSysAppsInfo(){
        _loadAppsInfo(false);
    }

    public void loadUserAppsInfo(){
        _loadAppsInfo(true);
    }


    public Drawable icon(String name)
    {
        Drawable icon = null;
        try {
            icon = pm.getApplicationIcon(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }
}
