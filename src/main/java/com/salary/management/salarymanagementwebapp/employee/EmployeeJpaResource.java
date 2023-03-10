package com.salary.management.salarymanagementwebapp.employee;

import com.salary.management.salarymanagementwebapp.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class EmployeeJpaResource {


	@Autowired
	private EmployeeJpaRepository employeeJpaRepository;

	/*@GetMapping("/jpa/users/employees")
	public List<Employee> getAllEmployees(){
		return employeeJpaRepository.findAll();
	}*/

	@GetMapping("/jpa/users/employees")
	@ResponseBody
	public ResponseEntity<Object> getAllEmployees
			(@RequestParam(defaultValue = "0") BigDecimal minSalary,
			 @RequestParam(defaultValue = "99999999") BigDecimal maxSalary,
			 @RequestParam(defaultValue = "ASC") String sort,
			 @RequestParam(defaultValue = "0") int pageNumber,
			 @RequestParam(defaultValue = "30") int pageSize,
			 @RequestParam(defaultValue = "id") String sortActive){
		HashMap<String, Object> responseMap = new HashMap<>();
		try {
			long totalNumberOfEmployees = 0;
			List<Employee> employeeList = new ArrayList<>();
			Pageable employeePagination = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(sort.toUpperCase()), sortActive);
			Page<Employee> employeePage = employeeJpaRepository.findEmployeeBySalary(minSalary, maxSalary, employeePagination);

			if (employeePage != null){
				employeeList = employeePage.getContent();
				totalNumberOfEmployees = employeePage.getTotalElements();
			}
			responseMap.put("results", employeeList);
			responseMap.put("totalNumberOfEmployees", totalNumberOfEmployees);
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@GetMapping("/jpa/users/employees/{id}")
	public Employee getEmployee(@PathVariable String id){
		return employeeJpaRepository.findById(id).get();
	}

	@DeleteMapping("/jpa/users/employees/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable String id){
		

		employeeJpaRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping("/jpa/users/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee employee){
		
		Employee todoUpdated = employeeJpaRepository.save(employee);
		
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	@PostMapping("/jpa/users/employees/{id}")
	public ResponseEntity<Employee> createEmployee(
			@PathVariable String id, @RequestBody Employee employee){

		Employee createdEmployee = employeeJpaRepository.save(employee);


		/*URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdEmployee.getId()).toUri();
		
		return ResponseEntity.created(uri).build();*/
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
		
}
