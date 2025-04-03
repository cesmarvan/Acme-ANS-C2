
package acme.features.assistanceAgent.claim;

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
import acme.entities.claim.IndicatorClaim;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentCreateClaimService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository claimRepository;


	@Override
	public void authorise() {
		boolean isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAssistanceAgent);
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
		claim.setIndicator(IndicatorClaim.PENDING);
		claim.setRegistrationMoment(moment);
		claim.setDraftMode(true);
		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		Leg leg;

		leg = this.claimRepository.findLegByClaim(claim.getId());
		claim.setLeg(leg);

		super.bindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.claimRepository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices type = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices indicator = SelectChoices.from(IndicatorClaim.class, claim.getIndicator());
		SelectChoices legChoices = SelectChoices.from(this.claimRepository.findAllLegs(), "id", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg", "draftMode");
		dataset.put("type", type);
		dataset.put("indicator", indicator);
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
