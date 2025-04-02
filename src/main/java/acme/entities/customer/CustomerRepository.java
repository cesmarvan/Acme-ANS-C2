
package acme.entities.customer;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

public interface CustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.identifier = :identifier")
	Customer findCustomerByIdentifier(String identifier);

}
