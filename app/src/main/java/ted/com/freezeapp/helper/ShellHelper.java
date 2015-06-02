package ted.com.freezeapp.helper;

/**
 * Created by fy1 on 15/05/28.
 */


import android.util.Log;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ShellHelper {

    static void thread_run_cmd(final String cmdstr, final Boolean root){
        new Thread( new Runnable() {
            @Override
            public void run() {
                _run_cmd(cmdstr, root);
            }
        }).start();
    }

    static void _run_cmd(String cmdstr, Boolean root){
        try {
            Shell rootshell;
            rootshell = RootShell.getShell(root);
            if(rootshell == null) {
                return;
            }
            Log.d("---", "rootshell: cmd=" + cmdstr);

            Command cmd = new Command(0, cmdstr){
                @Override
                public void commandOutput(int id, String line){
                    Log.d("---", line);
                    //update();
                }
                @Override
                public void commandCompleted(int id, int exitcode){
                    Log.d("---", "exec complete");
                }
            };
            // run cmd
            rootshell.add(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        }

    }

    public static void install_app(String apk){
        String cmdstr = String.format("pm install  %s", apk);
        _run_cmd(cmdstr, false);
    }
    public static void uninstall_app(String name){
        String cmdstr = String.format("pm uninstall  %s", name);
        thread_run_cmd(cmdstr, false);
    }



    public static void pm_freez(String a_longname, Boolean a_freez){
        String cmdstr = String.format("pm %s  %s", a_freez?"disable":"enable", a_longname);
        thread_run_cmd(cmdstr, true);
    }
}
