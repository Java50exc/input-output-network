package telran.employees.service;

import java.util.List;
import telran.employees.dto.Employee;

public interface Company {
	boolean addEmployee(Employee empl);
	Employee removeEmployee(long id);
	Employee getEmployee(long id);
	List<Employee> getEmployees();
	
	default void restore(String dataFile) {
		
	}
	
	default void save(String dataFile) {
		
	}
	

}
