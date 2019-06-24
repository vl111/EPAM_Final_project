package view;

import controller.resource_loader.ResourceLoader;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/*Each instance of this class is a window for displaying information
 about a selection of drivers, buses or routes.
 The window can be called by any logged in administrator by pressing "Show all" buttons
 on the UI panel.
* */

public class AllRowsSelectWindow extends JFrame {
    private static final Logger LOG = Logger.getLogger(AllRowsSelectWindow.class.getSimpleName());

    private JTable jTable;

    public AllRowsSelectWindow(String[][] rows, String[] columns, String name) {
        super(name);
        setLayout(new FlowLayout());
        setSize(new Dimension(Integer.parseInt(ResourceLoader.getProperyByKey("width").toString()),
                Integer.parseInt(ResourceLoader.getProperyByKey("height").toString())));
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        jTable = new JTable(rows, columns);
        add(new JScrollPane(jTable));

        setVisible(true);
        LOG.info("Created new DB selection window.");
    }


}

