
package acme.constraints;

import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claim.ClaimRepository;
import acme.entities.claim.IndicatorClaim;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimRepository			claimRepository;

	@Autowired
	private TrackingLogRepository	trackingLogRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");

		else {

			// Resolution is mandatory if resolutionPercentage=100%
			{
				boolean resolutionValid;

				if (trackingLog.getResolutionPercentage() == 100.00)
					resolutionValid = trackingLog.getResolution() != null && !trackingLog.getResolution().trim().isEmpty();
				else
					resolutionValid = trackingLog.getResolution() == null || trackingLog.getResolution().trim().isEmpty();

				super.state(context, resolutionValid, "resolution", "acme.validation.trackinLog.resolutionMandatory.message");

			}

			// Validate that there are at most 2 trackingLogs with 100% resolution
			{
				boolean trackingLogsCompleted = true;

				if (trackingLog.getResolutionPercentage() != null && trackingLog.isDraftMode()) {
					Collection<TrackingLog> trackingLogs = this.claimRepository.findTrackingLogsOfClaimByResolutionPercentage(trackingLog.getClaim().getId(), trackingLog.getResolutionPercentage());
					if (trackingLog.getResolutionPercentage() == 100)
						trackingLogsCompleted = trackingLogs.size() <= 1;
					else
						trackingLogsCompleted = trackingLogs.size() <= 0;
				}

				super.state(context, trackingLogsCompleted, "indicator", "acme.validation.trackingLog.trackingLogsCompleted.message");
			}

			//Validate that draftMode attribute is logical with its claim
			{
				boolean correctDraftMode;
				correctDraftMode = true;

				if (trackingLog.getClaim().isDraftMode())
					correctDraftMode = trackingLog.isDraftMode();

				super.state(context, correctDraftMode, "draftMode", "acme.validation.trackingLog.draftModeLogical.message");

			}

			// Validate that resolutionPercentage attribute is incremental
			{
				if (trackingLog.getClaim() != null) {

					boolean correctPercentage;
					Double currentPercentage = trackingLog.getResolutionPercentage();
					TrackingLog existing = this.trackingLogRepository.findTrackingLogById(trackingLog.getId());
					List<TrackingLog> logs = this.trackingLogRepository.findTrackingLogsByResolutionPercentage(trackingLog.getClaim().getId());

					if (existing == null) {
						TrackingLog last = logs.isEmpty() ? null : logs.get(0);
						correctPercentage = last == null || currentPercentage >= last.getResolutionPercentage();

					} else {
						int index = logs.indexOf(trackingLog);

						TrackingLog prev = index + 1 < logs.size() ? logs.get(index + 1) : null;
						TrackingLog next = index - 1 >= 0 ? logs.get(index - 1) : null;

						boolean validPrev = prev == null || currentPercentage >= prev.getResolutionPercentage();
						boolean validNext = next == null || currentPercentage <= next.getResolutionPercentage();

						correctPercentage = validPrev && validNext;
					}

					super.state(context, correctPercentage, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
				}
			}

			{
				boolean indicatorPending = true;

				if (trackingLog.getResolutionPercentage() == 100)
					indicatorPending = !trackingLog.getIndicator().equals(IndicatorClaim.PENDING);
				else
					indicatorPending = trackingLog.getIndicator().equals(IndicatorClaim.PENDING);

				super.state(context, indicatorPending, "indicator", "acme.validation.trackingLog.acceptedPending.message");
			}

		}
		result = !super.hasErrors(context);

		return result;
	}
}
