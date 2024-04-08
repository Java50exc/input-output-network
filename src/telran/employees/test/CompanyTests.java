package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.*;

import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTests {
	final static String TEST_FILE_NAME = "test.data";
	
	private final static Employee EMPLOYEE1 = new Employee(100, "name", "department", 10000, LocalDate.now());
	private final static Employee EMPLOYEE2 = new Employee(101, "name", "department", 5000, LocalDate.now());
	private final static Employee EMPLOYEE3 = new Employee(102, "name", "department", 10000, LocalDate.now());
	private final static Employee EMPLOYEE4 = new Employee(103, "name", "department", 7000, LocalDate.now());
	private final static Employee EMPLOYEE5 = new Employee(104, "name", "department", 15000, LocalDate.now());
	private static final List<Employee> EMPLOYEES = List.of(EMPLOYEE1, EMPLOYEE2, EMPLOYEE3, EMPLOYEE4, EMPLOYEE5);
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
		assertTrue(company.getEmployees().size() == 1);
	}
	
	@Test
	void addEmployee_alreadyExists_returnsFalse() {
		assertFalse(company.addEmployee(EMPLOYEE1));
		assertTrue(company.getEmployees().size() == 1);
	}

	@Test
	void removeEmployee_correctFlow_success() {
		assertTrue(company.getEmployees().size() == 1);
		assertEquals(EMPLOYEE1, company.removeEmployee(EMPLOYEE1.id()));
		assertTrue(company.getEmployees().isEmpty());
	}
	
	@Test
	void removeEmployee_notExists_returnsNull() {
		assertNull(company.removeEmployee(EMPLOYEE1.id() + 1));
		assertFalse(company.getEmployees().isEmpty());
	}

	@Test
	void getEmployee_correctFlow_success() {
		assertEquals(EMPLOYEE1, company.getEmployee(EMPLOYEE1.id()));
	}
	
	@Test
	void getEmployee_notExists_returnsNull() {
		assertNull(company.getEmployee(EMPLOYEE1.id() + 1));
	}

	@Test
	void getEmployees_correctFlow_success() {
		assertIterableEquals(List.of(EMPLOYEE1), company.getEmployees());
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

}
