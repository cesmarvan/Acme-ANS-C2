
package acme.features.assistanceAgent.trackingLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.IndicatorClaim;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface ------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(trackingLogId);

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && trackingLog != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;
		Date today = MomentHelper.getCurrentMoment();

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);
		trackingLog.setUpdateMoment(today);

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
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicatorChoices = SelectChoices.from(IndicatorClaim.class, trackingLog.getIndicator());
		int id = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimByTrackingLogId(id);

		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode", "claim");
		dataset.put("indicator", indicatorChoices);
		dataset.put("readOnlyIndicator", "false");
		dataset.put("claim", claim);
		super.getResponse().addData(dataset);
	}

}
