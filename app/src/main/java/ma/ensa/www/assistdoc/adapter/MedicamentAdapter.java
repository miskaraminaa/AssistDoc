package ma.ensa.www.assistdoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.entities.Medicament;

public class MedicamentAdapter extends ArrayAdapter<Medicament> {

    private Context context;
    private List<Medicament> medications;

    public MedicamentAdapter(Context context, List<Medicament> medications) {
        super(context, R.layout.item_medication, medications);
        this.context = context;
        this.medications = medications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medicament medicament = medications.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_medication, parent, false);
        }

        TextView textViewNom = convertView.findViewById(R.id.textViewNom);
        TextView textViewDosage = convertView.findViewById(R.id.textViewDosage);
        TextView textViewFrequence = convertView.findViewById(R.id.textViewFrequence);
        TextView textViewHeurePris = convertView.findViewById(R.id.textViewHeurePris);

        textViewNom.setText(medicament.getNom());
        textViewDosage.setText(medicament.getDosage());
        textViewFrequence.setText(medicament.getFrequence());
        textViewHeurePris.setText(medicament.getHeurePris());

        return convertView;
    }
}
