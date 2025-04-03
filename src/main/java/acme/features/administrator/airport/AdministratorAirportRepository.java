
package acme.features.administrator.airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	List<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select a from Airport a where a.iataCode = :iataCode")
	Airport findAirportByIATA(String iataCode);

}
