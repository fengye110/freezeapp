package ted.com.freezeapp.other;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.EventListener;

import ted.com.freezeapp.helper.ShellHelper;

/**
 * Created by fy1 on 15/05/28.
 */
public class AppStat implements Comparable<AppStat>{
    ArrayList<FreezStatChangedListener> listeners = new ArrayList<FreezStatChangedListener>();
    public String shortName;
    public String longName;
    public String apkpath;
    public boolean isUserApp;
    public boolean isEnabled;
    public Drawable micon;
    public ApplicationInfo ainfo;

    private void trigerEvent(){
        Log.d("---", String.format("%s enabled=%b", longName, isEnabled));
        for(FreezStatChangedListener l : listeners){
            l.onFreezStatChanged();
        }
    }

    public AppStat(){
        shortName = "";
        longName = "";
        apkpath = "";
    }

    public void setType(Boolean isUser, Boolean isEnabled){
        this.isUserApp = isUser;
        this.isEnabled = isEnabled;
    }

    public void setEnabled(Boolean enabled){
        if(this.isEnabled != enabled) {
            this.isEnabled = enabled;
            trigerEvent();
        }
    }

    public void TogEnabled(){
        isEnabled = !isEnabled;
        trigerEvent();
        ShellHelper.pm_freez(longName, !isEnabled);
    }

    public Boolean isFreezed(){
        return !isEnabled;
    }

    public Boolean isUserApp(){
        return isUserApp;
    }

    public void addFreezStateChangedListener(FreezStatChangedListener listener){
        listeners.add(listener);
    }

    @Override
    public int compareTo(AppStat another) {
        if(this.isEnabled){
             if(another.isEnabled) {
                 return this.longName.compareTo(another.longName);
             }else{
                 return -1;
             }
        }else{
            if(another.isEnabled){
                return 1;
            }else{
                return this.longName.compareTo(another.longName);
            }
        }
    }

    public static interface FreezStatChangedListener extends EventListener {
        public void onFreezStatChanged();
    }

}
