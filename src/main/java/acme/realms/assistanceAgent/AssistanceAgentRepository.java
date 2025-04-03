
package acme.realms.assistanceAgent;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("SELECT aa FROM AssistanceAgent aa")
	List<AssistanceAgent> findAllAgents();
}
