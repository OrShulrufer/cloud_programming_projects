package test;

import java.util.Comparator;

public class ReserveisionComparatorByDate implements Comparator<Reservation>{

	@Override
	public int compare(Reservation o1, Reservation o2) {
		return o1.getFromDate().compareTo(o2.getFromDate());
	}

}
