package telran.employees.service;

import java.io.*;
import java.util.List;
import static telran.employees.api.ErrorMessages.*;
import telran.employees.dto.Employee;

public interface Company {
	boolean addEmployee(Employee empl);
	Employee removeEmployee(long id);
	Employee getEmployee(long id);
	List<Employee> getEmployees();
	
	@SuppressWarnings("unchecked")
	default void restore(String dataFile) {
		List<Employee> employees = null;
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
			employees = (List<Employee>) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			new RuntimeException(e.getMessage());
		}
		employees.forEach(this::addEmployee);
	}
	
	default void save(String dataFile) {
		List<Employee> employees = getEmployees();
		
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
			out.writeObject(employees);
		} catch (IOException e) {
			throw new RuntimeException(SAVE_ERROR_MESSAGE);
		}
	}
	

}
