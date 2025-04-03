
package acme.features.administrator.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AdministratorAirportListService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Administrator administrator;
		administrator = (Administrator) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(administrator);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Airport> airports;

		airports = this.repository.findAllAirports();

		super.getBuffer().addData(airports);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope");
		super.addPayload(dataset, airport, "city");

		super.getResponse().addData(dataset);
	}
}
