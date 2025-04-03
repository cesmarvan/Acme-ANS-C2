
package acme.features.assistanceAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentPublishClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;
		AssistanceAgent agent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(claimId);
		agent = claim == null ? null : claim.getAssistanceAgent();

		status = claim != null && super.getRequest().getPrincipal().hasRealm(agent);

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
		;
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.claimRepository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices type = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices leg = SelectChoices.from(this.claimRepository.findAllLegs(), "id", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg");
		dataset.put("type", type);
		dataset.put("indicator", claim.getIndicator());
		dataset.put("legs", leg);
		dataset.put("leg", leg.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
