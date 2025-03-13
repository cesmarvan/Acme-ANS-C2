
package acme.entities.customer;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private List<String>		lastFiveDestinations;
	private Money				moneySpentInBookingsLastYear;
	private Integer				numberOfBookingsEconomy;
	private Integer				numberOfBookingsBusiness;

	private Long				bookingsCountLastFiveYears;
	private Double				bookingsAvgCostLastFiveYears;
	private Double				bookingsMinCostLastFiveYears;
	private Double				bookingsMaxCostLastFiveYears;
	private Double				bookingsStdDevCostLastFiveYears;

	private Long				passengersCount;
	private Double				passengersAvg;
	private Integer				passengersMin;
	private Integer				passengersMax;
	private Double				passengersStdDev;

}
