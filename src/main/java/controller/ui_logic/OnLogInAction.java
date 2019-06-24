package controller.ui_logic;

import controller.buspark.Buspark;
import controller.resource_loader.Localization;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*This is an action listener for the "login" button in main UI.*/

public class OnLogInAction implements ActionListener {

    private JTextField id, password;
    private Buspark busPark;
    private LoginAction loginAction;

    public OnLogInAction(Buspark busPark, LoginAction loginAction, JTextField id, JTextField password) {
        this.id = id;
        this.password = password;
        this.busPark = busPark;
        this.loginAction = loginAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            long id = Long.parseLong(this.id.getText());
            User user = busPark.logIn(id, password.getText());
            if (user == null)
                throw new NullPointerException();
            loginAction.updateUIonLogin(user);
        } catch (NumberFormatException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, Localization.getLocalizedValue("wrongId"));
        }
    }
}
