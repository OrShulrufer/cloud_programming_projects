package factory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import factory.model.Vehicle;
import factory.registry.ServiceRegistry;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

	@Autowired
	private ServiceRegistry serviceRegistry;

	@PostMapping
	public void processStudentDetails(@RequestBody Vehicle vehicle) {
		serviceRegistry.getService(vehicle.getVehicleType()).process(vehicle);
	}
}