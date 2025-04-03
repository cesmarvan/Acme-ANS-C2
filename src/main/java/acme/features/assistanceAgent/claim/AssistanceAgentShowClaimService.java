
package acme.features.assistanceAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.claim.IndicatorClaim;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentShowClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		status = super.getRequest().getPrincipal().hasRealm(agent) || claim != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(id);
		Leg leg = this.claimRepository.findLegByClaim(claim.getId());
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices indicatorChoices = SelectChoices.from(IndicatorClaim.class, claim.getIndicator());
		SelectChoices legChoices = SelectChoices.from(this.claimRepository.findAllLegs(), "id", claim.getLeg());
		SelectChoices draftModeChoices = new SelectChoices();
		draftModeChoices.add("true", "True", claim.isDraftMode());
		draftModeChoices.add("false", "False", !claim.isDraftMode());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "draftMode");
		dataset.put("type", claimTypeChoices);
		dataset.put("indicator", indicatorChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
