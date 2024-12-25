package ma.ensa.www.assistdoc.entities;

public class Messages {

    private String sender;    // Identifiant de l'expéditeur du message
    private String receiver;  // Identifiant du destinataire du message
    private String message;   // Contenu du message
    private String time;      // Heure d'envoi du message

    // Constructeur par défaut
    public Messages() {
        this.sender = "";
        this.receiver = "";
        this.message = "";
        this.time = "";
    }

    // Constructeur avec paramètres
    public Messages(String sender, String receiver, String message, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    // Getters et setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Méthode pour générer un identifiant unique pour chaque message
    public String getId() {
        return sender + "-" + receiver + "-" + message + "-" + time;
    }
}
