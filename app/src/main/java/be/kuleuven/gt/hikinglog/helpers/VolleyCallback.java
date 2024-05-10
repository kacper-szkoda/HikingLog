package be.kuleuven.gt.hikinglog.helpers;

import static android.app.ProgressDialog.show;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface VolleyCallback {
    void onSuccess(String stringResponse);
}
