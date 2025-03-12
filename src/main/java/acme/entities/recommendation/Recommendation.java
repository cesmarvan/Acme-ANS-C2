
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
	@ValidString(min = 2, max = 50)
	@Automapped
	private String					city;

	@Mandatory
	@ValidString(min = 2, max = 50)
	@Automapped
	private String					country;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private RecommendationCategory	category;

	@Mandatory
	@ValidString(min = 5, max = 100)
	private String					name;

	@Mandatory
	@ValidString(min = 10, max = 255)
	@Automapped
	private String					description;

	@Column(nullable = true)
	@ValidUrl
	@Automapped
	private String					moreInfoUrl;
}
