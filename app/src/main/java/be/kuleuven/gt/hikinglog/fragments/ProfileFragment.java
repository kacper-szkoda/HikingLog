package be.kuleuven.gt.hikinglog.fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.adapter.PathRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.FriendModel;
import be.kuleuven.gt.hikinglog.state.MapState;
import be.kuleuven.gt.hikinglog.state.PathModel;
import be.kuleuven.gt.hikinglog.state.UserState;

public class ProfileFragment extends Fragment {
    private ArrayList<PathModel> usrPaths;
    private TextView txtUsername;
    private MapState mapState;
    private int profileId;
    private BaseActivity mapsScreen;
    private Dialog dialogChange;
    private Button dialogBtnChange, dialogBtnCancel, btnAddChange;
    private int usrId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        usrId = sharedPreferences.getInt("usrId", 1);
        Bundle args = getArguments();
        profileId = args.getInt("profileId", 1);
        usrPaths = new ArrayList<PathModel>();
        mapsScreen = (BaseActivity) getActivity();
        mapState = mapsScreen.returnMapState();
        btnAddChange = view.findViewById(R.id.btnAdd_Change);
        txtUsername = view.findViewById(R.id.txtProfUsername);

        if (profileId == sharedPreferences.getInt("usrId", 1)) {
            txtUsername.setText(sharedPreferences.getString("username", ""));
        } else {
            txtUsername.setText(args.getString("username"));
        }
        setUpProfileButton();
        setUpPathModels();

        setUpSearch(view);
        return view;
    }

    private void setUpSearch(View view) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO implement dynamic suggestions with like
                return false;
            }
        });
    }

    public void setUpPathModels() {
        //TODO implement buttons on the dialogs, refactor the dialog to be generic for the accepts and cancels below
        mapState.getPathsPerUser(profileId, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(stringResponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String name = object.getString("pathname");
                        usrPaths.add(new PathModel(name));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                RecyclerView recyclerView = mapsScreen.findViewById(R.id.recyclerPaths);
                PathRecyclerViewAdapter adapter = new PathRecyclerViewAdapter(mapsScreen, usrPaths, profileId);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mapsScreen));
            }
        });
    }

    public void setUpProfileButton() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (profileId == sharedPreferences.getInt("usrId", 1)) {
            btnAddChange.setText("Change");
            btnAddChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogChange = setUpDialog();
                    dialogChange.show();
                }
            });
        } else {
            btnAddChange.setText("Add");
            btnAddChange.setEnabled(false);
            setUpAddButton();
        }
    }

    public Dialog setUpDialog() {
        dialogChange = new Dialog(getContext());
        dialogChange.setContentView(R.layout.confirm_path_dialog);
        dialogChange.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogChange.setCancelable(false);

        dialogBtnChange = dialogChange.findViewById(R.id.btnDialogSave);
        dialogBtnCancel = dialogChange.findViewById(R.id.btnDialogDelete);
        TextView txtPrompt = dialogChange.findViewById(R.id.txtDialogSave);
        TextInputEditText txtInput = dialogChange.findViewById(R.id.inputPathname);

        dialogBtnChange.setText("Change");
        dialogBtnCancel.setText("Cancel");
        txtPrompt.setText("Change username");

        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChange.dismiss();
            }
        });
        dialogBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBtnChange.setEnabled(false);
                if (txtInput.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getBaseContext(), "Input username", Toast.LENGTH_SHORT).show();
                } else if (txtInput.getText().toString().contains(".")) {
                    Toast.makeText(getActivity().getBaseContext(), "Username should not contain periods", Toast.LENGTH_SHORT).show();
                } else {
                    changeUsername(txtInput.getText().toString());
                }
                dialogBtnChange.setEnabled(true);
                dialogChange.dismiss();
            }
        });
        return dialogChange;
    }

    public void setUpAddButton() {
        UserState.INSTANCE.findFriends(new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                ArrayList<FriendModel> friendModels = new ArrayList<>();
                friendModels = FriendModel.getFriendsFromJSON(stringResponse, usrId);
                if (!(friendModels.stream()
                        .mapToInt(FriendModel::getIdprofile)
                        .anyMatch(id -> id == profileId))) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnAddChange.setEnabled(true);
                            btnAddChange.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btnAddChange.setEnabled(false);
                                    UserState.INSTANCE.addFriend(profileId, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(String stringResponse) {

                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void changeUsername(String newUsername) {
        btnAddChange.setEnabled(false);
        UserState.INSTANCE.changeUsername(newUsername, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                btnAddChange.setEnabled(true);
                SharedPreferences.Editor editor = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                editor.putString("username", newUsername);
                editor.apply();
                txtUsername.setText(newUsername);
            }
        });
    }
}