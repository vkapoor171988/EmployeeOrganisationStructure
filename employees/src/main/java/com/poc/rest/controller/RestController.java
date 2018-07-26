package com.poc.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.employee.dao.EmployeeDAO;
import com.poc.employee.model.Employee;
import com.poc.employee.utility.EmployeeList;
import com.poc.employee.utility.EmployeeService;

@Path("/employeeOrg")
public class RestController implements RestControllerInterface {

	@Path("/loadEmployeeData")
	@POST
	@Consumes("application/json")
	public Response loadEmployeeData(String empData) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Employee> list = mapper.readValue(empData,
					mapper.getTypeFactory().constructCollectionType(List.class, Employee.class));
			EmployeeList.setEmployeesList(list);
			EmployeeService.loadEmployeeData();
			return Response.status(Status.ACCEPTED).entity("Uploaded Successfully").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("Uploading Error").build();
		}
	}

	@Path("/changeManager/{id}")
	@POST
	@Consumes("application/json")
	public Response changeManagerOfEmployee(String mData, @PathParam("id") int empId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (mData != null) {
				List<Employee> list = null;
				if (EmployeeList.getEmployeesList() == null || EmployeeList.getEmployeesList().isEmpty()) {
					list = EmployeeDAO.getEmployeeListFromFile();
					EmployeeList.setEmployeesList(list);
					EmployeeService.loadEmployeeData();
				}
				if (EmployeeList.getEmployeesList() == null) {
					return Response.status(Status.BAD_REQUEST).entity("Employee Data Not Found").build();
				} else {
					Employee e = EmployeeService.getEmployeeData().get(empId);
					if (e != null) {
						Integer oldManagerId = e.getManagerId();
						HashMap map = mapper.readValue(mData, HashMap.class);
						Integer newManagerId = (Integer) map.get("Manager ID");
						EmployeeService.getEmployeeData().get(empId).setManagerId(newManagerId);
						int indexOfObjcet=EmployeeService.getEmployeeData().get(oldManagerId).getSubordinatesList().indexOf(empId);
						EmployeeService.getEmployeeData().get(oldManagerId).getSubordinatesList().remove(indexOfObjcet);
						EmployeeService.getEmployeeData().get(newManagerId).addEmployee(empId);
						List<Employee> employeeList = new ArrayList<Employee>(
								EmployeeService.getEmployeeData().values());
						EmployeeList.setEmployeesList(employeeList);
						EmployeeService.loadEmployeeData();
						return Response.status(Status.ACCEPTED).entity("Changed Successfully").build();
					} else {
						return Response.status(Status.BAD_REQUEST).entity("Employee Data Not Found").build();
					}
				}
			} else {
				return Response.status(Status.BAD_REQUEST).entity("Please enter valid Manager ID").build();
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("Change Error").build();
		}
	}

	@Path("/getEmployeeData/getMaxSubordinateEmployee")
	@GET
	public Response getMaximumEmployeeSubordinate() {
		try {
			List<Employee> list = null;
			if (EmployeeList.getEmployeesList() == null || EmployeeList.getEmployeesList().isEmpty()) {
				list = EmployeeDAO.getEmployeeListFromFile();
				EmployeeList.setEmployeesList(list);
				EmployeeService.loadEmployeeData();
			}
			if (EmployeeList.getEmployeesList() == null) {
				return Response.status(Status.BAD_REQUEST).entity("Employee Data Not Found").build();
			} else {
				List<Employee> listwithMax = EmployeeService.printEmployeeWithMaximumSubordinates();
				String result = EmployeeService.printMaximumSubordinates(listwithMax);
				return Response.status(Status.ACCEPTED).entity(result).build();
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("Uploading Error").build();
		}
	}

	@Path("/getEmployeeData/getTotalSubordinateSalary/{id}")
	@GET
	public Response getTotalSubordinatesSalary(@PathParam("id") int empId) {
		try {
			if (empId != 0) {
				List<Employee> list = null;
				if (EmployeeList.getEmployeesList() == null || EmployeeList.getEmployeesList().isEmpty()) {
					list = EmployeeDAO.getEmployeeListFromFile();
					EmployeeList.setEmployeesList(list);
					EmployeeService.loadEmployeeData();
				}
				if (EmployeeList.getEmployeesList() == null) {
					return Response.status(Status.BAD_REQUEST).entity("Employee Data Not Found").build();
				} else {
					String result = EmployeeService.printTotalDirectSubordinatesSalary(empId);
					return Response.status(Status.ACCEPTED).entity(result).build();
				}
			} else {
				return Response.status(Status.BAD_REQUEST).entity("Employee is not Valid").build();
			}

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("Data Error").build();
		}

	}

	@Path("/getOrganisationStructure")
	@GET
	public Response printOrganisationStructure() {
		try {
			List<Employee> list = null;
			if (EmployeeList.getEmployeesList() == null || EmployeeList.getEmployeesList().isEmpty()) {
				list = EmployeeDAO.getEmployeeListFromFile();
				EmployeeList.setEmployeesList(list);
				EmployeeService.loadEmployeeData();
			}
			if (EmployeeList.getEmployeesList() == null) {
				return Response.status(Status.BAD_REQUEST).entity("Employee Data Not Found").build();
			} else {
				String result = EmployeeService.printOrganisationStructure();
				return Response.status(Status.ACCEPTED).entity(result).build();
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("Data Error").build();
		}
	}

}
