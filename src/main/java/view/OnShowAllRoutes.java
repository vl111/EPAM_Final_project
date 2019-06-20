package view;

import controller.Buspark;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OnShowAllRoutes implements ActionListener {

    private final String[] columns = {"Route id", "Route name"};
    private Buspark buspark;
    private List<Integer> rangeToSelect;

    public OnShowAllRoutes(Buspark buspark, List<Integer> rangeToSelect) {
        this.buspark = buspark;
        this.rangeToSelect = rangeToSelect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new AllRowsSelectWindow(buspark.getAllRoutesInfo(rangeToSelect.get(0), rangeToSelect.get(1)),
                columns, "All Routes");
    }
}
