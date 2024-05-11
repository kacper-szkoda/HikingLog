package be.kuleuven.gt.hikinglog.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import be.kuleuven.gt.hikinglog.state.PathModel;
import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.fragments.PathDisplayFragment;

public class PathRecyclerViewAdapter extends RecyclerView.Adapter<PathRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PathModel> pathModels;
    int profileId;

    public PathRecyclerViewAdapter(Context context, ArrayList<PathModel> pathModels, int profileId) {
        this.context = context;
        this.pathModels = pathModels;
        this.profileId = profileId;
    }

    @NonNull
    @Override
    public PathRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_view_row_path, parent, false);
        return new PathRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.pathNameTxt.setText(pathModels.get(position).getPathName());
        holder.setPathName(pathModels.get(position).getPathName());
        holder.setProfileId(profileId);
    }

    @Override
    public int getItemCount() {
        return pathModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Dialog dialogPath;
        TextView pathNameTxt;
        Button btnView;
        String pathName;
        int profileId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pathNameTxt = itemView.findViewById(R.id.txtPathname);
            pathNameTxt.setText(pathName);
            btnView = itemView.findViewById(R.id.btnViewPath);
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayMapDialog();
                }
            });


        }

        public void displayMapDialog() {
            dialogPath = new Dialog(itemView.getContext());
            dialogPath.setContentView(R.layout.path_profile_dialog);
            dialogPath.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FragmentContainerView fcv = dialogPath.findViewById(R.id.fragmentContainerView);
            PathDisplayFragment pathDisplayFragment = fcv.getFragment();
            pathDisplayFragment.setPathName(pathName);
            pathDisplayFragment.setProfileId(profileId);
            dialogPath.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    PathDisplayFragment mapFragProf = fcv.getFragment();
                    androidx.fragment.app.FragmentManager fragmentManager = mapFragProf.getFragMan();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(mapFragProf);
                    fragmentTransaction.commit();
                }
            });
            dialogPath.show();
            TextView txt = dialogPath.findViewById(R.id.txtPathnameProfile);
            txt.setText(pathName);

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setReorderingAllowed(true);
//            transaction.replace(R.id.fragmentContainerView, new PathDisplayFragment());
//            transaction.commit();


//            pathDisplayFragment.recoverPath();

            this.getAdapterPosition();
        }

        public void setPathName(String pathName) {
            this.pathName = pathName;
        }
        public void setProfileId(int profileId){this.profileId = profileId;}
    }


}
