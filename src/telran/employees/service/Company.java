package telran.employees.service;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import telran.employees.dto.*;

public interface Company {
	boolean addEmployee(Employee empl);

	Employee removeEmployee(long id);

	Employee getEmployee(long id);

	List<Employee> getEmployees();

	@SuppressWarnings("unchecked")
	default void restore(String dataFile) {
		List<Employee> employees = null;

		if (Files.exists(Path.of(dataFile))) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
				employees = (List<Employee>) in.readObject();
			} catch (Exception e) {
				new RuntimeException(e.getMessage());
			}
			employees.forEach(this::addEmployee);
		}
	}

	default void save(String dataFile) {
		List<Employee> employees = getEmployees();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
			out.writeObject(employees);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	List<DepartmentSalary> getDepartmentSalaryDistribution();

	List<SalaryDistribution> getSalaryDistribution(int interval);

	List<Employee> getEmployeesByDepartment(String department);

	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);

	List<Employee> getEmployeesByAge(int ageFrom, int ageTo);

	Employee updateSalary(long id, int newSalary);

	Employee updateDepartment(long id, String department);

}
