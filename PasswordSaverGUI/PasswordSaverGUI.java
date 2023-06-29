import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PasswordSaverGUI extends JFrame implements ActionListener {
    private JTextField websiteTextField, usernameTextField, passwordTextField;
    private JButton saveButton, updateButton, deleteButton;
    private JTextArea passwordListTextArea;

    public PasswordSaverGUI() {
        setTitle("Password Saver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel websiteLabel = new JLabel("Website:");
        websiteLabel.setForeground(Color.WHITE);
        websiteTextField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameTextField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordTextField = new JTextField();

        inputPanel.setBackground(Color.BLACK);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(websiteLabel);
        inputPanel.add(websiteTextField);
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameTextField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordTextField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        saveButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        passwordListTextArea = new JTextArea();
        passwordListTextArea.setEditable(false);
        passwordListTextArea.setBackground(new Color(99, 69, 51));
        passwordListTextArea.setForeground(Color.WHITE);
        passwordListTextArea.setPreferredSize(new Dimension(100, 100));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(99, 69, 51));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(new JScrollPane(passwordListTextArea), BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(600, 300));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        loadPasswords();

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            savePassword();
        } else if (e.getSource() == updateButton) {
            updatePassword();
        } else if (e.getSource() == deleteButton) {
            deletePassword();
        }
    }

    private void loadPasswords() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("notes.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                passwordListTextArea.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePassword() {
        String website = websiteTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("notes.txt", true));
            writer.write("Website: " + website + ", Username: " + username + ", Password: " + password);
            writer.newLine();
            writer.close();

            passwordListTextArea.append("Website: " + website + ", Username: " + username + ", Password: " + password + "\n");

            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePassword() {
        String website = websiteTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        try {
            File inputFile = new File("notes.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Website: " + website)) {
                    writer.write("Website: " + website + ", Username: " + username + ", Password: " + password);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
                clearFields();
                passwordListTextArea.setText("");
                loadPasswords();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update password!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePassword() {
        String website = websiteTextField.getText();

        try {
            File inputFile = new File("notes.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean deleted = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Website: " + website)) {
                    deleted = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Password deleted successfully!");
                    clearFields();
                    passwordListTextArea.setText("");
                    loadPasswords();
                } else {
                    JOptionPane.showMessageDialog(this, "Website not found!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete password!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        websiteTextField.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PasswordSaverGUI();
        });
    }
}
