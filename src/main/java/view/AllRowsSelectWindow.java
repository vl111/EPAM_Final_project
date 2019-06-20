package view;

import javax.swing.*;
import java.awt.*;

public class AllRowsSelectWindow extends JFrame {

    private JTable jTable;

    public AllRowsSelectWindow(String[][] rows, String[] columns, String name) {
        super(name);
        setLayout(new FlowLayout());
        setSize(new Dimension(Integer.parseInt(controller.ResourceLoader.getProperties().get("width").toString()),
                Integer.parseInt(controller.ResourceLoader.getProperties().get("width").toString())));
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        jTable = new JTable(rows, columns);
        add(new JScrollPane(jTable));

        setVisible(true);
    }


}

