/*
 * CustomerRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.customer;

import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

@Repository
public interface CustomerRepository extends AbstractRepository {

    @Query("SELECT DISTINCT b FROM Booking b WHERE b.customer.id = :customerId ORDER BY b.purchaseMoment DESC")
    List<Booking> findLastFiveDestinations(int customerId);

    @Query("SELECT SUM(b.price.amount) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -1, CURRENT_DATE)")
    Double findTotalMoneySpentLastYear(int customerId);

    @Query("SELECT b.travelClass, COUNT(b) FROM Booking b WHERE b.customer.id = :customerId GROUP BY b.travelClass")
    List<Object[]> countBookingsByTravelClass(int customerId);

    @Query("SELECT COUNT(b.price.amount), AVG(b.price.amount), MIN(b.price.amount), MAX(b.price.amount), STDDEV(b.price.amount) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= FUNCTION('DATEADD', 'YEAR', -5, CURRENT_DATE)")
    Object[] computeBookingPriceStatistics(int customerId);

    @Query("SELECT COUNT(p.id), AVG(p.id), MIN(p.id), MAX(p.id), STDDEV(p.id) FROM Passenger p WHERE p.booking.customer.id = :customerId")
    Object[] computePassengerStatistics(int customerId);
}
