
package acme.entities.claim;

import java.beans.Transient;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.constraints.ValidLongText;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
@Table(indexes = {
	@Index(columnList = "assistance_agent_id, draftMode"), @Index(columnList = "leg_id"), @Index(columnList = "registrationMoment")
})
public class Claim extends AbstractEntity {

	// Version
	private static final long	serialVersionUID	= 1L;

	// Attributes
	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;


	@Transient
	public IndicatorClaim indicator() {
		Collection<TrackingLog> trackingLogs;
		IndicatorClaim indicator;

		ClaimRepository repository = SpringHelper.getBean(ClaimRepository.class);

		trackingLogs = repository.findAllByClaimId(this.getId());
		indicator = trackingLogs.size() == 0 ? IndicatorClaim.PENDING : trackingLogs.stream().findFirst().get().getIndicator();

		return indicator;
	}


	@Mandatory
	@Automapped
	private boolean			draftMode;

	// Relationships
	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private Leg				leg;

}
