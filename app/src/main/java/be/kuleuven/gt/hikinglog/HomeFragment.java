package be.kuleuven.gt.hikinglog;

import android.app.Dialog;
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

public class HomeFragment extends Fragment {

    MapFragment mapFragment;
    Button startBtn, dialogBtnSave, dialogBtnDelete;
    boolean started;
    Dialog dialogSave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            View view = inflater.inflate(R.layout.fragment_home, container, false);

//            EdgeToEdge.enable(this);
//            setContentView(R.layout.activity_maps_screen);

            started = false;
            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.fragMap);
            startBtn = (Button) view.findViewById(R.id.btnStart);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!started){
                        startBtn.setText(R.string.btnStopValue);
                        mapFragment.onStartBtn();
                        started = true;
                    }
                    else {
                        mapFragment.onStopBtn();

                        dialogSave.show();
                        startBtn.setText(R.string.btnStartValue);
                        started = false;
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