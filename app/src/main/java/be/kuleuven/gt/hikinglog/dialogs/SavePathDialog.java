package be.kuleuven.gt.hikinglog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.fragments.MapFragment;
import be.kuleuven.gt.hikinglog.fragments.PathDisplayFragment;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;

public class SavePathDialog extends Dialog {

    private Button dialogBtnSave, dialogBtnDelete;
    SavePathDialog dialog;
    MapFragment mapFragment;
    public SavePathDialog(@NonNull Context context, MapFragment mapFragment) {
        super(context);;
        dialog = this;
        this.mapFragment = mapFragment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.confirm_path_dialog);
        this.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setCancelable(false);

        dialogBtnSave = this.findViewById(R.id.btnDialogSave);
        dialogBtnDelete = this.findViewById(R.id.btnDialogDelete);
        setUpListeners();
    }
    public void setUpListeners(){
        dialogBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.onStopBtn();
                MapState.INSTANCE.deletePath(new VolleyCallback() {
                    @Override
                    public void onSuccess(String stringResponse) {

                    }
                });
                MapState.INSTANCE.deletePathEntries(new VolleyCallback() {
                    @Override
                    public void onSuccess(String stringResponse) {

                    }
                });
                dialog.dismiss();
            }
        });
        dialogBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = dialog.findViewById(R.id.inputPathname);
                mapFragment.onStopBtn();
                mapFragment.saveCoords();
                mapFragment.savePath(textView.getText().toString());
                dialog.dismiss();
            }
        });
    }

}
