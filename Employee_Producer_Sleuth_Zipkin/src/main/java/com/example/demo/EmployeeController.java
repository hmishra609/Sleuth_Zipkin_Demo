package com.example.demo;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;



@RestController
public class EmployeeController {
	
	@GetMapping("/")
	String wel() {
		return "Welcome";
	}
	
	
	public static Logger logger=LoggerFactory.getLogger(EmployeeController.class);
	
	private static Map<Integer, Employee> mapOfEmloyeess = new LinkedHashMap<Integer, Employee>();
	static int count=10004;
	static
	{
		mapOfEmloyeess.put(10001, new Employee("Jack",10001,12345.6,1001));
		mapOfEmloyeess.put(10002, new Employee("Justin",10002,12355.6,1002));
		mapOfEmloyeess.put(10003, new Employee("Eric",10003,12445.6,1003));
	}
	
	
	@RequestMapping(value="emp/controller/getDetails",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Employee>> getEmployeeDetails(){
		logger.info("From producer method[getEmployeeDetails] start");
		Collection <Employee> listEmployee = mapOfEmloyeess.values();
		logger.info("From producer method[getEmployeeDetails] end");
		return new ResponseEntity<Collection<Employee>>(listEmployee, HttpStatus.OK);
	}
	
	@RequestMapping(value="emp/controller/getDetailsById/{id}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> getEmployeeDetailByEmployeeId(@PathVariable("id") int myId){
		logger.info("From producer method[getEmployeeDetailByEmployeeId] start");
		Employee employee = mapOfEmloyeess.get(myId);
		if(employee!=null){
			logger.info("From producer method[getEmployeeDetailByEmployeeId] end");
			return new ResponseEntity<Employee>(employee,HttpStatus.OK);
		}
		else{
			logger.info("From producer method[getEmployeeDetailByEmployeeId] end");
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(value="/emp/controller/addEmp",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee){
		logger.info("From producer method[addEmployee] start");
		count++;
		mapOfEmloyeess.put(count,employee);
		employee.setEmployeeId(count);
		logger.info("From producer method[addEmployee] end");
		return new ResponseEntity<String>("Employee added successfully with id:"+count,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/emp/controller/updateEmp",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
		logger.info("From producer method[updateEmployee] start");
		if(mapOfEmloyeess.get(employee.getEmployeeId())==null){
			Employee employee2=null;
			return new ResponseEntity<Employee>(employee2,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println(employee);
		mapOfEmloyeess.put(employee.getEmployeeId(),employee);
		logger.info("From producer method[updateEmployee] end");
		return new ResponseEntity<Employee>(employee,HttpStatus.OK);
	}
	@RequestMapping(value="/emp/controller/deleteEmp/{id}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") int myId){
		logger.info("From producer method[deleteEmployee] start");
		if(mapOfEmloyeess.get(myId)==null){
			Employee employee2=null;
			return new ResponseEntity<Employee>(employee2,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Employee employee = mapOfEmloyeess.remove(myId);
		System.out.println("Removed: "+employee);
		logger.info("From producer method[deleteEmployee] end");
		return new ResponseEntity<Employee>(employee,HttpStatus.OK);
		
	}
}
