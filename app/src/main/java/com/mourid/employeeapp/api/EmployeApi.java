package com.mourid.employeeapp.api;

import com.mourid.employeeapp.entities.Employee;
import com.mourid.employeeapp.entities.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmployeApi {
        @GET("all")
        Call<List<Employee>> getAll();

        @GET("{id}")
        Call <Employee> getEmployeById(@Path("id") Long id);

        @POST("create")
        Call <Void> addEmployee(@Body Employee employee);

        @PUT("{id}")
        Call <Void> updateEmployee(@Path("id") Long id , @Body Employee employee);

        @DELETE("{id}")
        Call <Void> deleteEmployee(@Path("id") Long id);
    }

