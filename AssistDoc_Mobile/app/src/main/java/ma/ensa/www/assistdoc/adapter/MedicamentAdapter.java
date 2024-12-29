package ma.ensa.www.assistdoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.model.Medicament;
import ma.ensa.www.assistdoc.patient.Medications_Activity;

public class MedicamentAdapter extends ArrayAdapter<Medicament> implements Filterable {

    private Context context;
    private List<Medicament> medications;
    private List<Medicament> medicationsFull;
    private List<Medicament> medicationsFiltered;

    public MedicamentAdapter(Context context, List<Medicament> medications) {
        super(context, R.layout.item_medication, medications);
        this.context = context;
        this.medications = medications;
        this.medicationsFull = new ArrayList<>(medications);
        this.medicationsFiltered = new ArrayList<>(medications);
    }

    public List<Medicament> getMedications() {
        return new ArrayList<>(medicationsFiltered); // Return a copy of the filtered list
    }

    public void updateList(List<Medicament> filteredList) {
        medications.clear();
        medications.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < medications.size()) {
            Medicament medicament = medications.get(position);

            medications.remove(position); // Remove from displayed list
            medicationsFull.remove(medicament); // Remove from original list

            notifyDataSetChanged();
        } else {
            Log.e("MedicamentAdapter", "Invalid position or empty list.");
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = (constraint != null) ? constraint.toString() : "";

                List<Medicament> filteredList = new ArrayList<>();
                if (searchString.isEmpty()) {
                    filteredList = new ArrayList<>(medicationsFull); // Use full list if no search
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
                medicationsFiltered.clear();
                if (results != null && results.values != null) {
                    medicationsFiltered.addAll((List<Medicament>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_medication, parent, false);
        }

        Medicament medicament = medications.get(position);

        TextView textViewNom = convertView.findViewById(R.id.textViewNom);
        TextView textViewDosage = convertView.findViewById(R.id.textViewDosage);
        TextView textViewFrequence = convertView.findViewById(R.id.textViewFrequence);
        TextView textViewHeurePris = convertView.findViewById(R.id.textViewHeurePris);
        ImageButton btnDelete = convertView.findViewById(R.id.deleteButton);

        textViewNom.setText(medicament.getNom());
        textViewDosage.setText(medicament.getDosage());
        textViewFrequence.setText(medicament.getFrequence());
        textViewHeurePris.setText(medicament.getHeurePris());

        btnDelete.setOnClickListener(v -> {
            if (context instanceof Medications_Activity) {
                ((Medications_Activity) context).deleteMedication(medicament, position);
            }
        });

        return convertView;
    }
}
