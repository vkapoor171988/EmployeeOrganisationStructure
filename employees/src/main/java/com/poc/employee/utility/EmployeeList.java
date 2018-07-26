package com.poc.employee.utility;

import java.util.ArrayList;
import java.util.List;

import com.poc.employee.model.Employee;

public class EmployeeList {

	private static List<Employee> employeesList = null;

	public static List<Employee> getEmployeesList() {
		return employeesList;
	}
	
	public static void setEmployeesList(List<Employee> empList) {
		employeesList=empList;
	}

	public static void addEmployee(Employee e) {
		if (employeesList == null) {
			employeesList = new ArrayList<Employee>();
		}
		employeesList.add(e);
	}
}
