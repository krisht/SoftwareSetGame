package frontend;

import backend.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.border.Border;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameBoard extends JFrame implements ItemListener{
	
	JPanel header, gameboard, chatbox, gamestats;
	GridLayout board;
	GridBagConstraints c_header, c_gameboard, c_chatbox, c_gamestats;
	JButton NO_MORE_CARDS, EXIT;
	Border blackline;
	File folder = new File("./Images");
	File[] listOfFiles = folder.listFiles();
	
	public GameBoard(){
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(1280, 960);
	     Container cp = this.getContentPane();
	     cp.setLayout(new GridBagLayout());

		 blackline = BorderFactory.createLineBorder(Color.black);
	     makeHeaderPanel(cp);
	     makeGameBoardPanel(cp);
	     makeChatBox(cp);
	     makeGameStats(cp);
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
	    
	    NO_MORE_CARDS = new JButton("No More Cards");
	    header.add(NO_MORE_CARDS);
	    EXIT = new JButton("Exit");
	    header.add(EXIT);
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
	    gameboard.setLayout(new GridLayout(7,3));
	    // THIS IS WHERE WE CALL BACKEND FUNCTION TO POPULATE THE BOARD
	    for (int i = 0; i < 12; i++){
	    	addCard(i, gameboard);
	    }
	}
	
	public void addCard(int cardId, JPanel board){
		JToggleButton TOGGLE = new JToggleButton();
        TOGGLE.addItemListener(this);
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("./Images/"+listOfFiles[cardId].getName()));
		    // create card class here
		} catch (IOException e) {
			
		}
		ImageIcon imageIcon = new ImageIcon(img);
        TOGGLE.setIcon(imageIcon);
		board.add(TOGGLE);
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
	public void makeGameStats(Container cp){

	     c_gamestats = new GridBagConstraints();
	     gamestats = new JPanel();
	     c_gamestats.fill = GridBagConstraints.BOTH;
	     c_gamestats.weightx = 0.3;
	     c_gamestats.weighty = 0.45;
	     c_gamestats.gridx = 1;
	     c_gamestats.gridy = 2;
	     c_gamestats.gridwidth = 1;
	     c_gamestats.gridheight = 1;
		 gamestats.setBorder(blackline);
	     cp.add(gamestats, c_gamestats);
	}
	
	public BufferedImage createImage(JPanel panel) {

	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    return bi;
	}
	public void itemStateChanged(ItemEvent e){

        JToggleButton JTB = (JToggleButton)e.getSource();
        if (e.getStateChange() == ItemEvent.SELECTED){
        	JTB.setSelected(true);
        	// obtain informationa about the card
        }else{
           JTB.setSelected(false);
        }
    }
}