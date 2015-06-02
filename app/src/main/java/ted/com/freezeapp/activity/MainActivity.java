package ted.com.freezeapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import ted.com.freezeapp.helper.AppsHelper;
import ted.com.freezeapp.Adpters.LoadDisabledAppsAdaptor;
import ted.com.freezeapp.R;


public class MainActivity extends ActionBarActivity implements MaterialTabListener{

    MaterialTabHost tabHost;
    ViewPager pager;
    XPagerAdapter pg_adapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(""); //must set before this.setSupportActionBar call
        this.setSupportActionBar(toolbar);


        // pager init
        pager = (ViewPager) findViewById(R.id.pager);
        pg_adapter = new XPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pg_adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { return ; }

        });

        // table host initial
        tabHost = (MaterialTabHost) findViewById(R.id.tabHost);
        for (int i=0; i<2;i++) {
            tabHost.addTab( tabHost.newTab().setText(pg_adapter.Title(i))
                                            .setTabListener(this)
                );
        }
    }

    @Override
    protected void onPause() {
        AppsHelper.saveDisableAppInfosToSDCard(getPackageManager());
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    Dialog progressDialog;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == -1){
                progressDialog.dismiss();
            }
        }
    };


    public void getWifiPasswd()
    {
        String path = "/data/misc/wifi/wpa_supplicant.conf";
        Toast.makeText(getApplicationContext(), path,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_prev_disabled_sys_apps) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.loaddb_dialog);
            dialog.setTitle("prev disabled sys app");
            ListView lv = (ListView) dialog.findViewById(R.id.lv_loaddisable_list);
            BaseAdapter ad = new LoadDisabledAppsAdaptor(dialog.getContext(), getPackageManager());
            lv.setAdapter(ad);
            dialog.show();
            return true;
        }

        if(id == R.id.action_share){
            //Intent   tent = new Intent(this, InstallAppsFromSDActivity.class);
            //startActivity(tent);
        }
        if(id == R.id.action_startInstallActivy){
            Intent   tent = new Intent(this, InstallAppsFromSDActivity.class);
            startActivity(tent);
        }
        if(id == R.id.action_getWifiPwD){
            getWifiPasswd();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        pager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    public class XPagerAdapter extends FragmentStatePagerAdapter {

        public XPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    return AppsInfoFragment.newInstance(true);
                case 0:
                    return AppsInfoFragment.newInstance(false);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        public String Title(int position){
            if(position == 1)
                return getString(R.string.UserApps);
            else
                return getString(R.string.SystemApps);
        }
    }
}
