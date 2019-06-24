package view;

import controller.buspark.Buspark;
import controller.resource_loader.Localization;
import controller.resource_loader.ResourceLoader;
import controller.ui_logic.*;
import model.Administrator;
import model.Driver;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/*An Instance of this class is a main UI window of this software. It contains
 * log in panel, administrator panel, and driver panel*/

public class UserInterface extends JFrame implements LoginAction, LogoutAction, ConfirmRouteAction {

    private static volatile UserInterface instance;
    private Buspark buspark;

    private JPanel logInPanel, adminPanel, driverPanel;
    private JPanel superPanel;
    private JTextField id, password;
    private JButton logIn, logOut;
    private JLabel resultOfLogInOut, routeConfirmedLabel;
    private OnConfirmRouteAction onConfirmRouteAction;
    private OnLogInAction onLogInAction;
    private List<Integer> rangeToSelect;

    private UserInterface(Buspark buspark, String name, int selectionSize) {
        super(name);
        this.buspark = buspark;

        setLayout(new CardLayout());
        setSize(new Dimension(Integer.parseInt(ResourceLoader.getProperyByKey("width").toString()),
                Integer.parseInt(ResourceLoader.getProperyByKey("height").toString())));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rangeToSelect = new ArrayList<>();
        rangeToSelect.add(0);
        rangeToSelect.add(selectionSize);
        rangeToSelect.add(rangeToSelect.get(1));

        logInPanel = new JPanel();
        adminPanel = new JPanel();
        driverPanel = new JPanel();
        adminPanel.add(new JLabel(Localization.getLocalizedValue("administratorPanel")));
        driverPanel.add(new JLabel(Localization.getLocalizedValue("driverPanel")));
        adminPanel.setVisible(false);
        driverPanel.setVisible(false);
        superPanel = new JPanel();
        superPanel.add(logInPanel);
        logInPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / 4));
        superPanel.add(adminPanel);
        superPanel.add(driverPanel);
        add(superPanel);

        //Adding all UI elements of logIn panel below.
        logInPanel.setLayout(new FlowLayout());
        logInPanel.add(new JLabel("    id:  "));
        id = new JTextField(15);
        logInPanel.add(id);
        logInPanel.add(new JLabel(Localization.getLocalizedValue("password")));
        password = new JPasswordField(15);
        ((JPasswordField) password).setEchoChar('*');
        logInPanel.add(password);
        logIn = new JButton(Localization.getLocalizedValue("logIn"));
        logInPanel.add(logIn);
        logOut = new JButton(Localization.getLocalizedValue("logOut"));
        logInPanel.add(logOut);
        resultOfLogInOut = new JLabel(Localization.getLocalizedValue("loggedOut"));
        logInPanel.add(resultOfLogInOut);
        onLogInAction = new OnLogInAction(buspark, this, id, password);
        logIn.addActionListener(onLogInAction);
        logOut.addActionListener(new OnLogOutAction(buspark, this));

        //Adding all UI elements of driver panel below.
        driverPanel.setLayout(new BoxLayout(driverPanel, BoxLayout.PAGE_AXIS));
        JButton confirmRouteButton = new JButton(Localization.getLocalizedValue("confirmRoute"));
        routeConfirmedLabel = new JLabel("    ");
        driverPanel.add(routeConfirmedLabel);
        driverPanel.add(confirmRouteButton);
        onConfirmRouteAction = new OnConfirmRouteAction(buspark, this);
        confirmRouteButton.addActionListener(onConfirmRouteAction);

        //adding all the elements of administrator panel below.
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.PAGE_AXIS));
        JPanel limitSelectContainer = new JPanel();
        final JLabel showRangeToSelect = new JLabel(rangeToSelect.get(0) + " - " +
                (rangeToSelect.get(1) + rangeToSelect.get(0)));
        limitSelectContainer.add(showRangeToSelect);
        JButton moveBack = new JButton("<<<<");
        moveBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rangeToSelect.get(0) - rangeToSelect.get(2) >= 0) {
                    rangeToSelect.set(0, rangeToSelect.get(0) - rangeToSelect.get(2));
                    showRangeToSelect.setText(rangeToSelect.get(0) + " - " +
                            (rangeToSelect.get(1) + rangeToSelect.get(0)));
                }
            }
        });
        JButton moveForward = new JButton(">>>>");
        moveForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rangeToSelect.set(0, rangeToSelect.get(0) + rangeToSelect.get(2));
                showRangeToSelect.setText(rangeToSelect.get(0) + " - " +
                        (rangeToSelect.get(1) + rangeToSelect.get(0)));
            }
        });
        limitSelectContainer.add(moveBack);
        limitSelectContainer.add(moveForward);
        adminPanel.add(limitSelectContainer);
        JButton selectAllDrivers = new JButton(Localization.getLocalizedValue("showAllDrivers"));
        selectAllDrivers.addActionListener(new OnShowAllDrivers(buspark, rangeToSelect));
        adminPanel.add(selectAllDrivers);
        JButton selectAllBuses = new JButton(Localization.getLocalizedValue("showAllBuses"));
        selectAllBuses.addActionListener(new OnShowAllBuses(buspark, rangeToSelect));
        adminPanel.add(selectAllBuses);
        JButton selectAllRoutes = new JButton(Localization.getLocalizedValue("showAllRoutes"));
        selectAllRoutes.addActionListener(new OnShowAllRoutes(buspark, rangeToSelect));
        adminPanel.add(selectAllRoutes);

        JPanel assigningDriverBusContainer = new JPanel(new FlowLayout());
        JTextField busId = new JTextField(10),
                driverId = new JTextField(10);
        assigningDriverBusContainer.add(new JLabel("Driver id:"));
        assigningDriverBusContainer.add(driverId);
        assigningDriverBusContainer.add(new JLabel("Bus id:"));
        assigningDriverBusContainer.add(busId);
        JButton assignDriverOnBus = new JButton(Localization.getLocalizedValue("assignUnassign"));
        assignDriverOnBus.addActionListener(new AssigningDriverBusAction(buspark,
                driverId, busId));
        assigningDriverBusContainer.add(assignDriverOnBus);
        adminPanel.add(assigningDriverBusContainer);

        JPanel assigningBusRouteContainer = new JPanel(new FlowLayout());
        JTextField busId1 = new JTextField(10),
                routeId = new JTextField(10);
        assigningBusRouteContainer.add(new JLabel("Bus id:"));
        assigningBusRouteContainer.add(busId1);
        assigningBusRouteContainer.add(new JLabel("Route id:"));
        assigningBusRouteContainer.add(routeId);
        JButton assignBusRoute = new JButton(Localization.getLocalizedValue("assignUnassign"));
        assignBusRoute.addActionListener(new AssigningBusRouteAction(buspark,
                busId1, routeId));
        assigningBusRouteContainer.add(assignBusRoute);
        adminPanel.add(assigningBusRouteContainer);

        //setVisible should be the last command in constructor.
        setVisible(true);
    }

    public static UserInterface getInstance(Buspark buspark, String name, int selectionSize) {
        if (instance == null)
            synchronized (UserInterface.class) {
                if (instance == null) {
                    instance = new UserInterface(buspark, name, selectionSize);
                }
            }
        return instance;
    }

    //invokes from OnLogInAction class, when "login" button is pressed.
    @Override
    public void updateUIonLogin(User user) {
        resultOfLogInOut.setText(Localization.getLocalizedValue("loggedIn") + user.getName());
        if (user instanceof Driver) {
            driverPanel.setVisible(true);
            adminPanel.setVisible(false);
            onConfirmRouteAction.getDriverBusRoute();
            onConfirmRouteAction.updateRouteStatus();
        } else if (user instanceof Administrator) {
            driverPanel.setVisible(false);
            adminPanel.setVisible(true);
        }
    }

    //invokes from OnLogOutAction class, when "logout" button is pressed.
    @Override
    public void updateUIonLogout() {
        resultOfLogInOut.setText(Localization.getLocalizedValue("loggedOut"));
        driverPanel.setVisible(false);
        adminPanel.setVisible(false);
    }

    //invokes from OnConfirmRoute class, when "confirm route" button is pressed.
    @Override
    public void updateUIonConfirmRoute(String routeStatus) {

        routeConfirmedLabel.setText(routeStatus);
    }
}
