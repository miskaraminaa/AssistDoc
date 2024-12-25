package ma.ensa.www.assistdoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.model.Appointment_notif;


public class Appointments_Adapter extends RecyclerView.Adapter<Appointments_Adapter.ViewHolder> {
    private ArrayList<Appointment_notif> appointments;
    private Context context;

    public Appointments_Adapter(ArrayList<Appointment_notif> appointments, Context context) {
        this.appointments = appointments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment_notif appointment = appointments.get(position);
        holder.appointmentDate.setText(appointment.getDate());
        // Bind other views here
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void filterList(ArrayList<Appointment_notif> filteredList) {
        appointments = filteredList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentDate;

        ViewHolder(View itemView) {
            super(itemView);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
        }
    }
}
