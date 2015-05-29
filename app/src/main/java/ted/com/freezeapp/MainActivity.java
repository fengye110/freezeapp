package ted.com.freezeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    ListView lv_app;
    AppsAdaptor useAppAdapter;
    AppsAdaptor sysAppAdapter;
    AppsAdaptor curAdapter;

    View.OnClickListener uninstaler_handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AppsAdaptor.TagData td = (AppsAdaptor.TagData)v.getTag();
            td.swiplayout.toggle();
            Log.d("---", "uninstall clicked");

            final Dialog d = new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(String.format(" Do you want to uninstall \n\n\t%s (%s)" , td.as.shortName, td.as.longName))
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "No", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "yo", Toast.LENGTH_SHORT).show();
                            curAdapter.uninstall_app(td);
                            //lv_app.invalidateViews();
                            //lv_app.invalidate();
                        }
                    }).create();
            d.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_app =  (ListView)findViewById(R.id.lv_apps);
        useAppAdapter = new AppsAdaptor(getApplicationContext(), getPackageManager(),true);
        sysAppAdapter = new AppsAdaptor(getApplicationContext(), getPackageManager(),false);
        useAppAdapter.setUninstalerHandler(uninstaler_handler);
        sysAppAdapter.setUninstalerHandler(uninstaler_handler);

        //lv_app.setAdapter(useAppAdapter);
        curAdapter = useAppAdapter;
        lv_app.setAdapter(curAdapter);
        lv_app.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppsAdaptor.TagData td = (AppsAdaptor.TagData)view.getTag();
                Toast.makeText(getApplicationContext(), td.as.longName, Toast.LENGTH_SHORT).show();
                curAdapter.TogEnabled(td);
            }
        });
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
