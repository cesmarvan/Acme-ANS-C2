
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportOperationalScope;

@GuiService
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {

	// Internal state ---------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interface --------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;

		airport = new Airport();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "web", "emailAddress", "contactPhoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		{
			boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.airport.no-confirmation.message");
		}
		{
			boolean uniqueAirport;
			Airport otherAirport;

			otherAirport = this.repository.findAirportByIATA(airport.getIataCode());
			uniqueAirport = otherAirport == null || otherAirport.equals(airport);

			super.state(uniqueAirport, "iataCode", "acme.validation.airport.duplicated-iata-code.message");
		}
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		boolean confirmation = true;

		SelectChoices operationalScopeChoices;
		operationalScopeChoices = SelectChoices.from(AirportOperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "web", "emailAddress", "contactPhoneNumber");
		dataset.put("confirmation", confirmation);

		dataset.put("operationalScopes", operationalScopeChoices);

		super.getResponse().addData(dataset);
	}
}
