package com.mourid.employeeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mourid.employeeapp.R;
import com.mourid.employeeapp.entities.Employee;


import java.util.List;

public class EmployeeAdapter extends BaseAdapter {
    private List<Employee> employees;
    private LayoutInflater inflater;
    Context context;

    public EmployeeAdapter(List<Employee> employees, Context context) {
        this.employees = employees;
        this.inflater = LayoutInflater.from(context);
    }


    public void updateEmployeeList(List<Employee> newEmployees) {
        employees.clear();
        employees.addAll(newEmployees);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int position) {
        return employees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return employees.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.employee_item, null);
        TextView name= convertView.findViewById(R.id.nom);
         TextView prenom=convertView.findViewById(R.id.prenom);
        TextView id = convertView.findViewById(R.id.idemploye);
        TextView service=convertView.findViewById(R.id.service);
        TextView dateDeNaissance=convertView.findViewById(R.id.dateDeNaissance);
        name.setText(employees.get(position).getNom());
        prenom.setText(employees.get(position).getPrenom());
        id.setText(employees.get(position).getId().toString());
        service.setText(employees.get(position).getService().getNom());
        dateDeNaissance.setText(employees.get(position).getDateNaissance().toString());

        return convertView;
    }
}
