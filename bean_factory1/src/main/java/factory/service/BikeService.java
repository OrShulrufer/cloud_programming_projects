package factory.service;

import org.springframework.stereotype.Service;

import factory.model.Vehicle;
import factory.registry.AdapterService;

@Service("Bike")
public class BikeService implements AdapterService<Vehicle> {

	@Override
	public void process(Vehicle request) {
		System.out.println("inside bike service - " + request.toString());
	}
}
