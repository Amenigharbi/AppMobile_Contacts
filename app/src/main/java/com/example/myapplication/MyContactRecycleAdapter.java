package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyContactRecycleAdapter extends RecyclerView.Adapter<MyContactRecycleAdapter.MyViewHolder> implements Filterable {
    private final Context con;
    private final ArrayList<Contact> data; // Liste des contacts originaux
    private final ArrayList<Contact> dataFull; // Liste pour filtrer

    public MyContactRecycleAdapter(Context con, ArrayList<Contact> data) {
        this.con = con;
        this.data = data;
        this.dataFull = new ArrayList<>(data); // Copie de la liste originale
    }

    @NonNull
    @Override
    public MyContactRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(con);
        View v = inf.inflate(R.layout.view_contact, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContactRecycleAdapter.MyViewHolder holder, int position) {
        Contact c = data.get(position);
        holder.tvnom.setText(c.getNom());
        holder.pseudo.setText(c.getPrenom());
        holder.e.setText(c.getNumero());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refreshData() {
        ContactManager manager = new ContactManager(con);
        manager.ouvrir();
        ArrayList<Contact> updatedContacts = manager.getAllContact();
        manager.fermer();

        data.clear();
        data.addAll(updatedContacts);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contact> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    // Retourner la liste originale si aucune recherche n'est effectuée
                    filteredList.addAll(dataFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Contact contact : dataFull) {
                        // Filtrer par nom ou prénom
                        if (contact.getNom().toLowerCase().contains(filterPattern) ||
                                contact.getPrenom().toLowerCase().contains(filterPattern)) {
                            filteredList.add(contact);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                if (results.values != null) {
                    data.addAll((List) results.values);
                }
                if (data.isEmpty()) {
                    Toast.makeText(con, "No contacts found", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged(); // Notifier le changement d'affichage
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvnom, pseudo, e;
        ImageView imgDel, imgCall, imgEdit;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvnom = v.findViewById(R.id.tvnom_contact);
            pseudo = v.findViewById(R.id.tvpseudo_contact);
            e = v.findViewById(R.id.tvnum_contact);
            imgDel = v.findViewById(R.id.imageView_del);
            imgCall = v.findViewById(R.id.imageView_call);
            imgEdit = v.findViewById(R.id.imageView_edit);

            imgDel.setOnClickListener(view -> {
                int index = getAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    Contact contactToDelete = data.get(index);
                    new AlertDialog.Builder(con)
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to delete this contact?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Procéder à la suppression
                                ContactManager manager = new ContactManager(con);
                                manager.ouvrir();

                                // Supprimer le contact en utilisant l'ID
                                if (manager.deleteContact(contactToDelete.getId())) {
                                    // Rafraîchir les données après suppression
                                    refreshData();
                                    Toast.makeText(con, "Contact deleted!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(con, "Failed to delete contact!", Toast.LENGTH_SHORT).show();
                                }

                                manager.fermer();
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

            imgCall.setOnClickListener(view -> {
                Intent i = new Intent(Intent.ACTION_DIAL);
                int index = getAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    i.setData(Uri.parse("tel:" + data.get(index).getNumero()));
                    con.startActivity(i);
                }
            });

            imgEdit.setOnClickListener(view -> {
                int index = getAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    showEditDialog(data.get(index), index);
                }
            });
        }

        private void showEditDialog(Contact contact, int index) {
            AlertDialog.Builder alert = new AlertDialog.Builder(con);
            alert.setTitle("Édition");
            alert.setMessage("Saisir les infos:");

            LayoutInflater inf = LayoutInflater.from(con);
            View dialogView = inf.inflate(R.layout.view_dialog, null);
            alert.setView(dialogView);

            // Obtenir les champs EditText à partir de la vue du dialogue
            EditText edNom = dialogView.findViewById(R.id.edNom);
            EditText edPrenom = dialogView.findViewById(R.id.edPrenom);
            EditText edNumero = dialogView.findViewById(R.id.edNumero);

            // Remplir les champs avec les valeurs existantes
            edNom.setText(contact.getNom());
            edPrenom.setText(contact.getPrenom());
            edNumero.setText(contact.getNumero());

            alert.setPositiveButton("Valider", (dialog, which) -> {
                String nom = edNom.getText().toString().trim();
                String prenom = edPrenom.getText().toString().trim();
                String numero = edNumero.getText().toString().trim();

                // Vérifier si un champ est vide
                if (nom.isEmpty() || prenom.isEmpty() || numero.isEmpty()) {
                    Toast.makeText(con, "Veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContactManager manager = new ContactManager(con);
                manager.ouvrir();
                manager.updateContact(contact.getId(), nom, prenom, numero); // Assurez-vous que cette méthode est définie
                manager.fermer();

                // Mettre à jour le contact dans le tableau de données
                contact.setNom(nom);
                contact.setPrenom(prenom);
                contact.setPhoneNumber(numero);

                // Notifier le changement pour mettre à jour l'affichage
                notifyItemChanged(index);
                Toast.makeText(con, "Contact mis à jour!", Toast.LENGTH_SHORT).show();
            });

            alert.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss()); // Fermer le dialogue sans autre action

            alert.show();
        }
    }
}
