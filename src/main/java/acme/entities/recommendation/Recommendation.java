
package acme.entities.recommendation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendation extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Column(nullable = true)
	@Mandatory
	@ValidString(min = 3, max = 10, pattern = "^[A-Z]{3,10}$")
	@Automapped
	private String					airportCode;

	@Mandatory
	@ValidShortText
	@Automapped
	private String					city;

	@Mandatory
	@ValidShortText
	@Automapped
	private String					country;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private RecommendationCategory	category;

	@Mandatory
	@ValidShortText
	private String					name;

	@Mandatory
	@ValidLongText
	@Automapped
	private String					description;

	@Column(nullable = true)
	@ValidUrl
	@Automapped
	private String					moreInfoUrl;
}
