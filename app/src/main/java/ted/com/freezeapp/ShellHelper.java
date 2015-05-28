package ted.com.freezeapp;

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
    public static void pm_freez(String longname, Boolean freez){
        Shell rootshell;
        try {
            rootshell = RootShell.getShell(true);
            String cmdstr = String.format("pm %s  %s", freez?"disable":"enable", longname);
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
}
