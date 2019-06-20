package view;

import controller.Buspark;
import model.Entities.Bus;
import model.Entities.Driver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SigningDriverBusAction implements ActionListener {

    private JTextField driverField, busField;
    private Buspark buspark;

    public SigningDriverBusAction(Buspark buspark, JTextField driverField, JTextField busField) {
        this.driverField = driverField;
        this.busField = busField;
        this.buspark = buspark;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Driver driver = buspark.getDriverById(Long.parseLong(driverField.getText()));
            Bus bus = buspark.getBusById(Long.parseLong(busField.getText()));
            JOptionPane.showMessageDialog(null, buspark.signUnsign(driver, bus));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Wrong id.");
        }
    }
}
