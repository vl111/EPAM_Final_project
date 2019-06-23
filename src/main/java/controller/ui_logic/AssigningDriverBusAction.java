package controller.ui_logic;

import controller.buspark.Buspark;
import controller.resource_loader.Localization;
import model.Bus;
import model.Driver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*This is an action listener for the "assign/unassign bus to route" button in main UI.*/

public class AssigningDriverBusAction implements ActionListener {

    private JTextField driverField, busField;
    private Buspark buspark;

    public AssigningDriverBusAction(Buspark buspark, JTextField driverField, JTextField busField) {
        this.driverField = driverField;
        this.busField = busField;
        this.buspark = buspark;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Driver driver = buspark.getDriverById(Long.parseLong(driverField.getText()));
            Bus bus = buspark.getBusById(Long.parseLong(busField.getText()));
            JOptionPane.showMessageDialog(null, buspark.assignUnassign(driver, bus));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, Localization.getLocalizedValue("wrongId"));
        }
    }
}
