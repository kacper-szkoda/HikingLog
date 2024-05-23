package be.kuleuven.gt.hikinglog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.fragments.PathDisplayFragment;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;

public class PathDialog extends Dialog {
    PathDialog dialog;
    Button btnDelete;
    BaseActivity father;
    private PathDisplayFragment pathDisplayFragment;
    private FragmentContainerView fcv;
    private final String pathName;
    private final int profileId;

    public PathDialog(@NonNull Context context, String pathName, int profileId, BaseActivity father) {
        super(context);
        this.profileId = profileId;
        this.pathName = pathName;
        dialog = this;
        this.father = father;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog.setContentView(R.layout.path_profile_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fcv = dialog.findViewById(R.id.fragmentContainerView);
        pathDisplayFragment = fcv.getFragment();
        btnDelete = dialog.findViewById(R.id.btnDeleteFromDialog);
        setUpDialog(pathName, profileId);
        setUpListeners();
    }

    public void setUpDialog(String pathName, int profileId) {
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

    public void setUpListeners() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int usrId = sharedPreferences.getInt("usrId", 1);
        if (!(profileId == usrId)) {
            btnDelete.setVisibility(View.INVISIBLE);
            btnDelete.setEnabled(false);
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapState.INSTANCE.deleteEntirePathFromDialog(pathName, new VolleyCallback() {
                    @Override
                    public void onSuccess(String stringResponse) {
                        resetProfile();
                    }
                });
            }
        });
    }

    private void resetProfile() {
        dialog.dismiss();
        father.goToProfile();
//        father.setUpPathModels();
    }
}
