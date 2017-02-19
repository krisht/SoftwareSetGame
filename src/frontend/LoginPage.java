import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by abhinav on 1/28/2017.
 */
public class LoginPage extends JFrame implements ActionListener, ItemListener {

    // Create the basic instance variables for Submit button, username and password labels,
    // and username and password text fields
    JButton SUBMIT,REGISTER;
    JToggleButton TOGGLE;
    JLabel usernameLabel, passwordLabel, repeatPasswordLabel;
    final JTextField usernameField, passwordField, repeatPasswordField;

    final int USER_NOT_EXIST = 1;
    final int PWD_INCORRECT = 2;
    final int USER_ALREADY_EXIST = 3;
    final int LOGIN_SUCCESS = 4;
    final int REGISTER_SUCCESS = 5;
    // Constructor that initialises and sets the labels to "username" and "password", initialises the fields, and
    // prepares the submit button. Then, creates the layout and sets the label.
    public LoginPage() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);

        // make the title 
        
        // Initialize and set instance variables
        // Initialize visibilities of variables
        usernameLabel = new JLabel();
        usernameLabel.setText("Username: ");
        passwordLabel = new JLabel();
        passwordLabel.setText("Password: ");
        repeatPasswordLabel = new JLabel();
        repeatPasswordLabel.setText("Repeat Password: ");
        repeatPasswordLabel.setVisible(false);
        usernameField = new JTextField(15);
        passwordField = new JTextField(15);
        repeatPasswordField = new JTextField(15);
        repeatPasswordField.setVisible(false);
        
        // create login, register and toggle button
        SUBMIT = new JButton("SUBMIT");
        REGISTER = new JButton("REGISTER");
        REGISTER.setVisible(false);

        TOGGLE = new JToggleButton("TOGGLE TO LOGIN / REGISTER");
        TOGGLE.addItemListener(this);

        // Retrieve the top-level content-plane
        Container cp = this.getContentPane();
        // Set layout
        cp.setLayout(new FlowLayout());
        // Add the components
        cp.add(usernameLabel);
        cp.add(usernameField);
        cp.add(passwordLabel);
        cp.add(passwordField);
        cp.add(repeatPasswordLabel);
        cp.add(repeatPasswordField);
        cp.add(SUBMIT);
        cp.add(REGISTER);
        cp.add(TOGGLE);
        
        // Prepare the action for the submit button
        SUBMIT.addActionListener(this);
        REGISTER.addActionListener(this);
        // Set the title of the login window
        setTitle("Login for Game");
    }

    // Checks username and password, and logs in if both present, throws error else.
    public void actionPerformed(ActionEvent ae) {

    	// Fetch the username and password from the textboxes
        String value1 = usernameField.getText();
        String value2 = passwordField.getText();
        String value3 = repeatPasswordField.getText();
        // Check which button is pressed
    	JButton b = (JButton)ae.getSource();
        if (b.equals(REGISTER)){
        	// check that passwords are equal
        	if ((value1 != null && !value1.isEmpty()) && (value2 != null && !value2.isEmpty()) &&
        			value3 != null && !value3.isEmpty()){
        		if (!value2.equals(value3)){
            		System.out.println("Passwords are not the same");
                    JOptionPane.showMessageDialog(this, "Passwords are not the same", "Error", JOptionPane.ERROR_MESSAGE);
        		}else{
        			// check availability
        			switch(Register(value1, value2)){
        				case REGISTER_SUCCESS:
        					System.out.println("Registration successful!");
            				JOptionPane.showMessageDialog(this, "Registration Successful", "Success",JOptionPane.PLAIN_MESSAGE);
            				enterLanding(value1);
            				break;
        				case USER_ALREADY_EXIST:
        					System.out.println("User already exist!");
                            JOptionPane.showMessageDialog(this, "User already exist in database", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
        				default : break;
        					
        			}
        		}
        	}else{
        		System.out.println("username or password not present");
                JOptionPane.showMessageDialog(this, "Username or password not present", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }else if (b.equals(SUBMIT)){
        	// Check that both fields are present
            
            if ((value1 != null && !value1.isEmpty()) && (value2 != null && !value2.isEmpty())) {
            	enterLanding(value1);
            } else {    // Else, throw an error in an error box
                System.out.println("username or password not present");
                JOptionPane.showMessageDialog(this, "Username or password not present", "Error", JOptionPane.ERROR_MESSAGE);
            }	
        }
    }
    
    public void itemStateChanged(ItemEvent e){
    	if (e.getStateChange() == ItemEvent.SELECTED){
    		setTitle("Register for Game");
    		REGISTER.setVisible(true);
    		SUBMIT.setVisible(false);
    		repeatPasswordField.setVisible(true);
    		repeatPasswordLabel.setVisible(true);
    	}else{
    		setTitle("Login for Game");
    		REGISTER.setVisible(false);
    		SUBMIT.setVisible(true);
    		repeatPasswordField.setVisible(false);
    		repeatPasswordLabel.setVisible(false);
    	}
    }
    
    public void enterLanding(String value1){
    	System.out.println("logged in");

        // Create a landing page
        LandingPage page = new LandingPage();

        // NOTE: The proper way as implemented in the landing page closes the landing page too, so use this way
        setVisible(false);
        dispose();

        // Make page visible
        page.setVisible(true);
        // Set title
        page.setTitle("Welcome " + value1);

        // Set uername in login window
        page.userMessage.setText("User " + value1 + " logged in.");
    	
    }
    
    public int Register(String uname, String pwd){
    	return REGISTER_SUCCESS;
    }
}