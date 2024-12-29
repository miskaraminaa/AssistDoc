package ma.ensa.www.assistdoc.model;

public class Appointment {


    private String appointmentId;
    private String PatientName;
    private String date;
    private String time;
    private String doctorNotes;


    public Appointment() {
    }

    public Appointment(String appointmentId, String patientName, String date, String time, String doctorNotes) {
        this.appointmentId = appointmentId;
        PatientName = patientName;
        this.date = date;
        this.time = time;
        this.doctorNotes = doctorNotes;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}
