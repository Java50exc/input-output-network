package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.*;

import telran.employees.dto.Employee;
import telran.employees.service.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTests {
	final static String TEST_FILE_NAME = "test.data";
	
	private final static Employee EMPLOYEE = new Employee(100, "name", "department", 10000, LocalDate.now());
	
	Company company;

	@BeforeEach
	void setUp() {
		company = new CompanyImpl();
	}

	@Test
	void addEmployee_correctFlow_success() {
		company.addEmployee(EMPLOYEE);
		assertTrue(company.getEmployees().size() == 1);
		company.addEmployee(EMPLOYEE);
		assertTrue(company.getEmployees().size() == 1);
	}

	@Test
	void removeEmployee_correctFlow_success() {
		company.addEmployee(EMPLOYEE);
		assertTrue(company.getEmployees().size() == 1);
		company.removeEmployee(EMPLOYEE.id());
		assertTrue(company.getEmployees().isEmpty());
	}
	
	@Test
	void removeEmployee_notExists_returnsNull() {
		company.addEmployee(EMPLOYEE);
		assertNull(company.removeEmployee(EMPLOYEE.id() + 1));
		assertFalse(company.getEmployees().isEmpty());
	}

	@Test
	void getEmployee_correctFlow_success() {
		company.addEmployee(EMPLOYEE);
		assertEquals(EMPLOYEE, company.getEmployee(EMPLOYEE.id()));
	}
	
	@Test
	void getEmployee_notExists_returnsNull() {
		company.addEmployee(EMPLOYEE);
		assertNull(company.getEmployee(EMPLOYEE.id() + 1));
	}

	@Test
	void getEmployees_correctFlow_success() {
		company.addEmployee(EMPLOYEE);
		assertIterableEquals(List.of(EMPLOYEE), company.getEmployees());
	}
	
	@Test
	@Order(1)
	void save_correctFlow_success() {
		company.addEmployee(EMPLOYEE);
		company.save(TEST_FILE_NAME);
	}
	
	@Test
	@Order(2)
	void restore_correctFlow_success() {
		company.restore(TEST_FILE_NAME);
		company.getEmployees().forEach(e -> assertEquals(EMPLOYEE, e));
	}

}
