package ma.ensa.assistdocapp.repositories;

import ma.ensa.assistdocapp.entities.Appointment;

import java.util.List;

public interface CustomRepository{
    public List<Appointment> findAllByEmail(String email);
    //public List<Appointment> findAllForDoc(String docId);
}