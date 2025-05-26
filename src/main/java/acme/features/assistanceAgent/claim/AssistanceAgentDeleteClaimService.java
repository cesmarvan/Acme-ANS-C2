
package acme.features.assistanceAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentDeleteClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;

	// @Autowired
	// private AssistanceAgentTrackingLogRepository	tLRepository;


	@Override
	public void authorise() {
		boolean status;
		int claimId = super.getRequest().getData("id", int.class);
		Claim claim = this.claimRepository.findClaimById(claimId);
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null && super.getRequest().getPrincipal().getActiveRealm().getId() == claim.getAssistanceAgent().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int claimId;
		Leg leg;

		claimId = super.getRequest().getData("id", int.class);
		leg = this.claimRepository.findLegByClaim(claimId);

		claim = this.claimRepository.findClaimById(claimId);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg");

	}

	@Override
	public void validate(final Claim claim) {
		if (!claim.isDraftMode())
			super.state(false, "draftMode", "acme.validation.draftMode.message");
	}

	@Override
	public void perform(final Claim claim) {
		//		Collection<TrackingLog> trackingLogs;
		//		trackingLogs = this.trackingLogRepository.findTrackingLogsByClaimId(claim.getId());

		//		this.repository.deleteAll(trackingLogs);
		this.claimRepository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices legChoices = SelectChoices.from(this.claimRepository.findAvailableLegs(MomentHelper.getCurrentMoment()), "flighNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "leg");
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("getIndicator", claim.indicator());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}
}
