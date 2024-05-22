package be.kuleuven.gt.hikinglog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import java.text.BreakIterator;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.fragments.ProfileFragment;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.UserState;

public class ChangeUsernameDialog extends Dialog {
    private Button dialogBtnChange, dialogBtnCancel;
    private EditText txtInput;
    ChangeUsernameDialog dialog;
    ProfileFragment father;


    public ChangeUsernameDialog(@NonNull Context context, ProfileFragment father) {
        super(context);
        dialog = this;
        this.father = father;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(R.layout.confirm_path_dialog);
                dialog.setCancelable(false);
                dialogBtnChange = dialog.findViewById(R.id.btnDialogSave);
                dialogBtnCancel = dialog.findViewById(R.id.btnDialogDelete);
                TextView txtPrompt = dialog.findViewById(R.id.txtDialogSave);
                txtInput = dialog.findViewById(R.id.inputPathname);
                dialogBtnChange.setText("Change");
                dialogBtnCancel.setText("Cancel");
                txtPrompt.setText("Change username");
                setUpButtons();
    }
    public void setUpButtons(){
        Context context = getContext();
        ChangeUsernameDialog dialog = this;
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBtnChange.setEnabled(false);
                if (dialog.getTxtInput().getText().toString().isEmpty()) {
                    Toast.makeText(context, "Input username", Toast.LENGTH_SHORT).show();
                } else if (dialog.getTxtInput().getText().toString().contains(".")) {
                    Toast.makeText(context, "Username should not contain periods", Toast.LENGTH_SHORT).show();
                } else {
                    father.changeUsername(dialog.getTxtInput().getText().toString());
                }
                dialogBtnChange.setEnabled(true);
                dialog.dismiss();
            }
        });
    }

    public Button getDialogBtnChange() {
        return dialogBtnChange;
    }

    public Button getDialogBtnCancel() {
        return dialogBtnCancel;
    }

    public EditText getTxtInput() {
        return txtInput;
    }
}
