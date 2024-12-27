package ma.ensa.assistdocapp.repositories;

import ma.ensa.assistdocapp.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,String>{
	
	
}
