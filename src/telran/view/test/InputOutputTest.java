package telran.view.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;
import telran.view.*;

class InputOutputTest {
	InputOutput io = new SystemInputOutput();

	@Test
	void readEmployeesString() {
		Employee empl = io.readObject("Enter employee <id>#<name>#<iso birthdate>#<department>#<salary>", "Wrong employee", 
				str -> {
					String[] tokens = str.split("#");
					if(tokens.length != 5) {
						throw new RuntimeException("must be 5 tokens");
					}
					return new Employee(Long.parseLong(tokens[0]), tokens[1], tokens[3], Integer.parseInt(tokens[4]), LocalDate.parse(tokens[2]));
				});
		io.writeObjectLine(empl);
	}
	
	@Test
	void testReadEmployeeBySeparateFields() {
		
	}

}
