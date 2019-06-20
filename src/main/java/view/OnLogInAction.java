package view;

import controller.Buspark;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            if (user != null)
                loginAction.updateUIonLogin(user);
        } catch (NumberFormatException numEx) {
            JOptionPane.showMessageDialog(null, "Wrong id.");
        }
    }
}
