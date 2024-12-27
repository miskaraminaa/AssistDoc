package ma.ensa.assistdocapp.repositories;
import ma.ensa.assistdocapp.entities.Doctor;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepository extends CrudRepository<Doctor,String>{
	//List<Doctor> findAll();
	//Optional<Doctor> findById(String Id);

	
}
