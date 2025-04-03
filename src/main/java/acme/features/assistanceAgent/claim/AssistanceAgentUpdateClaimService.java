
package acme.features.assistanceAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
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

		id = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(id);

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
		SelectChoices type = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices leg = SelectChoices.from(this.claimRepository.findAllLegs(), "id", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "type", "indicator", "leg");
		dataset.put("type", type);
		dataset.put("legs", leg);
		dataset.put("leg", leg.getSelected().getKey());
		dataset.put("indictor", claim.getIndicator());

		super.getResponse().addData(dataset);
	}
}
