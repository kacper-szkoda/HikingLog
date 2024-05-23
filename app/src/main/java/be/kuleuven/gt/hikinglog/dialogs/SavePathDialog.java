package be.kuleuven.gt.hikinglog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.fragments.MapFragment;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;

public class SavePathDialog extends Dialog {

    SavePathDialog dialog;
    MapFragment mapFragment;
    private Button dialogBtnSave, dialogBtnDelete;
    private EditText textView;

    public SavePathDialog(@NonNull Context context, MapFragment mapFragment) {
        super(context);
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
        textView = this.findViewById(R.id.inputPathname);
        setUpListeners();
    }

    public void setUpListeners() {
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
                checkIfNoDuplicatePaths();
            }
        });
    }

    public void checkIfNoDuplicatePaths() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int usrId = sharedPreferences.getInt("usrId", 1);
        MapState.INSTANCE.getPathsPerUser(usrId, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                try {
                    JSONArray jsonArray = new JSONArray(stringResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String pathNameUsed = jsonObject.getString("pathname");
                        if (pathNameUsed.equals(textView.getText().toString())) {
                            Toast.makeText(getContext(), "This pathname is used already", Toast.LENGTH_LONG).show();
                            textView.setText("");
                            return;
                        }
                    }
                    mapFragment.onStopBtn();
                    mapFragment.savePath(textView.getText().toString());
                    dialog.dismiss();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
