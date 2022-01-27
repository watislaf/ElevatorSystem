package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.objects.building.Building;
import model.objects.custumer.Customer;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Model {
    private List<Customer> customers;
    private Building building;

    public void Initialize(Building building) {
        this.building = building;
        this.customers = new LinkedList<>();
    }
}
