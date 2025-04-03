
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claim.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentCompletedClaimsListService	completedClaimsListService;

	@Autowired
	private AssistanceAgentUndergoingClaimsListService	undergoingClaimsListService;

	@Autowired
	private AssistanceAgentShowClaimService				showService;

	@Autowired
	private AssistanceAgentCreateClaimService			createService;

	@Autowired
	private AssistanceAgentUpdateClaimService			updateService;

	@Autowired
	private AssistanceAgentPublishClaimService			publishService;

	@Autowired
	private AssistanceAgentDeleteClaimService			deleteService;


	@PostConstruct
	protected void initialilse() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-complete", "list", this.completedClaimsListService);
		super.addCustomCommand("list-pending", "list", this.undergoingClaimsListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
