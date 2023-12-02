package com.mourid.employeeapp.ui.employee;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mourid.employeeapp.R;
import com.mourid.employeeapp.adapters.ServiceAdapter;
import com.mourid.employeeapp.api.EmployeApi;
import com.mourid.employeeapp.api.RetrofitEmploye;
import com.mourid.employeeapp.api.RetrofitService;
import com.mourid.employeeapp.api.ServiceApi;
import com.mourid.employeeapp.entities.Employee;
import com.mourid.employeeapp.entities.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeDialogFragment extends DialogFragment {
    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText dateNaissanceEditText;
    private Spinner serviceSpinner;
    private Button cancel;
    private Button add;

    private Context parentFragmentContext;

    public AddEmployeeDialogFragment(Context context) {
        this.parentFragmentContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addemployee, container, false);

        nomEditText = view.findViewById(R.id.nom);
        prenomEditText = view.findViewById(R.id.prenom);
        dateNaissanceEditText = view.findViewById(R.id.dateDeNaissance);
        serviceSpinner = view.findViewById(R.id.spinner);
        cancel = view.findViewById(R.id.cancel);
        add = view.findViewById(R.id.ok2);
        populateSpinner();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomEditText.getText().toString();
                String prenom = prenomEditText.getText().toString();
                String dateNaissance = dateNaissanceEditText.getText().toString();
                Service selectedService = (Service) serviceSpinner.getSelectedItem();

                // Create an Employee object and add the logic to add it to the server
                // You need to modify the Employee class and API accordingly
                // For demonstration purposes, I'm creating a Service object here
                Employee employee = new Employee(0L, nom, prenom, dateNaissance, selectedService);
                addEmployee(employee);

                dismiss();
            }
        });

        return view;
    }

    private void addEmployee(Employee employee) {
        // Implement the logic to add an employee to the server using Retrofit
        // You need to modify the API accordingly
        // For demonstration purposes, I'm creating a ServiceApi object here
         EmployeApi employeApi = RetrofitEmploye.getClient().create(EmployeApi.class);
        Call<Void> call = employeApi.addEmployee(employee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle the response from the server
                Log.d("rep",response.toString());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle the failure

            }
        });
    }
    private void populateSpinner() {
        ServiceApi serviceApi = RetrofitService.getClient().create(ServiceApi.class);
        Call<List<Service>> call = serviceApi.getAllServices();
        call.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                List<Service> services = response.body();

                // Create an ArrayAdapter using a simple spinner layout and your list of services
                ArrayAdapter<Service> adapter = new ArrayAdapter<>(parentFragmentContext, android.R.layout.simple_spinner_item, services);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                serviceSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                // Handle failure

            }
        });
    }
}
