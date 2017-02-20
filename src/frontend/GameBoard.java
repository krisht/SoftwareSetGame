import java.awt.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.*;

public class GameBoard extends JFrame{
	
	JPanel header, gameboard, chatbox, gamestats;
	GridBagConstraints c_header, c_gameboard, c_chatbox, c_gamestats, c;
	JButton button;
	Border blackline;
	public GameBoard(){
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(1280, 960);
	     Container cp = this.getContentPane();
	     cp.setLayout(new GridBagLayout());

		 blackline = BorderFactory.createLineBorder(Color.black);
	     makeHeaderPanel(cp);
	     makeGameBoardPanel(cp);
	     makeChatBox(cp);
	     c = new GridBagConstraints();
	     button = new JButton("Button 4");
	     c.fill = GridBagConstraints.BOTH;
	     c.weightx = 0.3;
	     c.weighty = 0.45;
	     c.gridx = 1;
	     c.gridy = 2;
	     c.gridwidth = 1;
	     c.gridheight = 1;
	     cp.add(button, c);
	}
	// Make header here
	public void makeHeaderPanel(Container cp){
		c_header = new GridBagConstraints();
	    header = new JPanel();
	    c_header.weightx = 1;
	    c_header.weighty = 0.1;
	    c_header.fill = GridBagConstraints.BOTH;
	    c_header.gridx = 0;
	    c_header.gridy = 0;
	    c_header.gridwidth = 2;
	    c_header.gridheight = 1;
	    header.setBorder(blackline);
	    cp.add(header, c_header);
	}
	// Make Gameboard here
	public void makeGameBoardPanel(Container cp){
		c_gameboard = new GridBagConstraints();
	    gameboard = new JPanel();
	    c_gameboard.fill = GridBagConstraints.BOTH;
	    c_gameboard.weightx = 0.7;
	    c_gameboard.weighty = 0.9;
	    c_gameboard.gridx = 0;
	    c_gameboard.gridy = 1;
	    c_gameboard.gridwidth = 1;
	    c_gameboard.gridheight = 2;
	    gameboard.setBorder(blackline);
	    cp.add(gameboard, c_gameboard);
	    
	    // make 21 slots
	     
	}
	// Make chatbox here
	public void makeChatBox(Container cp){
	     c_chatbox = new GridBagConstraints();
	     chatbox = new JPanel();
	     c_chatbox.fill = GridBagConstraints.BOTH;
	     c_chatbox.weightx = 0.3;
	     c_chatbox.weighty = 0.45;
	     c_chatbox.gridx = 1;
	     c_chatbox.gridy = 1;
	     c_chatbox.gridwidth = 1;
	     c_chatbox.gridheight = 1;
	     chatbox.setBorder(blackline);
	     cp.add(chatbox, c_chatbox);
		
	}
	// Make game statistics chart here
	
	
}
