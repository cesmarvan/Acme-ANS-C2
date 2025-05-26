
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentCreateClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		boolean isAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAgent);

		boolean status = true;
		if (super.getRequest().hasData("leg", int.class)) {
			int legId = super.getRequest().getData("leg", int.class);

			Leg leg = this.claimRepository.findLegById(legId);
			if (legId != 0) {
				Collection<Leg> availableLegs = this.claimRepository.findAvailableLegs(MomentHelper.getCurrentMoment());
				status = availableLegs.contains(leg);
			}
		}

		super.getResponse().setAuthorised(isAgent && status);

	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistanceAgent;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int agentId = principal.getId();
		assistanceAgent = this.claimRepository.findAssistanceAgentById(agentId);

		claim = new Claim();
		claim.setRegistrationMoment(moment);
		claim.setDraftMode(true);
		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.claimRepository.findLegById(legId);
		claim.setLeg(leg);

		super.bindObject(claim, "registrationMoment", "email", "description", "type", "indicator");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		if (this.claimRepository.findLegById(super.getRequest().getData("leg", int.class)) == null)
			super.state(false, "leg", "acme.validation.confirmation.message.claim.leg");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
		claim.setDraftMode(true);
		this.claimRepository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs = this.claimRepository.findAvailableLegs(MomentHelper.getCurrentMoment());
		SelectChoices typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "draftMode");
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("indicator", claim.indicator());
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
