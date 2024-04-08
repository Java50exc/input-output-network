package telran.employees.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import telran.employees.dto.*;

public class CompanyImpl implements Company {
	HashMap<Long, Employee> employees = new HashMap<>();
	HashMap<String, List<Employee>> employeesByDepartment = new HashMap<>();
	TreeMap<Integer, List<Employee>> employeesByAge = new TreeMap<>();
	TreeMap<Integer, List<Employee>> employeesBySalary = new TreeMap<>();

	@Override
	public boolean addEmployee(Employee empl) {
		boolean res = employees.putIfAbsent(empl.id(), empl) == null;
		if (res) {
			addEmployeeMulti(empl);
		}
		return res;
	}

	private void addEmployeeMulti(Employee empl) {
		addEmployee(empl, employeesByAge, getAge(empl.birthDate()));
		addEmployee(empl, employeesByDepartment, empl.department());
		addEmployee(empl, employeesBySalary, empl.salary());
	}
	
	private <T> void addEmployee(Employee empl, Map<T, List<Employee>> table, T index) {
		table.computeIfAbsent(index, k -> new LinkedList<>()).add(empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee empl = employees.remove(id);

		if (empl != null) {
			removeEmployeeMulti(empl);
		}
		return empl;
	}

	private void removeEmployeeMulti(Employee empl) {
		removeEmployee(empl, employeesByAge, getAge(empl.birthDate()));
		removeEmployee(empl, employeesByDepartment, empl.department());
		removeEmployee(empl, employeesBySalary, empl.salary());
	}
	
	private <T> void removeEmployee(Employee empl, Map<T, List<Employee>> table, T index) {
		List<Employee> list = table.get(index);
		list.remove(empl);
		
		if (list.isEmpty()) {
			table.remove(index);
		}
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public List<Employee> getEmployees() {
		return new ArrayList<>(employees.values());
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		return employees.values().stream()
				.collect(Collectors.groupingBy(Employee::department, Collectors.averagingLong(Employee::salary)))
				.entrySet().stream().map(e -> new DepartmentSalary(e.getKey(), e.getValue())).toList();
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		Map<Integer, Long> mapIntervalNumbers = employees.values().stream()
				.collect(Collectors.groupingBy(e -> e.salary() / interval, Collectors.counting()));
		return mapIntervalNumbers
				.entrySet().stream().map(e -> new SalaryDistribution(e.getKey() * interval,
						e.getKey() * interval + interval, e.getValue().intValue()))
				.sorted((sd1, sd2) -> Integer.compare(sd1.min(), sd2.min())).toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return employeesByDepartment.get(department);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return employeesBySalary
				.subMap(salaryFrom, salaryTo)
				.values().stream()
				.flatMap(List::stream)
				.toList();
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		return employeesByAge
				.subMap(ageFrom, ageTo)
				.values()
				.stream()
				.flatMap(List::stream)
				.toList();
	}

	private int getAge(LocalDate birthDate) {
		return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		Employee empl = removeEmployee(id);
		Employee newEmpl = new Employee(id, empl.name(), empl.department(), newSalary, empl.birthDate());
		addEmployee(newEmpl);
		return empl;
	}

	@Override
	public Employee updateDepartment(long id, String department) {
		Employee empl = removeEmployee(id);
		Employee newEmpl = new Employee(id, empl.name(), department, empl.salary(), empl.birthDate());
		addEmployee(newEmpl);
		return empl;
	}

}
