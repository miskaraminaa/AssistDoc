package ma.ensa.www.assistdoc.model;


public class Appointment_notif {
    private String appointment_text;
    private String date;

    public Appointment_notif() {
        // Default constructor required for calls to DataSnapshot.getValue(Appointment_notif.class)
    }

    public Appointment_notif(String appointment_text, String date) {
        this.appointment_text = appointment_text;
        this.date = date;
    }

    public String getAppointment_text() {
        return appointment_text;
    }

    public void setAppointment_text(String appointment_text) {
        this.appointment_text = appointment_text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
