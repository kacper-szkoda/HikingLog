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

import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.dialogs.PathDialog;
import be.kuleuven.gt.hikinglog.fragments.ProfileFragment;
import be.kuleuven.gt.hikinglog.state.PathModel;
import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.fragments.PathDisplayFragment;

public class PathRecyclerViewAdapter extends RecyclerView.Adapter<PathRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PathModel> pathModels;
    int profileId;
    BaseActivity father;

    public PathRecyclerViewAdapter(Context context, ArrayList<PathModel> pathModels, int profileId, BaseActivity father) {
        this.context = context;
        this.pathModels = pathModels;
        this.profileId = profileId;
        this.father = father;
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
        holder.setFather(father);
    }

    @Override
    public int getItemCount() {
        return pathModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private PathDialog dialogPath;
        TextView pathNameTxt;
        Button btnView;
        String pathName;
        int profileId;
        BaseActivity father;

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
            dialogPath = new PathDialog(itemView.getContext(), pathName, profileId, father);
            dialogPath.show();
            this.getAdapterPosition();
        }

        public void setPathName(String pathName) {
            this.pathName = pathName;
        }
        public void setProfileId(int profileId){this.profileId = profileId;}
        public void setFather(BaseActivity father){
            this.father = father;
        }
    }
}
