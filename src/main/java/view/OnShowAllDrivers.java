package view;

import controller.Buspark;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OnShowAllDrivers implements ActionListener {

    private final String[] columns = {"Driver id", "Driver name", "Bus id", "Bus name",
            "Route id", "Route name", "Route confirmed"};
    private Buspark buspark;
    private List<Integer> rangeToSelect;

    public OnShowAllDrivers(Buspark buspark, List<Integer> rangeToSelect) {
        this.buspark = buspark;
        this.rangeToSelect = rangeToSelect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new AllRowsSelectWindow(buspark.getAllDriversInfo(rangeToSelect.get(0), rangeToSelect.get(1)),
                columns, "All Drivers");
    }
}
