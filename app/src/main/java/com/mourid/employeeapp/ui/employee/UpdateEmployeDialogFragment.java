package com.mourid.employeeapp.ui.employee;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.mourid.employeeapp.api.RetrofitEmploye;
import com.mourid.employeeapp.api.EmployeApi;
import com.mourid.employeeapp.api.RetrofitService;
import com.mourid.employeeapp.api.ServiceApi;
import com.mourid.employeeapp.entities.Employee;
import com.mourid.employeeapp.entities.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmployeDialogFragment extends DialogFragment {
    private Long id;

    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText dateNaissanceEditText;
    private Spinner serviceSpinner;
    private Button update;
    private Button delete;
    private Context parentFragmentContext;
    private Employee employee;

    public UpdateEmployeDialogFragment (Long id,Context context) {
        this.id = id;
        this.parentFragmentContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updateemployee, container, false);

        nomEditText = view.findViewById(R.id.nom);
        prenomEditText = view.findViewById(R.id.prenom);
        dateNaissanceEditText = view.findViewById(R.id.dateDeNaissance);
        serviceSpinner = view.findViewById(R.id.spinner);
        delete = view.findViewById(R.id.delete);
        update = view.findViewById(R.id.update);
        populateSpinner();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showConfirmationDialog();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employee.setNom(nomEditText.getText().toString());
                employee.setDateNaissance(dateNaissanceEditText.getText().toString());
                employee.setPrenom(prenomEditText.getText().toString());
                employee.setService((Service) serviceSpinner.getSelectedItem());
                updateEmloyee(employee,employee.getId());
                dismiss();
            }
        });
        getEmployee(id);
        return view;
    }

    public void getEmployee(long id){
        EmployeApi employeApi=RetrofitEmploye.getClient().create(EmployeApi.class);
        Call<Employee> call=employeApi.getEmployeById(id);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                employee = response.body();
                    nomEditText.setText(employee.getNom().toString());
                    prenomEditText.setText(employee.getPrenom().toString());
                    dateNaissanceEditText.setText(employee.getDateNaissance().toString());
                    int servicePosition = findServicePosition(employee.getService());

// Set the selected item in the Spinner
                    serviceSpinner.setSelection(servicePosition);

            }

            private int findServicePosition(Service selectedService) {
                if (selectedService != null) {
                    ArrayAdapter<Service> adapter = (ArrayAdapter<Service>) serviceSpinner.getAdapter();
                    if (adapter != null) {
                        for (int i = 0; i < adapter.getCount(); i++) {
                            Log.d("ServiceComparison", "Selected Service ID: " );
                            Service service = adapter.getItem(i);
                            if (service != null) {
                                Log.d("ServiceComparison", "Selected Service ID: " + selectedService.getId());
                                Log.d("ServiceComparison", "Current Service ID: " + service.getId());

                                if (service.getId().equals(selectedService.getId())) {
                                    Log.d("ServiceComparison", "Match found at position: " + i);
                                    return i;
                                }
                            }
                        }
                    }
                }

                Log.d("ServiceComparison", "No match found, returning default position 0");
                return 0; // Default position if the adapter or service is not found
            }


            @Override
            public void onFailure(Call<Employee> call, Throwable t) {

            }
        });

    }

    public void updateEmloyee(Employee employee,Long id) {
        EmployeApi employeApi=RetrofitEmploye.getClient().create(EmployeApi.class);
        Call<Void> call = employeApi.updateEmployee(id,employee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void deletEmployee(Long id){
        EmployeApi employeApi = RetrofitEmploye.getClient().create(EmployeApi.class);
        Call<Void> call = employeApi.deleteEmployee(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletEmployee(id);
                dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.create().show();
    }
    private void populateSpinner() {
        ServiceApi serviceApi = RetrofitService.getClient().create(ServiceApi.class);
        Call<List<Service>> call = serviceApi.getAllServices();
        call.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                List<Service> services = response.body();

                if (services != null) {
                    // Create an ArrayAdapter using a simple spinner layout and your list of services
                    ArrayAdapter<Service> adapter = new ArrayAdapter<>(parentFragmentContext, android.R.layout.simple_spinner_item, services);

                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    serviceSpinner.setAdapter(adapter);
                } else {
                    // Handle the case where services is null
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                // Handle failure

            }
        });
    }

}

