package be.kuleuven.gt.hikinglog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.fragments.PathDisplayFragment;

public class PathDialog extends Dialog {
    private PathDisplayFragment pathDisplayFragment;
    private FragmentContainerView fcv;
    private String pathName;
    private int profileId;
    PathDialog dialog;
    public PathDialog(@NonNull Context context, String pathName, int profileId) {
        super(context);
        this.profileId = profileId;
        this.pathName = pathName;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog.setContentView(R.layout.path_profile_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fcv = (FragmentContainerView) dialog.findViewById(R.id.fragmentContainerView);
        pathDisplayFragment = (PathDisplayFragment) fcv.getFragment();
        setUpDialog(pathName, profileId);
    }
    public void setUpDialog(String pathName, int profileId){
        pathDisplayFragment.setPathName(pathName);
        pathDisplayFragment.setProfileId(profileId);
        this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                PathDisplayFragment mapFragProf = fcv.getFragment();
                androidx.fragment.app.FragmentManager fragmentManager = mapFragProf.getFragMan();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(mapFragProf);
                fragmentTransaction.commit();
            }
        });
        TextView txt = this.findViewById(R.id.txtPathnameProfile);
        txt.setText(pathName);
    }
}
