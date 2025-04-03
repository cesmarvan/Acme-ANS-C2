
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface


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
		Airline airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		{
			boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.airline.no-confirmation.message");
		}
		{
			boolean uniqueAirline;
			Airline otherAirline;

			otherAirline = this.repository.findAirlineByIATA(airline.getIataCode());
			uniqueAirline = otherAirline == null || otherAirline.equals(airline);

			super.state(uniqueAirline, "iataCode", "acme.validation.airline.duplicated-iata-code.message");
		}
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		boolean confirmation = true;

		SelectChoices choices;
		choices = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", confirmation);

		dataset.put("types", choices);

		super.getResponse().addData(dataset);

	}

}
