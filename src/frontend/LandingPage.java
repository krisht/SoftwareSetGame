package frontend;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import static frontend.ClientConnThreaded.listofGames;
import static frontend.LoginPage.*;

/*
 * Landing page with "Welcome User" title
 */

public class LandingPage extends JFrame implements ActionListener {

    static String gameName;
    private JScrollPane serverlistpane;
    static JTextPane chatlogarea;
    static JTextField chatinputfield;
    static int gid;
    static GameBoard_Front gb;
    static int lifetime_score;
    private JPanel header, serverbrowser, chatbox, userbox;
    private GridBagConstraints c_gamelistLabel, c_refreshbutton, c_helpbutton, c_joingamebutton, c_creategamebutton;
    private GridBagConstraints c_userbox, c_welcomeLabel, c_scoreLabel, c_scorecapLabel, c_logout;
    private GridBagConstraints  c_chatLabel, c_chatbox, c_chatlogpane, c_chatinputfield;
    private GridBagConstraints c_header, c_serverbrowser, c_serverlistpane;
    private JButton LOGOUT, JOINGAME, CREATEGAME, HELP, REFRESH;
    private JLabel userMessage, titleLabel, creatorLabel, chatLabel;
    private JLabel gamelistLabel;
    private JPanel list_of_games_panel;
    private ArrayList<JButton> list_of_games_buttons;
    private JLabel welcomeLabel,scoreLabel, scorecapLabel;
    private JScrollPane chatlogpane;
    private Font f, bfont;
    private ActionListener listener;
    private HashMap<Integer,Integer> location_to_gid;
    private WindowListener exitListener;
    static Style uname_overall_style, msg_overall_style, system_style, myuname_overall_style, mymsg_overall_style, game_system_style;

    LandingPage() {

        try {
            BufferedImage img = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("frontend/images/SET.png"));

            this.setIconImage(img);
        } catch (IOException ex){

        }

        // blah 2
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1280, 960);
        newConnectionThread.start();
        list_of_games_buttons = new ArrayList<JButton>();
        gid = -1;
        location_to_gid = new HashMap<Integer, Integer>();


        exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Are You Sure to exit game?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    log_out();
                }else{
                    return;
                }
            }
        };
        this.addWindowListener(exitListener);

        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0 ; i < listofGames.size(); i++){
                	if (e.getSource() == list_of_games_buttons.get(i)){
                		if (location_to_gid.get(i) == gid){
                			gid = -1;
                    		list_of_games_buttons.get(i).setBackground(Color.decode("#FFFFFF"));
                		}else{
                			gid = location_to_gid.get(i);
                			list_of_games_buttons.get(i).setBackground(Color.decode("#BDBDBD"));
                		}
                	}else{
                		list_of_games_buttons.get(i).setBackground(Color.decode("#FFFFFF"));
                	}
                }
                System.out.println("Selected game "+ gid);
                // deselect the other game
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (gid >= 0){
                    gb.returnToLanding();
                }
                log_out();
                /*try {
                    Thread.sleep(200);
                    System.out.println("Shouting down ...");
                    //some cleaning up code...

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
            }
        });



        Container cp = this.getContentPane();
        cp.setLayout(new GridBagLayout());

		f = new Font("Arial", Font.BOLD, 12);
		bfont = new Font("Arial",Font.PLAIN, 18);
        
        makeHeaderPanel(cp);
        makeServerBrowser(cp);
        makeUserBox(cp);
        makeChatBox(cp);
        getUserScore();
        requestupdateServerList();

        system_style = this.chatlogarea.addStyle("System", null);
        StyleConstants.setForeground(system_style, Color.red);
        StyleConstants.setItalic(system_style, true);

        game_system_style = this.chatlogarea.addStyle("GameSystem", null);
        StyleConstants.setForeground(game_system_style, Color.red);
        StyleConstants.setBold(game_system_style, true);
        StyleConstants.setItalic(game_system_style, true);

        msg_overall_style = this.chatlogarea.addStyle("Msg", null);

        StyleConstants.setForeground(msg_overall_style, Color.black);
        StyleConstants.setItalic(msg_overall_style, false);
        StyleConstants.setBold(msg_overall_style, false);

        uname_overall_style = this.chatlogarea.addStyle("Username", null);
        StyleConstants.setForeground(uname_overall_style, Color.black);
        StyleConstants.setItalic(uname_overall_style, false);
        StyleConstants.setBold(uname_overall_style, true);

        mymsg_overall_style = this.chatlogarea.addStyle("Mymsg", null);
        StyleConstants.setForeground(mymsg_overall_style, Color.blue);
        StyleConstants.setItalic(mymsg_overall_style, false);
        StyleConstants.setBold(mymsg_overall_style, false);

        myuname_overall_style = this.chatlogarea.addStyle("Myusername", null);
        StyleConstants.setForeground(myuname_overall_style, Color.blue);
        StyleConstants.setItalic(myuname_overall_style, false);
        StyleConstants.setBold(myuname_overall_style, true);
    }


    private void makeHeaderPanel(Container cp) {

        header = new JPanel(new GridBagLayout());
        c_header = new GridBagConstraints();
        c_header.fill = GridBagConstraints.HORIZONTAL;
        header.setMinimumSize(new Dimension(1280,100));
        header.setPreferredSize(new Dimension(1280,100));
        //c_header.weightx = 1;
        //c_header.weighty = 0.1;
        c_header.gridx = 0;
        c_header.gridy = 0;
        c_header.ipadx = 0;
        c_header.ipady = 0;
        c_header.gridwidth = 2;
        c_header.gridheight = 1;
        header.setBackground(Color.decode("#009688"));
        cp.add(header, c_header);

        titleLabel = new JLabel("SET");
        Font f_big = new Font("Arial",Font.BOLD, 60);
        titleLabel.setFont(f_big);
        titleLabel.setForeground(Color.WHITE);
        GridBagConstraints c_title = new GridBagConstraints();
        c_title.anchor = GridBagConstraints.LINE_START;
        c_title.fill = GridBagConstraints.NONE;
        c_title.gridwidth = 1;
        c_title.gridheight = 1;
        c_title.weightx = 1;
        c_title.weighty = 1;
        c_title.gridx = 0;
        c_title.gridy = 0;
        c_title.ipadx = 0;
        c_title.ipady = 0;
        c_title.insets = new Insets(0,24,0,0);
        header.add(titleLabel, c_title);


        creatorLabel = new JLabel("by rosskaplan, krisht, abhinavj30, brendabrandy");
        Font f2 = new Font("Arial",Font.PLAIN, 13);
        creatorLabel.setFont(f2);
        creatorLabel.setForeground(Color.WHITE);
        GridBagConstraints c_creator = new GridBagConstraints();
        c_creator.anchor = GridBagConstraints.LAST_LINE_END;
        c_creator.fill = GridBagConstraints.NONE;
        c_creator.gridwidth = 1;
        c_creator.gridheight = 1;
        c_creator.weightx = 1;
        c_creator.weighty = 1;
        c_creator.gridx = 1;
        c_creator.gridy = 0;
        c_creator.ipadx = 0;
        c_creator.ipady = 0;
        c_creator.insets = new Insets(0,0,10,24);
        header.add(creatorLabel, c_creator);

    }

    private void makeServerBrowser(Container cp) {

        c_serverbrowser = new GridBagConstraints();
        serverbrowser = new JPanel(new GridBagLayout());
        c_serverbrowser.fill = GridBagConstraints.BOTH;
        c_serverbrowser.weightx = 0.7;
        c_serverbrowser.weighty = 0.9;
        c_serverbrowser.gridx = 0;
        c_serverbrowser.gridy = 1;
        c_serverbrowser.gridwidth = 1;
        c_serverbrowser.gridheight = 2;
        c_serverbrowser.insets = new Insets(24,24,24,12);
        serverbrowser.setBackground(Color.decode("#FFFFFF"));
        serverbrowser.setMinimumSize(new Dimension(650,800));
        serverbrowser.setPreferredSize(new Dimension(650,800));
        serverbrowser.setMaximumSize(new Dimension(650, 800));

        cp.add(serverbrowser, c_serverbrowser);

        gamelistLabel = new JLabel("Active Games");
        c_gamelistLabel = new GridBagConstraints();
        gamelistLabel.setFont(f);
        gamelistLabel.setForeground(Color.decode("#616161"));
        c_gamelistLabel.fill = GridBagConstraints.BOTH;
        c_gamelistLabel.weightx = 1.0;
        c_gamelistLabel.weighty = 0.05;
		c_gamelistLabel.gridx = 0;
		c_gamelistLabel.gridy = 0;
        c_gamelistLabel.gridwidth = 4;
        c_gamelistLabel.gridheight = 1;
        c_gamelistLabel.insets = new Insets(8,16,0,0);
        serverbrowser.add(gamelistLabel, c_gamelistLabel);
        
        list_of_games_panel = new JPanel(new GridBagLayout());
        list_of_games_panel.setBackground(Color.decode("#CFD8DC"));
		serverlistpane = new JScrollPane(list_of_games_panel);
		serverlistpane.setBorder(null);
		serverlistpane.setBackground(Color.decode("#CFD8DC"));
        serverlistpane.setMinimumSize(new Dimension(500, 700));
        serverlistpane.setMaximumSize(new Dimension(500, 700));
        serverlistpane.setPreferredSize(new Dimension(500, 700));
		c_serverlistpane = new GridBagConstraints();
		c_serverlistpane.fill = GridBagConstraints.BOTH;
		c_serverlistpane.weightx = 1.0;
		c_serverlistpane.weighty = 0.9;
		c_serverlistpane.gridx = 0;
		c_serverlistpane.gridy = 1;
        c_serverlistpane.gridwidth = 4;
        c_serverlistpane.gridheight =  1;
        c_serverlistpane.insets = new Insets(16,16,16,16);
        serverbrowser.add(serverlistpane, c_serverlistpane);
        
        JOINGAME = new JButton("JOIN GAME");
		JOINGAME.addActionListener(this);
        JOINGAME.setOpaque(true);
        JOINGAME.setBorderPainted(false);
        JOINGAME.setForeground(Color.white);
		JOINGAME.setFocusPainted(false);
		JOINGAME.setBackground(Color.decode("#FF4081"));
        JOINGAME.setMinimumSize(new Dimension(175,40));
        JOINGAME.setPreferredSize(new Dimension(175,40));
		JOINGAME.setFont(bfont);
		c_joingamebutton = new GridBagConstraints();
		c_joingamebutton.fill = GridBagConstraints.NONE;
		c_joingamebutton.weightx = 0.25;
		c_joingamebutton.weighty = 0.05;
		c_joingamebutton.gridx = 0;
		c_joingamebutton.gridy = 2;
        c_joingamebutton.gridwidth = 1;
        c_joingamebutton.gridheight = 1;
        serverbrowser.add(JOINGAME, c_joingamebutton);
        
        CREATEGAME = new JButton("CREATE GAME");
		CREATEGAME.addActionListener(this);
        CREATEGAME.setOpaque(true);
        CREATEGAME.setBorderPainted(false);
        CREATEGAME.setForeground(Color.white);
		CREATEGAME.setFocusPainted(false);
		CREATEGAME.setBackground(Color.decode("#FF4081"));
        CREATEGAME.setMinimumSize(new Dimension(175,40));
        CREATEGAME.setPreferredSize(new Dimension(175,40));
		CREATEGAME.setFont(bfont);
		c_creategamebutton = new GridBagConstraints();
		c_creategamebutton.fill = GridBagConstraints.NONE;
		c_creategamebutton.weightx = 0.25;
		c_creategamebutton.weighty = 0.05;
		c_creategamebutton.gridx = 1;
		c_creategamebutton.gridy = 2;
        c_creategamebutton.gridwidth = 1;
        c_creategamebutton.gridheight = 1;
        serverbrowser.add(CREATEGAME, c_creategamebutton);
        
        REFRESH = new JButton("REFRESH");
		REFRESH.addActionListener(this);
        REFRESH.setOpaque(true);
        REFRESH.setBorderPainted(false);
        REFRESH.setForeground(Color.white);
		REFRESH.setFocusPainted(false);
		REFRESH.setBackground(Color.decode("#2F5398"));
        REFRESH.setMinimumSize(new Dimension(175,40));
        REFRESH.setPreferredSize(new Dimension(175,40));
		REFRESH.setFont(bfont);
		c_refreshbutton = new GridBagConstraints();
		c_refreshbutton.fill = GridBagConstraints.NONE;
		c_refreshbutton.weightx = 0.25;
		c_refreshbutton.weighty = 0.05;
		c_refreshbutton.gridx = 2;
		c_refreshbutton.gridy = 2;
        c_refreshbutton.gridwidth = 1;
        c_refreshbutton.gridheight = 1;
        serverbrowser.add(REFRESH, c_refreshbutton);

        HELP = new JButton("HELP");
		HELP.addActionListener(this);
        HELP.setForeground(Color.white);
        HELP.setOpaque(true);
        HELP.setBorderPainted(false);
		HELP.setFocusPainted(false);
		HELP.setBackground(Color.decode("#f34711"));
        HELP.setMinimumSize(new Dimension(175,40));
        HELP.setPreferredSize(new Dimension(175,40));
		HELP.setFont(bfont);
		c_helpbutton = new GridBagConstraints();
		c_helpbutton.fill = GridBagConstraints.NONE;
		c_helpbutton.weightx = 0.25;
		c_helpbutton.weighty = 0.05;
		c_helpbutton.gridx = 3;
		c_helpbutton.gridy = 2;
        c_helpbutton.gridwidth = 1;
        c_helpbutton.gridheight = 1;
        serverbrowser.add(HELP, c_helpbutton);
    }

    // make the userbox with user name, total score, logout, and help button
    private void makeUserBox(Container cp){
        c_userbox = new GridBagConstraints();
        userbox = new JPanel(new GridBagLayout());
        c_userbox.fill = GridBagConstraints.BOTH;
        c_userbox.weightx = 0.3;
        c_userbox.weighty = 0.3;
        c_userbox.gridx = 1;
        c_userbox.gridy = 1;
        c_userbox.gridwidth = 1;
        c_userbox.gridheight = 1;
        c_userbox.insets = new Insets(24,12,12,24);
        userbox.setBackground(Color.decode("#FFFFFF"));
        userbox.setMinimumSize(new Dimension(350,300));
        userbox.setPreferredSize(new Dimension(350,300));
        cp.add(userbox, c_userbox);
        
        welcomeLabel = new JLabel("Welcome " + username + "!");
        c_welcomeLabel = new GridBagConstraints();
        welcomeLabel.setFont(f);
        welcomeLabel.setForeground(Color.decode("#616161"));
        c_welcomeLabel.fill = GridBagConstraints.BOTH;
        c_welcomeLabel.weightx = 1.0;
        c_welcomeLabel.weighty = 0.05;
		c_welcomeLabel.gridx = 0;
		c_welcomeLabel.gridy = 0;
        c_welcomeLabel.gridwidth = 1;
        c_welcomeLabel.gridheight = 1;
        c_welcomeLabel.insets = new Insets(8,16,0,0);

        scoreLabel = new JLabel(String.valueOf(lifetime_score));
        c_scoreLabel = new GridBagConstraints();
        Font scoreFont = new Font("Arial",Font.BOLD, 60);
        scoreLabel.setFont(scoreFont);
        scoreLabel.setForeground(Color.decode("#009688"));
        c_scoreLabel.fill = GridBagConstraints.NONE;
        c_scoreLabel.anchor = GridBagConstraints.PAGE_END;
        c_scoreLabel.weightx = 1.0;
        c_scoreLabel.weighty = 0.5;
		c_scoreLabel.gridx = 0;
		c_scoreLabel.gridy = 1;
        c_scoreLabel.gridwidth = 1;
        c_scoreLabel.gridheight = 1;
        
        scorecapLabel = new JLabel("points");
        c_scorecapLabel = new GridBagConstraints();
        scorecapLabel.setFont(f);
        scorecapLabel.setForeground(Color.decode("#616161")); 
        c_scorecapLabel.fill = GridBagConstraints.NONE;
        c_scorecapLabel.anchor = GridBagConstraints.CENTER;
        c_scorecapLabel.weightx = 1.0;
        c_scorecapLabel.weighty = 0.2;
		c_scorecapLabel.gridx = 0;
		c_scorecapLabel.gridy = 2;
        c_scorecapLabel.gridwidth = 1;
        c_scorecapLabel.gridheight = 1;

        LOGOUT = new JButton("LOGOUT");
        c_logout = new GridBagConstraints();
        LOGOUT.setForeground(Color.white);
		LOGOUT.setFocusPainted(false);
		LOGOUT.setBackground(Color.decode("#FF4081"));
        LOGOUT.setMinimumSize(new Dimension(150,40));
        LOGOUT.setPreferredSize(new Dimension(150,40));
		LOGOUT.setFont(bfont);
        LOGOUT.setOpaque(true);
        LOGOUT.setBorderPainted(false);
		LOGOUT.addActionListener(this);
        c_logout.fill = GridBagConstraints.NONE;
        c_logout.anchor = GridBagConstraints.CENTER;
        c_logout.weightx = 1.0;
        c_logout.weighty = 0.25;
		c_logout.gridx = 0;
		c_logout.gridy = 3;
        c_logout.gridwidth = 1;
        c_logout.gridheight = 1;
        
		userbox.add(welcomeLabel,c_welcomeLabel);
		userbox.add(scoreLabel,c_scoreLabel);
		userbox.add(scorecapLabel, c_scorecapLabel);
		userbox.add(LOGOUT,c_logout);
        // need to obtain username somehow
        // need to obtain total score somehow
        // logout button

    }

    private void makeChatBox(Container cp) {
    	
        c_chatbox = new GridBagConstraints();
        chatbox = new JPanel(new GridBagLayout());
        c_chatbox.fill = GridBagConstraints.BOTH;
        c_chatbox.weightx = 0.3;
        c_chatbox.weighty = 0.65;
        c_chatbox.gridx = 1;
        c_chatbox.gridy = 2;
        c_chatbox.gridwidth = 1;
        c_chatbox.gridheight = 1;
        c_chatbox.insets = new Insets(12,12,24,24);
        chatbox.setBackground(Color.decode("#FFFFFF"));
        chatbox.setMinimumSize(new Dimension(350,600));
        chatbox.setPreferredSize(new Dimension(350,600));
        cp.add(chatbox, c_chatbox);
        

        chatLabel = new JLabel("Messenger");
        c_chatLabel = new GridBagConstraints();
        chatLabel.setFont(f);
        chatLabel.setForeground(Color.decode("#616161"));
        c_chatLabel.fill = GridBagConstraints.BOTH;
        c_chatLabel.weightx = 1.0;
        c_chatLabel.weighty = 0.05;
		c_chatLabel.gridx = 0;
		c_chatLabel.gridy = 0;
        c_chatLabel.gridwidth = 1;
        c_chatLabel.gridheight = 1;
        c_chatLabel.insets = new Insets(4,16,0,0);
        
        
		chatlogarea = new JTextPane();
		// chatlogarea.setLineWrap(true);
		// chatlogarea.setWrapStyleWord(true);
		chatlogarea.setEditable(false);
		chatlogpane = new JScrollPane(chatlogarea);
		// chatlogpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // chatlogpane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatlogarea.setBorder(null);
		chatlogpane.setViewportBorder(null);
		c_chatlogpane = new GridBagConstraints();
		c_chatlogpane.fill = GridBagConstraints.BOTH;
		c_chatlogpane.weightx = 1.0;
		c_chatlogpane.weighty = 0.9;
		c_chatlogpane.gridx = 0;
		c_chatlogpane.gridy = 1;
        c_chatlogpane.gridwidth = 1;
        c_chatlogpane.gridheight = 1;
        /* Need to figure out how to display all the messages!*/

		chatinputfield = new JTextField();
		chatinputfield.setBorder(BorderFactory.createLineBorder(Color.decode("#757575")));
		chatinputfield.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromUser = chatinputfield.getText();
                if (fromUser.length() > 200) {
                    StyledDocument doc = landingPage.chatlogarea.getStyledDocument();
                    Style GameStyle = doc.getStyle("System");
                    Style gameSystemStyle = doc.getStyle("GameSystem");
                    try {
                        doc.insertString(doc.getLength(), "System: ", gameSystemStyle);
                        doc.insertString(doc.getLength(), "Chat messages are limited to 200 characters!", GameStyle);
                        doc.insertString(doc.getLength(), "\n", GameStyle);
                    } catch (Exception ex) {

                    }
                    chatinputfield.setText("");
                    landingPage.chatlogarea.setCaretPosition(landingPage.chatlogarea.getDocument().getLength());

                } else if (fromUser != null) {
                    sendChatMessage(fromUser);
                    chatinputfield.setText("");
                }

            }
        });
		c_chatinputfield = new GridBagConstraints();
		c_chatinputfield.fill = GridBagConstraints.BOTH;
		c_chatinputfield.weightx = 1.0;
		c_chatinputfield.weighty = 0.05;
		c_chatinputfield.gridx = 0;
		c_chatinputfield.gridy = 2;
        c_chatinputfield.gridwidth = 1;
        c_chatinputfield.gridheight = 1;

        chatbox.add(chatLabel, c_chatLabel);
		chatbox.add(chatlogpane, c_chatlogpane);
		chatbox.add(chatinputfield, c_chatinputfield);
    }

    public void actionPerformed(ActionEvent ae) {

        JButton b = (JButton)ae.getSource();
        // log out, this time killing the whole thing
        if (b.equals(LOGOUT)){

            //SEND LOGOUT REQUEST TO SERVER
            System.err.println("logging out...");
            try {
                log_out();
            } catch (Exception e) {
            }
        } else if (b.equals(JOINGAME)) {
            try {
                join_game(gid);
            } catch (Exception e) {
            }
        } else if (b.equals(CREATEGAME)) {
            try {
                gameName = JOptionPane.showInputDialog(this, "Enter name of game");
                if (gameName.length() > 30) {
                    StyledDocument doc = this.chatlogarea.getStyledDocument();
                    Style GameStyle = doc.getStyle("System");
                    Style gameSystemStyle = doc.getStyle("GameSystem");
                    doc.insertString(doc.getLength(), "System: ", gameSystemStyle);
                    doc.insertString(doc.getLength(), "Game name must be less than 30 characters!", GameStyle);
                    doc.insertString(doc.getLength(), "\n", GameStyle);
                    this.chatlogarea.setCaretPosition(landingPage.chatlogarea.getDocument().getLength());

                }else if (!gameName.equals("") && gameName != null) {
                    create_game(gameName);
                }else{
                    StyledDocument doc = this.chatlogarea.getStyledDocument();
                    Style GameStyle = doc.getStyle("System");
                    Style gameSystemStyle = doc.getStyle("GameSystem");
                    doc.insertString(doc.getLength(), "System: ", gameSystemStyle);
                    doc.insertString(doc.getLength(), "You must enter a name to create a game!", GameStyle);
                    doc.insertString(doc.getLength(), "\n", GameStyle);
                    this.chatlogarea.setCaretPosition(landingPage.chatlogarea.getDocument().getLength());
                }
            } catch (Exception e) {
            }
        }else if (b.equals(REFRESH)){
        	requestupdateServerList();
        }else if (b.equals(HELP)){

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://krisht.github.io/GameOfSet/#landing-page-help"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

        	//showHelpDialog();
        	
        }
        //PERFORM ACTION ON TEXT FIELD FOR CHAT BOX
    }

    private void log_out() {
        JSONObject loggingoutobj = new JSONObject();
        loggingoutobj.put("fCall", "loggingOut");
        loggingoutobj.put("uid", uid);
        try {
            newConnectionThread.messageServer(loggingoutobj);
        } catch(Exception e){
            e.printStackTrace();

        }
    }

    private void join_game (int newgid){
        if (newgid == -1) {
            StyledDocument doc = this.chatlogarea.getStyledDocument();
            Style systemStyle = doc.getStyle("System");
            Style gameSystemStyle = doc.getStyle("GameSystem");
            try {
                doc.insertString(doc.getLength(), "System: ", gameSystemStyle);
                doc.insertString(doc.getLength(), "No game selected. Please select a game or click refresh.\n", systemStyle);
            }
            catch (BadLocationException e) {
                e.printStackTrace();
            }
            this.chatlogarea.setCaretPosition(this.chatlogarea.getDocument().getLength());
        } else {
            JSONObject joingameobj = new JSONObject();
            joingameobj.put("fCall", "joinGame");
            joingameobj.put("uid", uid);
            joingameobj.put("gid", newgid);
            try {
                newConnectionThread.messageServer(joingameobj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void create_game (String gameName) {
        JSONObject creategameobj = new JSONObject();
        creategameobj.put("fCall", "createGame");
        creategameobj.put("uid", uid);
        creategameobj.put("gameName", gameName);
        try{
            newConnectionThread.messageServer(creategameobj);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void makeGameListings(){
    	// make the grid layout first
    	// make a JPanel and append it to gridlayout
    	int counter = 0;
    	int column_counter = 0;
    	int row_counter = 0;
    	location_to_gid.clear();
    	gid = -1;
        for (int i = 0 ; i < list_of_games_buttons.size(); i++){
            list_of_games_buttons.get(i).setBackground(Color.decode("#FFFFFF"));
        }
    	for (int i = 0 ; i < list_of_games_buttons.size(); i++){
    		list_of_games_panel.remove(list_of_games_buttons.get(i));
    	}
    	list_of_games_buttons.clear();
        this.repaint();
        this.revalidate();
    	// serverlistpane.remove(list_of_games_panel);
    	while (counter < listofGames.size()){
        	GridBagConstraints c_panel = new GridBagConstraints();
    		c_panel.fill = GridBagConstraints.NONE;
    		c_panel.weightx = 1.0;
    		c_panel.weighty = 1.0;
    		c_panel.gridx = column_counter;
    		c_panel.gridy = row_counter;
    		c_panel.gridwidth = 1;
    		c_panel.gridheight = 1;
    		list_of_games_buttons.add(make_game_selection_panel(listofGames.get(counter)));
    		location_to_gid.put(counter, listofGames.get(counter).getGid());
    		list_of_games_panel.add(list_of_games_buttons.get(counter), c_panel);
    		column_counter += 1;
    		if (column_counter == 3){
    			column_counter = 0;
    			row_counter += 1;
    		}
    		counter += 1;
    	}
    	// serverlistpane.add(list_of_games_panel);
        this.repaint();
        this.revalidate();
    }
    
    // make one panel with the game name and the number of players in the game,
    // should take some arguments, including GID, number of players and game name
    private JButton make_game_selection_panel(GameListing game){
    	int gid = game.getGid();
    	String game_name = game.getGname();
    	int num_players = 0;
    	
    	JLabel p_name1 = new JLabel();
    	JLabel p_name2 = new JLabel();
    	JLabel p_name3 = new JLabel();
    	JLabel p_name4 = new JLabel();
    	
        Font f_big = new Font("Arial",Font.BOLD, 60);
    	JButton p = new JButton();
        p.setOpaque(true);
        p.setBorderPainted(false);
    	p.setBorderPainted(false);
    	p.setLayout(new GridBagLayout());
    	p.setMinimumSize(new Dimension(200,125));
        p.setPreferredSize(new Dimension(200,125));
        p.setBackground(Color.WHITE);
        p.addActionListener(listener);
        
    	if (!game.getPlayer1().equals("")){
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name1.setText(game.getPlayer1());
            p_name1.setMinimumSize(new Dimension(140,20));
            p_name1.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 1;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name1, c_p);
    		num_players += 1;
    	}else{
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name1.setText(" ");
            p_name1.setMinimumSize(new Dimension(140,20));
            p_name1.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 1;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name1, c_p);
    	}
    	if (!game.getPlayer2().equals("")){
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name2.setText(game.getPlayer2());
            p_name2.setMinimumSize(new Dimension(140,20));
            p_name2.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 2;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name2, c_p);
    		num_players += 1;
    	}else{
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name2.setText(" ");
            p_name2.setMinimumSize(new Dimension(140,20));
            p_name2.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 2;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name2, c_p);
    	}
    	if (!game.getPlayer3().equals("")){
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name3.setText(game.getPlayer3());
            p_name3.setMinimumSize(new Dimension(140,20));
            p_name3.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 3;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name3, c_p);
    		num_players += 1;
    	}else{
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name3.setText(" ");
            p_name3.setMinimumSize(new Dimension(140,20));
            p_name3.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 3;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name3, c_p);
    	}
    	if (!game.getPlayer4().equals("")){
            p_name4.setMinimumSize(new Dimension(140,20));
            p_name4.setPreferredSize(new Dimension(140,20));
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name4.setText(game.getPlayer4());
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 4;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name4, c_p);
    		num_players += 1;
    	}else{
        	GridBagConstraints c_p = new GridBagConstraints();
    		p_name4.setText(" ");
            p_name4.setMinimumSize(new Dimension(140,20));
            p_name4.setPreferredSize(new Dimension(140,20));
    		c_p.fill = GridBagConstraints.NONE;
    		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
            c_p.weightx = 0.5;
            c_p.weighty = 0.125;
    		c_p.gridx = 0;
    		c_p.gridy = 4;
            c_p.gridwidth = 1;
            c_p.gridheight = 1;
            p.add(p_name4, c_p);
    	}
    	
    	JLabel p_name = new JLabel();
    	GridBagConstraints c_p = new GridBagConstraints();
		p_name.setText(" ");
        p_name.setMinimumSize(new Dimension(140,20));
        p_name.setPreferredSize(new Dimension(140,20));
		c_p.fill = GridBagConstraints.NONE;
		c_p.anchor = GridBagConstraints.FIRST_LINE_START;
        c_p.weightx = 0.5;
        c_p.weighty = 0.125;
		c_p.gridx = 0;
		c_p.gridy = 5;
        c_p.gridwidth = 1;
        c_p.gridheight = 1;
        p.add(p_name, c_p);
        
    	JLabel numPlayersLabel = new JLabel(String.valueOf(num_players));
        numPlayersLabel.setMinimumSize(new Dimension(60,50));
        numPlayersLabel.setPreferredSize(new Dimension(60,50));
        GridBagConstraints c_numPlayersLabel = new GridBagConstraints();
        numPlayersLabel.setFont(f_big);
        if (num_players == 1){
            numPlayersLabel.setForeground(Color.decode("#80CBC4"));
        }else if (num_players == 2){
        	numPlayersLabel.setForeground(Color.decode("#26A69A"));
        }else if (num_players == 3){
        	numPlayersLabel.setForeground(Color.decode("#00897B"));
        }else{
        	numPlayersLabel.setForeground(Color.decode("#00695C"));
        }
        c_numPlayersLabel.fill = GridBagConstraints.NONE;
        c_numPlayersLabel.weightx = 0.5;
        c_numPlayersLabel.weighty = 1.0;
		c_numPlayersLabel.gridx = 1;
		c_numPlayersLabel.gridy = 0;
        c_numPlayersLabel.gridwidth = 1;
        c_numPlayersLabel.gridheight = 6;
        p.add(numPlayersLabel, c_numPlayersLabel);
        
    	JLabel gamenameLabel = new JLabel(game_name);
        gamenameLabel.setMinimumSize(new Dimension(140,70));
        gamenameLabel.setPreferredSize(new Dimension(140,70));
    	GridBagConstraints c_gamenameLabel = new GridBagConstraints();
        Font f_medium = new Font("Arial",Font.PLAIN, 20);
        gamenameLabel.setFont(f_medium);
        gamenameLabel.setForeground(Color.decode("#616161"));
        c_gamenameLabel.fill = GridBagConstraints.NONE;
        c_gamenameLabel.anchor = GridBagConstraints.LINE_START;
        c_gamenameLabel.weightx = 0.5;
        c_gamenameLabel.weighty = 0.5;
		c_gamenameLabel.gridx = 0;
		c_gamenameLabel.gridy = 0;
        c_gamenameLabel.gridwidth = 1;
        c_gamenameLabel.gridheight = 1;
        p.add(gamenameLabel, c_gamenameLabel);
        
        return p;
    }
    
    void enterGame(){
    	 try {
             gb = new GameBoard_Front();
             this.setVisible(false);
             gb.setVisible(true);
             gb.setTitle("SET GAME");
         } catch (Exception e) {
         }
    }



    public void getUserScore(){
        JSONObject userscoreobj = new JSONObject();
        userscoreobj.put("fCall", "playerScore");
        userscoreobj.put("uid", uid);
        try{
            newConnectionThread.messageServer(userscoreobj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void requestupdateServerList() {
        JSONObject servupobj = new JSONObject();
        servupobj.put("fCall", "getGameListing");
        servupobj.put("uid", uid);
        try {
             newConnectionThread.messageServer(servupobj);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendChatMessage(String msg) {
        JSONObject chatmsgobj = new JSONObject();
        chatmsgobj.put("fCall", "sendPublicMessage");
        chatmsgobj.put("uid", uid);
        chatmsgobj.put("msg", msg);
        try {
            newConnectionThread.messageServer(chatmsgobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void reset_user_score(){
    	scoreLabel.setText(String.valueOf(lifetime_score));
    }
    
    
}
