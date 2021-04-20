package factory.service;

import org.springframework.stereotype.Service;

import factory.model.Vehicle;
import factory.registry.AdapterService;

@Service("Car")
public class CarService implements AdapterService<Vehicle> {

	@Override
	public void process(Vehicle request) {
		// TODO Auto-generated method stub
		System.out.println("inside car service - " + request.toString());
	}
}
