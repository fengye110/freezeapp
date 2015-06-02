package ted.com.freezeapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ted.com.freezeapp.Adpters.InstallAppAdaptor;
import ted.com.freezeapp.R;
import ted.com.freezeapp.helper.ShellHelper;
import ted.com.freezeapp.other.TagData;


public class InstallAppsFromSDActivity extends ActionBarActivity {
    ListView applist;
    InstallAppAdaptor madpater;
    EditText tv_dir;
    String dirpath;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_apps_from_sd);

        tb = (Toolbar) findViewById(R.id.toolbar_install_app);
        tb.setTitle("");
        setSupportActionBar(tb);

        // create list adpater
        madpater = new InstallAppAdaptor(getApplicationContext());

        // list setting
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
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Toast.makeText(getApplicationContext(), v.getText(), Toast.LENGTH_LONG).show();
                    String dirpath = (String) v.getText();
                    madpater.loadData(dirpath);
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


    void do_installSelectedApps() {
        if(madpater.toInstallApps.size() > 0){
            curidx = 0;
            Intent tent = new Intent(Intent.ACTION_VIEW);
            tent.setDataAndType(Uri.parse("file://" + madpater.toInstallApps.get(curidx)), "application/vnd.android.package-archive");
            startActivityForResult(tent, install_app_code_base);
            curidx++;
        }
    }
    void do_installSelectedApps_deleted(){
        final Dialog progdialog = new Dialog(this);
        progdialog.setContentView(R.layout.install_process_dialog);
        int curnums =0;
        final int totalnums = madpater.toInstallApps.size();

        progdialog.setTitle("progress");
        progdialog.setCancelable(false);

        final TextView tvnums = (TextView) progdialog.findViewById(R.id.tv_apps_nums);
        final TextView appname = (TextView) progdialog.findViewById(R.id.tv_appname);
        final ProgressBar pb = (ProgressBar) progdialog.findViewById(R.id.progbar);

        tvnums.setText(String.format("%d/%d", curnums, totalnums));
        appname.setText("");
        pb.setProgress(0);

        final Handler handler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what <= 0){
                    progdialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Install Done", Toast.LENGTH_LONG).show();
                }else{
                    pb.setProgress(msg.what*100/totalnums);
                    tvnums.setText(String.format("%d/%d", msg.what, totalnums));
                    appname.setText(msg.obj.toString());
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                final int nums = madpater.toInstallApps.size();
                int curidx = 0;

                for(final String fpath: madpater.toInstallApps){
                    curidx++;
                    Message msg = Message.obtain();
                    msg.what = curidx;
                    msg.obj = fpath.substring(fpath.lastIndexOf('/') + 1).split(".apk")[0];
                    handler.sendMessage(msg);

                    // do job
                    // ShellHelper.install_app(fpath);
                    Intent tent = new Intent(Intent.ACTION_VIEW);
                    tent.setDataAndType(Uri.parse("file://"+fpath), "application/vnd.android.package-archive");
                    startActivityForResult(tent,curidx);
                }

                handler.sendEmptyMessage(-1);
            }
        });

        progdialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == install_app_code_base){

            if(curidx == madpater.toInstallApps.size()){
                Toast.makeText(getApplicationContext(),"Install Done",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), String.format("%d/%d", curidx, madpater.toInstallApps.size()), Toast.LENGTH_SHORT).show();
            }

            if(curidx < madpater.toInstallApps.size()) {
                Intent tent = new Intent(Intent.ACTION_VIEW);
                tent.setDataAndType(Uri.parse("file://" + madpater.toInstallApps.get(curidx)), "application/vnd.android.package-archive");
                startActivityForResult(tent, install_app_code_base);
                curidx++;
            }
        }
    }
    public int curidx = 0;
    public int install_app_code_base=400;
}
