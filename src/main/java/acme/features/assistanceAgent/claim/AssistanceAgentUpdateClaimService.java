
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.claim.IndicatorClaim;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentUpdateClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Claim claim;
		AssistanceAgent agent;

		id = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(id);
		agent = claim == null ? null : claim.getAssistanceAgent();
		status = super.getRequest().getPrincipal().hasRealm(agent) && claim != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;
		Leg leg;

		id = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(id);

		leg = this.claimRepository.findLegByClaim(id);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "email", "description", "type", "indicator", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		if (!claim.isDraftMode())
			super.state(false, "draftMode", "acme.validation.confirmation.message.update");
	}

	@Override
	public void perform(final Claim claim) {
		this.claimRepository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		Collection<Leg> legs = this.claimRepository.findAvailableLegs(MomentHelper.getCurrentMoment());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		boolean pending = claim.indicator().equals(IndicatorClaim.PENDING);

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg");
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("leg", claim.getLeg());
		dataset.put("indictor", claim.indicator());
		dataset.put("pending", pending);

		super.getResponse().addData(dataset);
	}
}
