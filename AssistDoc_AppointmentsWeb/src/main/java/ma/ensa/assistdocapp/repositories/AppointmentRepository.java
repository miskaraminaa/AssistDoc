package ma.ensa.assistdocapp.repositories;

import ma.ensa.assistdocapp.entities.Appointment;
import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepository extends CrudRepository<Appointment,String>, CustomRepository, CustomTwo {

	
	
}
