package ted.com.freezeapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import ted.com.freezeapp.other.AppStat;
import ted.com.freezeapp.Adpters.AppsAdaptor;
import ted.com.freezeapp.R;
import ted.com.freezeapp.other.TagData;


/**
 * A fragment representing a list of Items.
 */
public class AppsInfoFragment extends Fragment implements AbsListView.OnItemClickListener,View.OnClickListener, AbsListView.OnScrollListener{

    private static final String ARG_ISUSER_APP = "is_USER_APP";

    private Boolean showUserApps;
    private AbsListView mListView;

    public AppsAdaptor mAdapter;

    public static AppsInfoFragment newInstance(Boolean a_showUserApps) {
        AppsInfoFragment fragment = new AppsInfoFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ISUSER_APP, a_showUserApps);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppsInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            showUserApps = getArguments().getBoolean(ARG_ISUSER_APP);
        }

        // create adapter
        mAdapter = new AppsAdaptor(getActivity().getApplicationContext(), getActivity().getPackageManager(),showUserApps);
        mAdapter.setUninstalerHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.lv_apps);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        view.setTag(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TagData td = (TagData)view.getTag();
        Toast.makeText(getActivity().getApplicationContext(), td.as.longName, Toast.LENGTH_SHORT).show();
        mAdapter.TogEnabled(td);
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if(tag instanceof AppStat){
            return;
        }

        final TagData td = (TagData)tag;
        td.swiplayout.toggle();
        Log.d("---", "uninstall clicked");

        final Dialog d = new AlertDialog.Builder(getActivity().getApplicationContext())
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
                        mAdapter.uninstall_app(td);
                        //lv_app.invalidateViews();
                        //lv_app.invalidate();
                    }
                }).create();
        d.show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mAdapter.closeOpendSwipeLayotu();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
