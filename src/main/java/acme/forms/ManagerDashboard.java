
package acme.forms;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						rankingPlaceBasedOnExperienceYears;
	Integer						numberOYearsToRetire;
	Double						ratioOfOnTimeDelayedLegs;
	String						nameMostPopularAirport;
	String						nameLessPopularAirport;
	Integer						numbersOfLegsOnTime;
	Integer						numbersOfLegsDelayed;
	Integer						numbersOfLegsCanceled;
	Integer						numbersOfLegsLanded;
	Money						averageCost;
	Money						minimumCost;
	Money						maximumCost;
}
