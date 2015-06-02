package ted.com.freezeapp.activity;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ted.com.freezeapp.Adpters.InstallAppAdaptor;
import ted.com.freezeapp.R;
import ted.com.freezeapp.other.TagData;


public class InstallAppsFromSDActivity extends ActionBarActivity {
    ListView applist;
    InstallAppAdaptor madpater;
    EditText tv_dir;
    String dirpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_apps_from_sd);

        madpater = new InstallAppAdaptor(getApplicationContext());

        applist = (ListView) findViewById(R.id.lv_install_app);
        applist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TagData td = (TagData) view.getTag();
                madpater.TogEnabled(td);
            }
        });
        applist.setAdapter(madpater);

        // text view dir name
        tv_dir = (EditText) findViewById(R.id.tv_cur_dir);
        dirpath =Environment.getExternalStorageDirectory()+"/backups/apps/";
        tv_dir.setText(dirpath);
        tv_dir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    //Toast.makeText(getApplicationContext(), v.getText(), Toast.LENGTH_LONG).show();
                    String dirpath = (String)v.getText();
                    madpater.loadData( dirpath);
                    return true;
                }
                return false;
            }
        });

        madpater.loadData(dirpath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_install_apps_from_sd, menu);
        return true;
    }

    void do_installSelectedApps(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_install) {
            do_installSelectedApps();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
