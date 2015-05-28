package ted.com.freezeapp;

import android.graphics.drawable.Drawable;

/**
 * Created by fy1 on 15/05/28.
 */
public class AppStat {

    String shortName;
    String longName;
    String apkpath;
    boolean isUserApp;
    boolean isEnabled;

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
        this.isEnabled = enabled;
    }

    public Boolean isFreezed(){
        return !isEnabled;
    }

    public Boolean isUserApp(){
        return isUserApp;
    }
}
