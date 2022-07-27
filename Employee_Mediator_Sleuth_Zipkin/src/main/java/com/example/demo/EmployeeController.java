package com.example.demo;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Employee;


@RestController
public class EmployeeController {
	
	@Autowired
	RestTemplate restTemplate;
	
	static String URL ="http://localhost:8090/";
	
	private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="emp/controller/getDetails",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Employee>> getEmployeeDetails(){
		logger.info("From Mediator Controller From method: [getEmployeeDetails] start");
		List list= restTemplate.getForObject(URL+"emp/controller/getDetails",List.class);
		logger.info("From Mediator Controller From method: [getEmployeeDetails] end");
		return new ResponseEntity<Collection<Employee>>(list, HttpStatus.OK);
	}
	@RequestMapping(value="emp/controller/getDetailsById/{id}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> getEmployeeDetailByEmployeeId(@PathVariable("id") int myId){
		logger.info("From Mediator Controller From method: [getEmployeeDetailByEmployeeId] start");
		Employee employee = restTemplate.getForObject(URL+"emp/controller/getDetailsById/"+myId,Employee.class);
		if(employee!=null){
			logger.info("From Mediator Controller From method: [getEmployeeDetailByEmployeeId] end");
			return new ResponseEntity<Employee>(employee,HttpStatus.OK);
		}
		else{
			logger.info("From Mediator Controller From method: [getEmployeeDetailByEmployeeId] end");
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(value="/emp/controller/addEmp",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee){
		logger.info("From Mediator Controller From method: [addEmployee] start");
		Integer count=restTemplate.postForObject(URL+"emp/controller/addEmp", new HttpEntity<Employee>(employee),Integer.class );
		employee.setEmployeeId(count);
		logger.info("From Mediator Controller From method: [addEmployee] end");
		return new ResponseEntity<String>("Employee added successfully with id:"+count,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/emp/controller/updateEmp",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
		logger.info("From Mediator Controller From method: [updateEmployee] start");		
		Employee employee1 = restTemplate.getForObject(URL+"emp/controller/getDetailsById/"+employee.getEmployeeId(),Employee.class);
		ResponseEntity<Employee> res=null;
		if(employee1!=null){
			logger.info("From Mediator Controller From method: [updateEmployee] if");
			res= restTemplate.exchange(URL+"emp/controller/updateEmp", HttpMethod.PUT, new HttpEntity<Employee>(employee), Employee.class);
		}
		else{
			logger.info("From Mediator Controller From method: [updateEmployee] else");
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		logger.info("From Mediator Controller From method: [updateEmployee] end");
		return res;
	}
	@RequestMapping(value="/emp/controller/deleteEmp/{id}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") int myId){
		logger.info("From Mediator Controller From method: [deleteEmployee] start");
		Employee employee1 = restTemplate.getForObject(URL+"emp/controller/getDetailsById/"+myId,Employee.class);
		ResponseEntity<Employee> res=null;
		if(employee1!=null){
			logger.info("From Mediator Controller From method: [deleteEmployee] if");
			res= restTemplate.exchange(URL+"emp/controller/deleteEmp/"+myId, HttpMethod.DELETE, null, Employee.class);
		}
		else{
			logger.info("From Mediator Controller From method: [deleteEmployee] else");
			res= new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		logger.info("From Mediator Controller From method: [deleteEmployee] end");
		return res;
	}
}
