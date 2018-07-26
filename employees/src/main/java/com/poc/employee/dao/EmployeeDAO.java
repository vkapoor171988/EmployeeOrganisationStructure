package com.poc.employee.dao;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.employee.model.Employee;
import com.poc.employee.utility.EmployeeList;

public class EmployeeDAO {
	
	public static void dumpFile() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(new File("").getAbsolutePath() + "//EmployeeRecords.json"),
				EmployeeList.getEmployeesList());
	}

	public static List<Employee> getEmployeeListFromFile() throws Exception {
		List<Employee> list = null;
		ObjectMapper mapper = new ObjectMapper();
		list = mapper.readValue(new File(new File("").getAbsolutePath() + "//EmployeeRecords.json"),
				mapper.getTypeFactory().constructCollectionType(List.class, Employee.class));
		if (list != null && !list.isEmpty()) {
			return list;
		}
		return null;
	}

}
