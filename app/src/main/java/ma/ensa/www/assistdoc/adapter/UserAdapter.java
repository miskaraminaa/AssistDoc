package ma.ensa.www.assistdoc.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ensa.www.assistdoc.patient.Medications_Activity;
import ma.ensa.www.assistdoc.doctor.PatientDetailsActivity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.Chat_Windo;
import ma.ensa.www.assistdoc.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    ArrayList<Users> usersArrayList;
    private Context mContext;

    public UserAdapter(Context mContext, ArrayList<Users> usersArrayList) {
        this.mContext=mContext;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {

        Users users = usersArrayList.get(position);
        holder.username.setText(users.getUsername());
        holder.userstatus.setText(users.getStatus());
        Picasso.get().load(users.getProfilepic()).into(holder.userimg);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, Chat_Windo.class);
            intent.putExtra("nameeee",users.getUsername());
            intent.putExtra("reciverImg",users.getProfilepic());
            intent.putExtra("uid",users.getUserId());
            mContext.startActivity(intent);

        });

        holder.itemView.setOnLongClickListener(v -> {

            // Create the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Patient Informations") // Set the title
                    .setMessage("All to know about your patient") // Set the message
                    .setPositiveButton("See appointment details", (dialog, which) -> {
                        Intent intent = new Intent(mContext, PatientDetailsActivity.class);
                        intent.putExtra("nameeee",users.getUsername());
                        intent.putExtra("reciverImg",users.getProfilepic());
                        intent.putExtra("mail",users.getEmail());
                        intent.putExtra("uid",users.getUserId());
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    })
                    .setNeutralButton("See Medicament Details", (dialog, which) -> {
                        Intent intent = new Intent(mContext, Medications_Activity.class);
                        intent.putExtra("uid",users.getUserId());
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    });

            // Show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            return false;
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        CircleImageView userimg;
        TextView username;
        TextView userstatus;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
