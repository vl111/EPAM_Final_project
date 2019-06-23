package controller.ui_logic;

import controller.buspark.Buspark;
import model.Bus;
import model.Route;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*This is an action listener for the "assign/unassign bus to driver" button in main UI.*/

public class AssigningBusRouteAction implements ActionListener {

    private JTextField busField, routeField;
    private Buspark buspark;

    public AssigningBusRouteAction(Buspark buspark, JTextField busField, JTextField routeField) {
        this.busField = busField;
        this.routeField = routeField;
        this.buspark = buspark;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Bus bus = buspark.getBusById(Long.parseLong(busField.getText()));
            Route route = buspark.getRouteById(Long.parseLong(routeField.getText()));
            JOptionPane.showMessageDialog(null, buspark.assignUnassign(bus, route));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Wrong id.");
        }
    }
}
