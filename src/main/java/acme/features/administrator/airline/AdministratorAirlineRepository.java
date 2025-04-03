
package acme.features.administrator.airline;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("select a from Airline a")
	List<Airline> findAllAirlines();

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

	@Query("select a from Airline a where a.iataCode = :iataCode")
	Airline findAirlineByIATA(String iataCode);

}
