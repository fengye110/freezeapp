package ted.com.freezeapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by fy1 on 15/05/28.
 */
public class AppsHelper {
    public ArrayList<AppStat> apps = new ArrayList<AppStat>();
    AppStat.FreezStatChangedListener listener = null;

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
                if(this.listener != null)
                    as.addFreezStateChangedListener(listener);
            }
        }
        sort();
    }

    public void sort(){
        Collections.sort(apps);
    }

    public void addFreezStatChangedListener(AppStat.FreezStatChangedListener listener){
        this.listener = listener;
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

    public static void saveDisableAppInfosToSDCard(PackageManager pm) {
        String state = Environment.getExternalStorageState();
        if ( !Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        // create path
        String path=Environment.getExternalStorageDirectory() + "/freezapp";
        new File(path).mkdirs();

        try {
            File f = new File(path+"/"+backUpFileName);
            f.delete();
            f.createNewFile();
            BufferedWriter bwrite = new BufferedWriter( new FileWriter(f));
            for (String name : listDisabledSysApps(pm)){
                bwrite.write(name);
                bwrite.write("\n");
            }
            bwrite.flush();
            bwrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<AppStat> loaddisabledSysApps(PackageManager pm){
        ArrayList<AppStat> alist = new ArrayList<AppStat>();

        String dbfilepath = Environment.getExternalStorageDirectory() + "/freezapp/" + backUpFileName;
        File f = new File(dbfilepath);
        if(!f.exists()){
            return null;
        }

        try {
            ApplicationInfo ifo;
            BufferedReader read = new BufferedReader(new FileReader(f));
            while(true){
                String  data = read.readLine();
                if(data == null || data.length() == 0) {
                    break;
                }
                try {
                    ifo = pm.getApplicationInfo(data,0);
                    if(ifo != null){
                        AppStat as = new AppStat();
                        as.longName = ifo.packageName;
                        as.apkpath = ifo.publicSourceDir;
                        as.shortName = (String)pm.getApplicationLabel(ifo);

                        as.setType( (ifo.flags & ApplicationInfo.FLAG_SYSTEM) == 0, ifo.enabled);
                        alist.add(as);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return alist;
    }

    private static ArrayList<String> listDisabledSysApps(PackageManager pm) {
        ArrayList<String> alist = new ArrayList<String>();

        ApplicationInfo ifo;
        List<PackageInfo> ls_apps = pm.getInstalledPackages(0);

        // get user/sys app list
        for (PackageInfo pinfo : ls_apps) {
            ifo = pinfo.applicationInfo;
            if( (ifo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 && !ifo.enabled){
                alist.add(ifo.packageName);
            }
        }
        return alist;
    }

    static private String backUpFileName = "disabledApp.txt";
}
