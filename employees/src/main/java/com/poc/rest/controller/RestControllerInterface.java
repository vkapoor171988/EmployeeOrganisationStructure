package com.poc.rest.controller;

import javax.ws.rs.core.Response;

public interface RestControllerInterface {

	public Response loadEmployeeData(String empData);

	public Response changeManagerOfEmployee(String mData,int empId);

	public Response getMaximumEmployeeSubordinate();

	public Response getTotalSubordinatesSalary(int empId);

	public Response printOrganisationStructure();

}
