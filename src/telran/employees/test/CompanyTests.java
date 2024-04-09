package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.*;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTests {
	final static String TEST_FILE_NAME = "test.data";
	
	private final static Employee EMPLOYEE1 = new Employee(100, "name", "department1", 10000, LocalDate.now());
	private final static Employee EMPLOYEE2 = new Employee(101, "name", "department1", 5000, LocalDate.now());
	private final static Employee EMPLOYEE3 = new Employee(102, "name", "department2", 10000, LocalDate.now());
	private final static Employee EMPLOYEE4 = new Employee(103, "name", "department2", 7000, LocalDate.now());
	private final static Employee EMPLOYEE5 = new Employee(104, "name", "department3", 15000, LocalDate.now());
	private final static List<Employee> EMPLOYEES = List.of(EMPLOYEE1, EMPLOYEE2, EMPLOYEE3, EMPLOYEE4, EMPLOYEE5);
	Company company;

	@BeforeEach
	void setUp() {
		company = new CompanyImpl();
		company.addEmployee(EMPLOYEE1);
		company.addEmployee(EMPLOYEE2);
		company.addEmployee(EMPLOYEE3);
		company.addEmployee(EMPLOYEE4);
		company.addEmployee(EMPLOYEE5);
	}

	@Test
	void addEmployee_correctFlow_success() {
		assertTrue(company.getEmployees().size() == 5);
	}
	
	@Test
	void addEmployee_alreadyExists_returnsFalse() {
		assertFalse(company.addEmployee(EMPLOYEE1));
		assertTrue(company.getEmployees().size() == 5);
	}

	@Test
	void removeEmployee_correctFlow_success() {
		assertTrue(company.getEmployees().size() == 5);
		assertEquals(EMPLOYEE1, company.removeEmployee(EMPLOYEE1.id()));
		assertTrue(company.getEmployees().size() == 4);
		
	}
	
	@Test
	void removeEmployee_notExists_returnsNull() {
		assertNull(company.removeEmployee(EMPLOYEE1.id() + 100));
		assertTrue(company.getEmployees().size() == 5);
	}

	@Test
	void getEmployee_correctFlow_success() {
		assertEquals(EMPLOYEE1, company.getEmployee(EMPLOYEE1.id()));
	}
	
	@Test
	void getEmployee_notExists_returnsNull() {
		assertNull(company.getEmployee(EMPLOYEE1.id() + 100));
	}

	@Test
	void getEmployees_correctFlow_success() {
		assertIterableEquals(EMPLOYEES, company.getEmployees());
	}
	
	@Test
	@Order(1)
	void save_correctFlow_success() {
		company.save(TEST_FILE_NAME);
	}
	
	@Test
	@Order(2)
	void restore_correctFlow_success() {
		company = new CompanyImpl();
		company.restore(TEST_FILE_NAME);
		assertIterableEquals(EMPLOYEES, company.getEmployees());
	}
	
	@Test
	void getSalaryDistribution_correctFlow_success() {
		List<SalaryDistribution> expected = List.of(new SalaryDistribution(5000, 10000, 2), new SalaryDistribution(10000, 15000, 2), new SalaryDistribution(15000, 20000, 1));
		assertIterableEquals(expected, company.getSalaryDistribution(5000));
	}
	
	@Test
	void getDepartmentSalaryDistribution_correctFlow_success() {
		List<DepartmentSalary> expected = List.of(new DepartmentSalary("department1", 7500), new DepartmentSalary("department2", 8500), new DepartmentSalary("department3", 15000));
		assertIterableEquals(expected, company.getDepartmentSalaryDistribution());
	}
	
	@Test
	void getEmployeesByDepartment_correctFlow_success() {
		assertIterableEquals(List.of(EMPLOYEE1, EMPLOYEE2), company.getEmployeesByDepartment("department1"));
	}
	
	@Test
	void getEmployeesBySalary_correctFlow_success() {
		assertIterableEquals(List.of(EMPLOYEE2, EMPLOYEE4), company.getEmployeesBySalary(5000, 10000));
	}
	
	
	private int getAge(LocalDate birthDate) {
		return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
	}
	


}
