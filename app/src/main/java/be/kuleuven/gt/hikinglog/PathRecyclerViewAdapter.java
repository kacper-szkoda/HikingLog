package be.kuleuven.gt.hikinglog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.mapstate.PathModel;

public class PathRecyclerViewAdapter extends RecyclerView.Adapter<PathRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PathModel> pathModels;
    public PathRecyclerViewAdapter(Context context, ArrayList<PathModel> pathModels) {
        this.context = context;
        this.pathModels = pathModels;
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
    }

    @Override
    public int getItemCount() {
        return pathModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private Dialog dialogPath;
        TextView pathNameTxt;
        Button btnView;
        String pathName;

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

        public void displayMapDialog(){
            dialogPath = new Dialog(itemView.getContext());
            dialogPath.setContentView(R.layout.path_profile_dialog);
            dialogPath.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FragmentContainerView  fcv = dialogPath.findViewById(R.id.fragmentContainerView);
            PathDisplay pathDisplay= fcv.getFragment();
            pathDisplay.setPathName(pathName);
            dialogPath.show();
            TextView txt = dialogPath.findViewById(R.id.txtPathnameProfile);
            txt.setText(pathName);

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setReorderingAllowed(true);
//            transaction.replace(R.id.fragmentContainerView, new PathDisplay());
//            transaction.commit();


//            pathDisplay.recoverPath();

            this.getAdapterPosition();
        }
        public void setPathName(String pathName){
            this.pathName = pathName;
        }
    }

}
