package com.poc.employee.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.employee.dao.EmployeeDAO;
import com.poc.employee.model.Employee;

public class EmployeeService {

	private static HashMap<Integer, Employee> employeeData = null;

	private EmployeeService() {
	}

	public static HashMap<Integer, Employee> getEmployeeData() {
		if (employeeData == null) {
			employeeData = new HashMap<Integer, Employee>();
		}
		return employeeData;
	}

	public static void loadEmployeeData() throws Exception {
		Employee empdata = null;
		if (EmployeeList.getEmployeesList() != null && !EmployeeList.getEmployeesList().isEmpty()) {
			Iterator<Employee> empIterator = EmployeeList.getEmployeesList().iterator();
			while (empIterator.hasNext()) {
				empdata = empIterator.next();
				empdata.getSubordinatesList().clear();
				EmployeeService.getEmployeeData().put(empdata.getEmployeeID(), empdata);
			}
			Iterator<Employee> toAddManager = EmployeeList.getEmployeesList().iterator();
			while (toAddManager.hasNext()) {
				empdata = toAddManager.next();
				if (empdata.getManagerId() != 0
						&& EmployeeService.getEmployeeData().containsKey(empdata.getManagerId())) {
					Employee manager = EmployeeService.getEmployeeData().get(empdata.getManagerId());
					manager.addEmployee(empdata.getEmployeeID());
					EmployeeService.getEmployeeData().put(empdata.getManagerId(), manager);
				}
			}
		}
		if (EmployeeList.getEmployeesList() != null && !EmployeeList.getEmployeesList().isEmpty()) {
			EmployeeDAO.dumpFile();
		}
	}


	public static List<Employee> printEmployeeWithMaximumSubordinates() {
		List<Employee> listOfMaximumSubordinates = new ArrayList<Employee>();
		int maxSubOrdinates = 0;
		if (EmployeeService.getEmployeeData() != null && !EmployeeService.getEmployeeData().isEmpty()) {
			List<Employee> employees = new ArrayList<Employee>(EmployeeService.getEmployeeData().values());
			Collections.sort(employees, new EmployeeSubordinateComparator());
			for (int i = 0; i < employees.size(); i++) {
				Employee emp = employees.get(i);
				if (i == 0) {
					maxSubOrdinates = emp.getSubordinatesList().size();
				}
				if (maxSubOrdinates == emp.getSubordinatesList().size()) {
					listOfMaximumSubordinates.add(emp);
				} else if (maxSubOrdinates > emp.getSubordinatesList().size()) {
					break;
				}

			}
		}
		return listOfMaximumSubordinates;
	}

	public static String printMaximumSubordinates(List<Employee> employee) {
		StringBuilder result = new StringBuilder();

		if (employee != null && !employee.isEmpty()) {
			result.append("The Maximum Subordinates Employees are:");
			result.append("\n");
			for (Employee data : employee) {
				result.append(
						data.getFirstName() + " " + data.getSecondName() + " " + "Employee ID:" + data.getEmployeeID());
				result.append("\n");
				result.append("Number of Direct Subordinates it is having:" + data.getSubordinatesList().size());
				result.append("\nIDs of the subordinates are following:");
				for (Integer i : data.getSubordinatesList()) {
					result.append("\n ID:" + i);
				}
				result.append("\n\n");
			}

		} else {
			result.append("No employee in the organisation has any subordinates");
		}

		return result.toString();
	}

	public static String printTotalDirectSubordinatesSalary(int empId) {
		StringBuilder result = new StringBuilder();
		if (EmployeeService.getEmployeeData() != null && !EmployeeService.getEmployeeData().isEmpty()) {
			Employee empData = EmployeeService.getEmployeeData().get(empId);
			if (empData != null) {
				long totalSalaryOfSubordinates = 0;
				Iterator<Integer> i = empData.getSubordinatesList().iterator();
				while (i.hasNext()) {
					Employee e = EmployeeService.getEmployeeData().get(i.next());
					result.append("Subordinate Employee ID:" + e.getEmployeeID() + "\n");
					result.append("Salary:" + e.getSalary() + "\n\n");
					totalSalaryOfSubordinates = totalSalaryOfSubordinates + e.getSalary();
				}
				if (totalSalaryOfSubordinates != 0) {
					result.append("Total salary of the Direct subordinates for Employee ID:" + empId + " is "
							+ totalSalaryOfSubordinates);
				} else {
					result.append("There are no subordinates of the Employee");
				}
			} else {
				result.append("Employee is not valid");
			}
		}
		return result.toString();
	}

	public static String printOrganisationStructure() {
		StringBuilder result = new StringBuilder();
		if (EmployeeService.getEmployeeData() != null && !EmployeeService.getEmployeeData().isEmpty()) {
			List<Employee> managers = new ArrayList<Employee>();
			for (Integer i : EmployeeService.getEmployeeData().keySet()) {
				Employee e = EmployeeService.getEmployeeData().get(i);
				if (e.getManagerId() == 0) {
					managers.add(e);
				}
			}
			for (Employee manager : managers) {
				result.append("\n");
				result.append(printStructure(manager));
			}
		}
		return result.toString();
	}

	public static StringBuilder printStructure(Employee e) {
		StringBuilder result = new StringBuilder();
		List<Employee> listForEmployees = new ArrayList<Employee>();
		if (e.getSubordinatesList() != null && !e.getSubordinatesList().isEmpty()) {
			result.append("\nManager------->" + e.getFirstName() + " " + e.getSecondName());
			for (int i : e.getSubordinatesList()) {
				Employee data = EmployeeService.getEmployeeData().get(i);
				result.append("\nUnder Manager " + e.getFirstName() + " , Employee------>" + data.getFirstName());
				listForEmployees.add(data);
			}
			for (Employee sub : listForEmployees) {
				result.append("\n");
				result.append(printStructure(sub));
			}
		} else {
			result.append("\nEmployee------->" + e.getFirstName() + " " + e.getSecondName());
		}
		return result;
	}
}
