package ted.com.freezeapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    ListView lv_app;
    AppsAdaptor useAppAdapter;
    AppsAdaptor sysAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_app =  (ListView)findViewById(R.id.lv_apps);
        useAppAdapter = new AppsAdaptor(getApplicationContext(), getPackageManager(),true);
        sysAppAdapter = new AppsAdaptor(getApplicationContext(), getPackageManager(),false);

        //lv_app.setAdapter(useAppAdapter);
        lv_app.setAdapter(sysAppAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
