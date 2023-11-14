import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowStudents implements ActionListener {

    GridBagConstraints gbc = new GridBagConstraints();
    Insets defaultInsets = new Insets(5, 5, 5, 5);

    JLabel jl = new JLabel("To show all the students click the button under ..");
    JButton btn = new JButton("Show All");

    JPanel panel;

    ShowStudents(JPanel panel) {
        this.panel = panel;
        panel.setLayout(new GridBagLayout());

        gbc.gridy = 0;
        gbc.insets = defaultInsets;
        gbc.gridwidth = 2;
        panel.add(jl, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(btn, gbc);

        btn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student", "root", "walid"
        )) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM student")) {
                try (ResultSet rs = ps.executeQuery()) {

                    String[] columnNames = {"id", "Nom", "Prenom", "Note"};

                    // Create a DefaultTableModel with specified columns
                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

                    // Create a JTable with the DefaultTableModel
                    JTable students = new JTable(tableModel);

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nom = rs.getString("nom");
                        String prenom = rs.getString("prenom");
                        float note = rs.getFloat("note");

                        // Add data to the DefaultTableModel
                        Object[] rowData = {id, nom, prenom, note};
                        tableModel.addRow(rowData);
                    }
                    //Remove the existing components
                    panel.removeAll();

                    // Revalidate and repaint the panel to reflect the changes
                    panel.revalidate();
                    panel.repaint();

                    // Add the JTable to the panel
                    JScrollPane scrollPane = new JScrollPane(students);
                    gbc.gridy = 0;  // Adjust the grid position based on your layout
                    panel.add(scrollPane, gbc);

                    // Revalidate and repaint the panel to reflect the changes
                    panel.revalidate();
                    panel.repaint();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}