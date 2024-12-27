package ma.ensa.www.assistdoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.model.Medicament;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> implements Filterable {

    private List<Medicament> medications;
    private List<Medicament> medicationsFull;
    private List<Medicament> medicationsFiltered;
    private final MedicationViewHolder.OnDeleteListener onDelete;

    public MedicationAdapter(List<Medicament> medications, MedicationViewHolder.OnDeleteListener onDelete) {
        this.medications = medications;
        this.medicationsFull = new ArrayList<>(medications);
        this.medicationsFiltered = new ArrayList<>(medications);
        this.onDelete = onDelete;
    }

    public void updateList(List<Medicament> newList) {
        medications.clear();
        medications.addAll(newList);
        medicationsFull.clear();
        medicationsFull.addAll(newList);
        medicationsFiltered.clear();
        medicationsFiltered.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public MedicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    public List<Medicament> getMedications() {
        return new ArrayList<>(medicationsFiltered); // Retourne une copie de la liste filtrée
    }

    @Override
    public void onBindViewHolder(MedicationViewHolder holder, int position) {
        Medicament medicament = medicationsFiltered.get(position);
        holder.textMedName.setText(medicament.getNom());
        holder.textMedTime.setText(medicament.getHeurePris());

        holder.itemView.setOnLongClickListener(v -> {
           // onDelete.onDelete(medicationsFiltered.indexOf(medicament));
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return medicationsFiltered.size();
    }

    public void removeAt(int position) {
        Medicament medicament = medicationsFiltered.get(position);
        medicationsFiltered.remove(position);
        medications.remove(medicament);
        medicationsFull.remove(medicament);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = (constraint != null) ? constraint.toString() : "";

                List<Medicament> filteredList = new ArrayList<>();
                if (searchString.isEmpty()) {
                    filteredList = medicationsFull;
                } else {
                    for (Medicament medicament : medicationsFull) {
                        if (medicament.getNom().toLowerCase().contains(searchString.toLowerCase()) ||
                                medicament.getHeurePris().toLowerCase().contains(searchString.toLowerCase())) {
                            filteredList.add(medicament);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                medicationsFiltered = (List<Medicament>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView textMedName;
        TextView textMedTime;

        public MedicationViewHolder(View itemView) {
            super(itemView);
           /*   textMedName = itemView.findViewById(R.id.textMedName);
            textMedTime = itemView.findViewById(R.id.textMedTime);
        }*/
        }

        public interface OnDeleteListener {
            void onDelete(int position);
        }

    }}
