package test;

import java.util.Comparator;

public class ReserveisionComparatorByPrice implements Comparator<Reservation>{

	@Override
	public int compare(Reservation o1, Reservation o2) {
		if (o1.getPrice() < o2.getPrice()) 
			return -1;
		else if (o1.getPrice() > o2.getPrice())
			return 1;
		else 
			return 0;
	}
	

}
