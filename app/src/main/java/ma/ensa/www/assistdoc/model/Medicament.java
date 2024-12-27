package ma.ensa.www.assistdoc.model;


public class Medicament {
    private String userId;
    private String id;
    private String dosage;
    private String frequence;
    private String heurePris;
    private String nom;

    public Medicament(String nom, String dosage, String frequence, String heurePris) {
        this.nom = nom;
        this.dosage = dosage;
        this.frequence = frequence;
        this.heurePris = heurePris;
    }

    public Medicament() {

    }

    // Getters and setters for each field
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getHeurePris() {
        return heurePris;
    }

    public void setHeurePris(String heurePris) {
        this.heurePris = heurePris;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
