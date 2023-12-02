package com.mourid.employeeapp.ui.employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mourid.employeeapp.R;
import com.mourid.employeeapp.adapters.EmployeeAdapter;
import com.mourid.employeeapp.adapters.ServiceAdapter;
import com.mourid.employeeapp.api.EmployeApi;
import com.mourid.employeeapp.api.RetrofitEmploye;
import com.mourid.employeeapp.api.RetrofitService;
import com.mourid.employeeapp.api.ServiceApi;
import com.mourid.employeeapp.databinding.FragmentEmployeeBinding;
import com.mourid.employeeapp.databinding.FragmentServiceBinding;
import com.mourid.employeeapp.entities.Employee;
import com.mourid.employeeapp.entities.Service;
import com.mourid.employeeapp.ui.employee.EmployeeViewModel;
import com.mourid.employeeapp.ui.service.AddServiceDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmployeeFragment extends Fragment {

    private FragmentEmployeeBinding binding;
    private Button add;
    private List<Employee> employees = new ArrayList<>();
    private ListView listView;
    private EmployeeAdapter employeeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EmployeeViewModel employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        binding = FragmentEmployeeBinding.inflate(inflater, container,false);
        View root = binding.getRoot();

        listView = binding.listemployes;
        employeeAdapter = new EmployeeAdapter(employees,getContext());
        getEmployes();
        add = binding.add2;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEmployeeDialogFragment dialogFragment = new AddEmployeeDialogFragment(getContext());
                dialogFragment.show(requireActivity().getSupportFragmentManager(), "AddEmployeeDialog");
            }
        });
        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Trigger refresh when the user swipes
                getEmployes();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.idemploye);
                UpdateEmployeDialogFragment dialogFragment = new UpdateEmployeDialogFragment(Long.parseLong(id.getText().toString()),getContext());
                dialogFragment.show(requireActivity().getSupportFragmentManager(), "UpdateEmployeDialog");
            }
        });

        return root;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getEmployes(){
      EmployeApi employeApi = RetrofitEmploye.getClient().create(EmployeApi.class);
        Call<List<Employee>> call =employeApi.getAll();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                List<Employee> employees = response.body();
                // Update your adapter with the list of employees
                employeeAdapter.updateEmployeeList(employees);
                listView.setAdapter(employeeAdapter);
                employeeAdapter.notifyDataSetChanged();
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
           @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });
    }
}