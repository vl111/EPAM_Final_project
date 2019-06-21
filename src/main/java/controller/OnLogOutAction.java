package controller;

import controller.buspark.Buspark;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnLogOutAction implements ActionListener {

    private Buspark buspark;
    private LogoutAction logoutAction;

    public OnLogOutAction(Buspark buspark, LogoutAction onLogin) {
        this.buspark = buspark;
        this.logoutAction = onLogin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logoutAction.updateUIonLogout();
        buspark.logOut();
    }
}
