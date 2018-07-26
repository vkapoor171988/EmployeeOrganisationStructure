package com.poc.employee.utility;

import java.util.Comparator;

import com.poc.employee.model.Employee;

public class EmployeeSubordinateComparator implements Comparator<Employee> {

	public int compare(Employee emp1, Employee emp2) {
		if (emp1.getSubordinatesList().size() < emp2.getSubordinatesList().size()) {
			return 1;
		} else if (emp1.getSubordinatesList().size() > emp2.getSubordinatesList().size()) {
			return -1;
		} else {
			return 0;
		}
	}

}
