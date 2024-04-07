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
		company.addEmployee(EMPLOYEE);
	}

	@Test
	void addEmployee_correctFlow_success() {
		assertTrue(company.getEmployees().size() == 1);
	}
	
	@Test
	void addEmployee_alreadyExists_returnsFalse() {
		assertFalse(company.addEmployee(EMPLOYEE));
		assertTrue(company.getEmployees().size() == 1);
	}

	@Test
	void removeEmployee_correctFlow_success() {
		assertTrue(company.getEmployees().size() == 1);
		assertEquals(EMPLOYEE, company.removeEmployee(EMPLOYEE.id()));
		assertTrue(company.getEmployees().isEmpty());
	}
	
	@Test
	void removeEmployee_notExists_returnsNull() {
		assertNull(company.removeEmployee(EMPLOYEE.id() + 1));
		assertFalse(company.getEmployees().isEmpty());
	}

	@Test
	void getEmployee_correctFlow_success() {
		assertEquals(EMPLOYEE, company.getEmployee(EMPLOYEE.id()));
	}
	
	@Test
	void getEmployee_notExists_returnsNull() {
		assertNull(company.getEmployee(EMPLOYEE.id() + 1));
	}

	@Test
	void getEmployees_correctFlow_success() {
		assertIterableEquals(List.of(EMPLOYEE), company.getEmployees());
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
		company.getEmployees().forEach(e -> assertEquals(EMPLOYEE, e));
	}

}
