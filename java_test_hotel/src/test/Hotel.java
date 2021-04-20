package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Hotel {
	private String name;
	private int numOfRums;

	private List<Reservation> reservations = new ArrayList<Reservation>(); 

	
	public Hotel() {
		this.numOfRums = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumOfRums() {
		return numOfRums;
	}

	public void setNumOfRums(int numOfRums) {
		this.numOfRums = numOfRums;
	}


	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public boolean addReservation(Reservation r) {
		for (LocalDate date = r.getFromDate(); date.isBefore(r.getToDate()); date = date.plusDays(1)) {
			int count = 0;
		    for(Reservation res : reservations) {
		    	for(LocalDate acupide_date = res.getFromDate(); acupide_date.isBefore(res.getToDate()); acupide_date = acupide_date.plusDays(1)) {
		    		if(acupide_date.compareTo(date) == 0) {
		    			count++;
		    			if(count == numOfRums)
		    				return false;
		    		}		
		    	}
		    }
		}
		reservations.add(r);
		return true;		
	}
	
	public void cancelReservation(int reservationId) {
		this.reservations = reservations.stream().filter(res-> res.getId() != reservationId).collect(Collectors.toList()); 
	}
	
	public Reservation getReservation(int reservationId) {
		for(Iterator<Reservation> iterator = reservations.iterator(); iterator.hasNext(); ) {
			Reservation reservation = iterator.next();
		    if(reservation.getId() == reservationId)
		        return reservation;
		}
		return null;
	}
	
	public int getNumberAvailableRooms(LocalDate dateToCheck) {
		int acupide = 0;
		for(Reservation res : reservations) {
	    	for(LocalDate acupide_date = res.getFromDate(); acupide_date.isBefore(res.getToDate()); acupide_date = acupide_date.plusDays(1)) {
	    		if(acupide_date.compareTo(dateToCheck) == 0) {
	    			acupide++;
	    		}		
	    	}
	    }
		return this.numOfRums - acupide;
	}
	
	
	
	public int getPriceOfReservations(LocalDate from, LocalDate to) {
	int totalPrice = 0;
	List<Reservation> rList = reservations.stream().filter(res-> 
	(res.getFromDate().isAfter(from) || res.getFromDate().isEqual(from)) && (res.getToDate().isBefore(to) || res.getToDate().isEqual(to))).collect(Collectors.toList());
	System.out.println(rList);

	for(Reservation r: reservations) {
		totalPrice += r.getPrice();
	}
	System.out.println(totalPrice);
	return totalPrice;	
}
	
	
	public List<Reservation> getAllReservationsSortedByPrice(LocalDate from, LocalDate to){
		List<Reservation> rList = reservations.stream().filter(res-> 
		(res.getFromDate().isAfter(from) || res.getFromDate().isEqual(from)) && (res.getToDate().isBefore(to) || res.getToDate().isEqual(to))).collect(Collectors.toList());
		Collections.sort(rList, new  ReserveisionComparatorByPrice());
		return rList;	
	}
	
	public List<Reservation> getAllReservationsSortedByDate(LocalDate from, LocalDate to) {
		
		List<Reservation> rList = reservations.stream().filter(res-> 
		(res.getFromDate().isAfter(from) || res.getFromDate().isEqual(from)) && (res.getToDate().isBefore(to) || res.getToDate().isEqual(to))).collect(Collectors.toList());
		Collections.sort(rList, new  ReserveisionComparatorByDate());
		return rList;
		
		
	}

}
