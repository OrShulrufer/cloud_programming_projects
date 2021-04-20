package test;

import java.time.LocalDate;
import java.util.List;

public class Manager implements HotelManager {
	Hotel hotel;
	
	public Manager() {
		this.hotel = new Hotel();
		
	}
	

	@Override
	public void setNumberOfRooms(int numRooms) {
		this.hotel.setNumOfRums(numRooms);
		
	}

	@Override
	public boolean makeReservation(Reservation reservation) {	
		return this.hotel.addReservation(reservation);
	}

	@Override
	public void cancelReservation(int reservationId) {
		this.hotel.cancelReservation(reservationId);
		
	}

	@Override
	public Reservation getReservation(int reservationId) {
		return this.hotel.getReservation(reservationId);
	}

	@Override
	public int getNumberAvailableRooms(LocalDate dateToCheck) {	
		return this.hotel.getNumberAvailableRooms(dateToCheck);
	}

	@Override
	public int getPriceOfReservations(LocalDate from, LocalDate to) {
		return 	hotel.getPriceOfReservations(from, to);

	}

	@Override
	public List<Reservation> getAllReservationsSortedByPrice(LocalDate from, LocalDate to) {
		return this.hotel.getAllReservationsSortedByPrice(from, to);
	}

	@Override
	public List<Reservation> getAllReservationsSortedByDate(LocalDate from, LocalDate to) {
		return this.hotel.getAllReservationsSortedByDate(from, to);
	}

}
