package ma.ensa.assistdocapp.repositories;

import ma.ensa.assistdocapp.entities.Appointment;

import java.util.List;

public interface CustomTwo{
    public List<Appointment> findByDocId(String docId);
}