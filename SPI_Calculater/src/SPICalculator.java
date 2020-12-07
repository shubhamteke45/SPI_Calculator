import javax.swing.*;
import java.awt.event.*;

import java.sql.*;
import java.awt.Font;
import java.awt.Color;


public class SPICalculator
{
    private JFrame mainWindow;
    private JLabel labelRollNo, labelSubject1, labelSubject2, labelSubject3, labelSubject4, labelSubject5;
    private JTextField textRollNo, textMarks1, textMarks2, textMarks3, textMarks4, textMarks5;
    private JButton buttonCalculate, buttonGet, buttonReset;

    // Database connectivity components
    private Connection connection;
    private Statement statement;
    private ResultSet results;
    private final String username = "root";
    private final String password = "";
    private final String databaseURL = "jdbc:mysql://localhost:3306/calculator";

    private final class CalculateSpiEvent implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            float SPI = getSPI();
            if (SPI == 0.0f)
            {
                JOptionPane.showMessageDialog(mainWindow, "Sorry! Data cannot be inserted");
            }
            else if (textRollNo.getText().equals(""))
            {
                JOptionPane.showMessageDialog(mainWindow, "Roll number required!");
            }
            else
            {
                int rollNo = Integer.parseInt(textRollNo.getText());
                String query = "";

                if (isExist(rollNo))
                {
                    query = "UPDATE grade SET spi = " + SPI + " WHERE rollno = " + rollNo + ";";
                }
                else 
                {
                    query = "INSERT INTO grade(rollno, spi) VALUES(" + rollNo + "," + SPI + ");";
                }
                try
                {
                    statement.executeUpdate(query);
                    JOptionPane.showMessageDialog(mainWindow, "SPI with Roll No: " + rollNo + " added succesfully!");
                } catch(SQLException e)
                {
                   JOptionPane.showMessageDialog(mainWindow, "Cannot insert data now. Please try again later. " + e.toString());
                }
            }
        }
    }
    private final class GetSpiEvent implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            if (textRollNo.getText().equals(""))
            {
                JOptionPane.showMessageDialog(mainWindow, "Please provide a valid roll number!");
            }
            else
            {
                int rollNo = Integer.parseInt(textRollNo.getText());
                String query = "SELECT spi FROM grade WHERE rollno = " + rollNo + ";";
                try
                {
                    results = statement.executeQuery(query);
                    results.next();
                    double SPI = results.getDouble(1);
                    JOptionPane.showMessageDialog(mainWindow, "SPI for roll No." + rollNo + " is " + SPI);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(mainWindow, "SPI for this roll number doesn't exist!");
                }
            }
            
        }
    }

    private final class ResetEvent implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            textRollNo.setText("");
            textMarks1.setText("");
            textMarks2.setText("");
            textMarks3.setText("");
            textMarks4.setText("");
            textMarks5.setText("");
        }
    }

    private boolean isExist(int rollNo)
    {
        String query = "SELECT spi FROM grade WHERE rollno = " + rollNo + ";";
        boolean result = true;
        try
        {
            results = statement.executeQuery(query);
            if (results.next() == false)
            {
                result = false;
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(mainWindow, "Something went wrong: " + e.toString());
        }
        return result;
    }

    private float getSPI()
    {
        float SPI = 0.0f;
        if (textMarks1.getText().equals(""))
        {
            JOptionPane.showMessageDialog(mainWindow, "Please provide marks for SUBJECT 1");
        }
        else if (textMarks2.getText().equals(""))
        {
            JOptionPane.showMessageDialog(mainWindow, "Please provide marks for SUBJECT 2");
        }
        else if (textMarks3.getText().equals(""))
        {
            JOptionPane.showMessageDialog(mainWindow, "Please provide marks for SUBJECT 3");
        }
        else if (textMarks4.getText().equals(""))
        {
            JOptionPane.showMessageDialog(mainWindow, "Please provide marks for SUBJECT 4");
        }
        else if (textMarks5.getText().equals(""))
        {
            JOptionPane.showMessageDialog(mainWindow, "Please provide marks for SUBJECT 5");
        }
        else
        {
            int marks1 = Integer.parseInt(textMarks1.getText());
            int marks2 = Integer.parseInt(textMarks2.getText());
            int marks3 = Integer.parseInt(textMarks3.getText());
            int marks4 = Integer.parseInt(textMarks4.getText());
            int marks5 = Integer.parseInt(textMarks5.getText());

            int[] credits = {3, 2, 3, 4, 3};
            int totalCredits = 15;

            // Calculate numerator first
            int numerator = (marks1 * credits[0]) + (marks2 * credits[1]) + (marks3 * credits[2]) + (marks4 * credits[3]) + (marks5 * credits[4]);
        
            // Calculator SPI
            SPI = numerator / totalCredits; 
        }
        return SPI;
    }

    public SPICalculator()
    {
        // * Initializing database components
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(databaseURL, username, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainWindow, "Something went wrong. Click OK to exit");
            System.exit(0);
        }

        // 1 initializing the mainWindow frame
        mainWindow = new JFrame("SPI Calculator");
        mainWindow.getContentPane().setBackground(new Color(255, 255, 0));
        mainWindow.setBounds(100, 100, 975, 713);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.getContentPane().setLayout(null);

        labelRollNo = new JLabel("Roll No : ");
        labelRollNo.setForeground(new Color(255, 0, 0));
        labelRollNo.setBackground(new Color(255, 255, 0));
        labelRollNo.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelRollNo.setBounds(52, 99, 120, 20);
        mainWindow.getContentPane().add(labelRollNo);

        labelSubject1 = new JLabel("Subject 1: ");
        labelSubject1.setForeground(new Color(255, 0, 0));
        labelSubject1.setBackground(new Color(255, 255, 0));
        labelSubject1.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelSubject1.setBounds(52, 165, 120, 20);
        mainWindow.getContentPane().add(labelSubject1);

        labelSubject2 = new JLabel("Subject 2: ");
        labelSubject2.setForeground(new Color(255, 0, 0));
        labelSubject2.setBackground(new Color(255, 255, 0));
        labelSubject2.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelSubject2.setBounds(52, 234, 120, 20);
        mainWindow.getContentPane().add(labelSubject2);

        labelSubject3 = new JLabel("Subject 3: ");
        labelSubject3.setForeground(new Color(255, 0, 0));
        labelSubject3.setBackground(new Color(255, 255, 0));
        labelSubject3.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelSubject3.setBounds(52, 294, 120, 20);
        mainWindow.getContentPane().add(labelSubject3);

        labelSubject4 = new JLabel("Subject 4: ");
        labelSubject4.setForeground(new Color(255, 0, 0));
        labelSubject4.setBackground(new Color(255, 255, 0));
        labelSubject4.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelSubject4.setBounds(52, 352, 120, 20);
        mainWindow.getContentPane().add(labelSubject4);

        labelSubject5 = new JLabel("Subject 5: ");
        labelSubject5.setForeground(new Color(255, 0, 0));
        labelSubject5.setBackground(new Color(255, 255, 0));
        labelSubject5.setFont(new Font("Tahoma", Font.BOLD, 18));
        labelSubject5.setBounds(52, 419, 120, 20);
        mainWindow.getContentPane().add(labelSubject5);

        // 3 adding all textfields
        textRollNo = new JTextField();
        textRollNo.setFont(new Font("Tahoma", Font.BOLD, 12));
        textRollNo.setBounds(229, 98, 120, 31);
        mainWindow.getContentPane().add(textRollNo);

        textMarks1 = new JTextField();
        textMarks1.setFont(new Font("Tahoma", Font.BOLD, 12));
        textMarks1.setBounds(229, 165, 120, 31);
        mainWindow.getContentPane().add(textMarks1);

        textMarks2 = new JTextField();
        textMarks2.setFont(new Font("Tahoma", Font.BOLD, 12));
        textMarks2.setBounds(229, 234, 120, 31);
        mainWindow.getContentPane().add(textMarks2);

        textMarks3 = new JTextField();
        textMarks3.setFont(new Font("Tahoma", Font.BOLD, 12));
        textMarks3.setBounds(229, 294, 120, 31);
        mainWindow.getContentPane().add(textMarks3);

        textMarks4 = new JTextField();
        textMarks4.setFont(new Font("Tahoma", Font.BOLD, 12));
        textMarks4.setBounds(229, 356, 120, 31);
        mainWindow.getContentPane().add(textMarks4);

        textMarks5 = new JTextField();
        textMarks5.setFont(new Font("Tahoma", Font.BOLD, 12));
        textMarks5.setBounds(229, 423, 120, 31);
        mainWindow.getContentPane().add(textMarks5);


        // 4 Adding buttons
        buttonCalculate = new JButton("Calculate SPI");
        buttonCalculate.setForeground(new Color(255, 255, 0));
        buttonCalculate.setBackground(new Color(255, 0, 0));
        buttonCalculate.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonCalculate.setBounds(45, 508, 141, 40);
        buttonCalculate.addActionListener(new CalculateSpiEvent());
        mainWindow.getContentPane().add(buttonCalculate);

        buttonGet = new JButton("Get SPI");
        buttonGet.setForeground(new Color(255, 255, 0));
        buttonGet.setBackground(new Color(255, 0, 0));
        buttonGet.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonGet.setBounds(299, 508, 100, 40);
        buttonGet.addActionListener(new GetSpiEvent());
        mainWindow.getContentPane().add(buttonGet);

        buttonReset = new JButton("Reset");
        buttonReset.setForeground(new Color(255, 255, 0));
        buttonReset.setBackground(new Color(255, 0, 0));
        buttonReset.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonReset.setBounds(199, 574, 94, 40);
        buttonReset.addActionListener(new ResetEvent());
        mainWindow.getContentPane().add(buttonReset);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setForeground(new Color(0, 0, 0));
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Shubham Teke\\Desktop\\unnamed.png"));
        lblNewLabel.setBounds(418, 37, 520, 532);
        mainWindow.getContentPane().add(lblNewLabel);
    }

    public static void main(String[] args)
    {
        SPICalculator calculator = new SPICalculator();
    }
}
