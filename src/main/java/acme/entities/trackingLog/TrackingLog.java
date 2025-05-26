
package acme.entities.trackingLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidShortText;
import acme.constraints.ValidTrackingLog;
import acme.entities.claim.Claim;
import acme.entities.claim.IndicatorClaim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
public class TrackingLog extends AbstractEntity {

	// Version
	private static final long	serialVersionUID	= 1L;

	// Attributes
	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateMoment;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				step;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	private IndicatorClaim		indicator;

	@Optional
	@ValidString
	@Automapped
	private String				resolution;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
