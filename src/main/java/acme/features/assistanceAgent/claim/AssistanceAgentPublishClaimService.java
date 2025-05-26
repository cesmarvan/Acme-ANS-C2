
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
public class AssistanceAgentPublishClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		int claimId = super.getRequest().getData("id", int.class);
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean status = this.claimRepository.isDraftClaimOwnedByAgent(claimId, agentId);

		if (super.getRequest().hasData("leg", int.class)) {
			int legId = super.getRequest().getData("leg", int.class);

			if (legId != 0) {
				Leg leg = this.claimRepository.findLegById(legId);
				Collection<Leg> availableLegs = this.claimRepository.findAvailableLegs(MomentHelper.getCurrentMoment());
				status = status && availableLegs.contains(leg);
			}
		}

		if (super.getRequest().hasData("claimType", String.class)) {
			String claimType = super.getRequest().getData("claimType", String.class);

			if (!"0".equals(claimType))
				try {
					IndicatorClaim.valueOf(claimType);
				} catch (IllegalArgumentException | NullPointerException e) {
					status = false;
				}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);
		Leg leg = this.claimRepository.findLegByClaim(claimId);

		claim = this.claimRepository.findClaimById(claimId);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "email", "description", "type", "indicator", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.claimRepository.findLegById(legId);

		if (leg == null)
			super.state(false, "leg", "acme.validation.confirmation.message.claim.leg");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
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
		dataset.put("indicator", claim.indicator());
		dataset.put("pending", pending);

		super.getResponse().addData(dataset);
	}

}
