package model;

import model.objects.Building;
import model.objects.Customer;

import java.util.LinkedList;
import java.util.List;

public class Model {
    List<Customer> customers;
    Building building;

    public void Initialize(Building building) {
        this.building = (Building) building.cloneObj();
        this.customers = new LinkedList<>();
    }
}
