
package acme.features.assistanceAgent.trackingLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.IndicatorClaim;
import acme.entities.trackingLog.TrackingLog;
import acme.features.assistanceAgent.claim.AssistanceAgentClaimRepository;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAgent);
	}

	@Override
	public void load() {
		TrackingLog trackingLog = new TrackingLog();
		Date today = MomentHelper.getCurrentMoment();
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(claimId);

		trackingLog.setUpdateMoment(today);
		trackingLog.setIndicator(IndicatorClaim.PENDING);
		trackingLog.setResolutionPercentage(0.);
		trackingLog.setDraftMode(true);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolution");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setUpdateMoment(MomentHelper.getCurrentMoment());
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicatorChoices = SelectChoices.from(IndicatorClaim.class, trackingLog.getIndicator());
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(claimId);

		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode", "claim");
		dataset.put("indicator", indicatorChoices);
		trackingLog.setClaim(claim);
		dataset.put("readOnlyIndicator", "true");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
