package be.kuleuven.gt.hikinglog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import be.kuleuven.gt.hikinglog.mapstate.MapState;
import be.kuleuven.gt.hikinglog.mapstate.SQLControl;
import be.kuleuven.gt.hikinglog.mapstate.VolleyCallback;

public class HomeFragment extends Fragment {

    MapFragment mapFragment;
    Button startBtn, dialogBtnSave, dialogBtnDelete;
    boolean started;
    Dialog dialogSave;
    SharedPreferences sharedPref;
    MapState mapState;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("usrId", 1);
            editor.putInt("started", 0);
            editor.apply();
//            EdgeToEdge.enable(this);
//            setContentView(R.layout.activity_maps_screen);

            maps_screen mapsScreen = (maps_screen) getActivity();
            mapState = mapsScreen.returnMapState();

            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.fragMap);
            startBtn = (Button) view.findViewById(R.id.btnStart);
            startBtn.setText(R.string.btnStartValue);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getStarted()){
                        startBtn.setText(R.string.btnStopValue);
                        startBtn.setEnabled(false);
                        mapState.startPath(sharedPref.getInt("usrId", 1), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                mapState.getLatestPathId(sharedPref.getInt("usrId", 1), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONArray jsonArray) {
                                        try {
                                            int pathId = jsonArray.getJSONObject(0).getInt("idpaths");
                                            editor.putInt("latestPathId", pathId);
                                            editor.putInt("started", 1);
                                            editor.apply();
                                            mapFragment.onStartBtn();
                                            startBtn.setEnabled(true);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            }
                        });
                    }
                    else {
                        mapFragment.onStopBtn();
                        dialogSave.show();
                        startBtn.setText(R.string.btnStartValue);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("started", 0);
                        editor.apply();
                    }
                }
            });

            dialogSave = new Dialog(this.getContext());
            dialogSave.setContentView(R.layout.confirm_path_dialog);
            dialogSave.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogSave.setCancelable(false);

            dialogBtnSave = dialogSave.findViewById(R.id.btnDialogSave);
            dialogBtnDelete = dialogSave.findViewById(R.id.btnDialogDelete);

            dialogBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapFragment.onStopBtn();
                    dialogSave.dismiss();
                }
            });
            dialogBtnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapFragment.onStopBtn();
                    mapFragment.saveCoords();
                    dialogSave.dismiss();
//                    Toast.makeText(HomeFragment.getPare.this, "Path saved", Toast.LENGTH_SHORT).show();
                }
            });

//            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//                return insets;
//            });

        return view;
    }


    public boolean getStarted(){
        sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPref.getInt("started", 0) != 0;
    }
//    public void onBtnStart_Clicked(View Caller){
//        if (!started){
//            startBtn.setText(R.string.btnStopValue);
//            mapFragment.onStartBtn();
//            started = true;
//        }
//        else {
//            mapFragment.onStopBtn();
//
//            dialogSave.show();
//            startBtn.setText(R.string.btnStartValue);
//            started = false;
//        }
//    }
}