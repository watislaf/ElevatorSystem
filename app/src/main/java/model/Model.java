package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.objects.building.Building;
import model.objects.custumer.Customer;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Model {
    private List<Customer> customers;
    private Building building;

    public void Initialize(Building building) {
        this.building = building;
        this.customers = new LinkedList<>();
    }
}
