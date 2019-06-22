package controller.ui_logic;

import controller.buspark.Buspark;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*This is an action listener for the "logout" button in main UI.*/

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
