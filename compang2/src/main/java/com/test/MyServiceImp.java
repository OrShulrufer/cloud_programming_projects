package com.test;


import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MyServiceImp implements MyService {
	
	public static String VALID_EMAIL_PATTERN = "^[\\w-.]+@([\\w-]+.)+[\\w-]{2,4}$";
	public static String VALID_PHONE_PATTERN = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
	
	private CompangDao compangDao;
	
	@Autowired
	public MyServiceImp(CompangDao compangDao) {
		this.compangDao = compangDao;
	}

	@Override
	public CompangBoundary register(Long id, CompangBoundary register_ditales) {
		
		Optional<CompangEntity> existing = this.compangDao.findById(id);
		if (existing.isPresent()) 
			throw new ExistExeption();

		Map<String, String> mandatoryFields =  register_ditales.getMandatoryFields();
		if (mandatoryFields.get("email") != null) {
			System.out.println(mandatoryFields.get("email"));
			if (!Pattern.matches(VALID_EMAIL_PATTERN,
					mandatoryFields.get("email").trim()))
				throw new IncorrectInputExeption("wrong format for field : email");
		}
		
		if (mandatoryFields.get("phone") != null) {
			if (!Pattern.matches(VALID_EMAIL_PATTERN,
					mandatoryFields.get("phone").trim()))
				throw new IncorrectInputExeption("wrong format for field : phone");
		}

		CompangEntity compangEntity = new CompangEntity(
				id, register_ditales.getCampagnName(), mandatoryFields);

		this.compangDao.save(compangEntity);

		return register_ditales;

	}


	

	

	
	



}
