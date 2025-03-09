
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog {

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date	registrationMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String	typeOfIncident;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String	description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer	severity;

}
