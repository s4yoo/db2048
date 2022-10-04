import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.event.*;

class LogIn extends JFrame {
       TextField id, password;
       JButton Sign_in, Log_in;
       ResultSet rs;
       Statement stmt;
       String Welcome = "Welcome!";
       
       public LogIn() throws SQLException {	//로그인 화면
             super("Log in");
             Connection con = makeConnection();
             stmt = con.createStatement();
            // GridBagLayout gridBagn = new GridBagLayout();
             
            // rs = stmt.executeQuery("SELECT * FROM user");
             password = new TextField();
             setLayout(new GridLayout(0, 2, 2, 1));
             add(new JLabel("ID", JLabel.CENTER));
             add(id = new TextField());
             add(new JLabel("PASSWORD",JLabel.CENTER));
             add(password);
           	 password.setEchoChar('*');
    
             Sign_in = new JButton("Sign in");
             Sign_in.addActionListener(new ActionListener(){
            	 public void actionPerformed(ActionEvent event) {            		 
            		 StringBuilder sb = new StringBuilder();
            		 String sql;
            		 String idChk = id.getText();
            		 String pwChk = password.getText();
            		 try {
            			 rs = stmt.executeQuery("Select * from user");
            			 while(rs.next()) {
            				 if (idChk.equals(rs.getString("user_id"))) {
            					 new IdSame();
            					 return;
            				 }
            			 }
            			 if(idChk == null || idChk.length() == 0 || pwChk == null || pwChk.length() == 0) {
            				 new LoginFail();
            			 }
            			 else {
            				 sql = sb.append("insert into user(user_id, user_pw) values('")
            						 .append(id.getText() + "', '")
            						 .append(password.getText() + "');")
            						 .toString();
            			 
            				 	stmt.executeUpdate(sql);
            				 	new SignSuccess();
            				 	System.out.println("가입성공");

            			 	} 
            			 } catch (SQLException e) {
         			 			e.printStackTrace();
         			 			System.out.println("가입 실패");
            		 }
            	 }
             });
            
      
            
             Log_in=new JButton("Log in");
             Log_in.addActionListener(new ActionListener(){
            	   public void actionPerformed(ActionEvent event) {
                       try {
                    	   boolean i = false;
                    	   rs = stmt.executeQuery("SELECT * FROM user");
                    	   while(rs.next()) {
                    		   if(id.getText().equals(rs.getString("user_id")) && password.getText().equals(rs.getString("user_pw"))) {
                    			   //현재 로그인 창을 끄고 게임창을 켤 것. 킬 때 rs.getInt(user_tag)를 넘겨줄 것.
                    			   System.out.println("로그인 성공");
                    			   new Gameboard(rs.getInt("user_tag"), rs, stmt);
                    			   i = false;
                    			   dispose();
                    			   break;
                    		   }
                    		   else {
                    			   System.out.println("로그인 실패");
                    			   i = true;
                    		   }
                    	   }   
                    		   if(i) {
                    			   new LoginFail();
                    			   i = false;
                    		   }
                    	   
                       } catch (SQLException e) {
                             e.printStackTrace();
                             System.out.println("연동 실패");

                       }
                }
             }) ;
             add(Sign_in);
           
             add(Log_in);
             
             setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             pack();
             setVisible(true);
             setSize(300,100);
       }
       
   	public static Connection makeConnection() {	//mySQL 연동
		//MySQL 접속시
		String url = "jdbc:mysql://localhost/project";
		
		//오라클 11g   orcl 접속시
		//String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		
		//교수용컴퓨터접속시 11g 포트 1522
		//String url = "jdbc:oracle:thin:@localhost:1522:DBSERVER";
		
		String id = "root";
		String password = "young0202";
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 적재 성공!!!!");
			con = DriverManager.getConnection(url, id, password);
			System.out.println("데이터베이스 연결 성공!!!!");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다.");
		} catch (SQLException e) {
			System.out.println("연결에 실패하였습니다.");
		}
		return con;
	}

}
 
class SignSuccess extends JFrame{	//가입 성공 메시지 화면
	SignSuccess() {
		setTitle("Sign in Success!");
		
		JPanel NewWindowContainer = new JPanel();
		setContentPane(NewWindowContainer);
		
		JLabel NewLabel = new JLabel("Sign in Success!");
		
		NewWindowContainer.add(NewLabel);
		
		setSize(300,100);
		setResizable(false);
		setVisible(true);
	}
}

class LoginFail extends JFrame{	//로그인 실패 메시지 화면
	LoginFail(){
		setTitle("Log in Fail!");
		
		JPanel NewWindowContainer = new JPanel();
		setContentPane(NewWindowContainer);
		
		JLabel NewLabel = new JLabel("ID 또는 PW를 확인해주세요!");
		
		NewWindowContainer.add(NewLabel);
		
		setSize(300,100);
		setResizable(false);
		setVisible(true);
	}
}

class IdSame extends JFrame{	//로그인 실패 메시지 화면
	IdSame(){
		setTitle("Your ID has been used.");
		
		JPanel NewWindowContainer = new JPanel();
		setContentPane(NewWindowContainer);
		
		JLabel NewLabel = new JLabel("사용하고 있는 ID입니다.");
		
		NewWindowContainer.add(NewLabel);
		
		setSize(300,100);
		setResizable(false);
		setVisible(true);
	}
}

class PwFail extends JFrame{
	PwFail(){
		setTitle("Fail!");
		
		JPanel NewWindowContainer = new JPanel();
		setContentPane(NewWindowContainer);
		
		JLabel NewLabel = new JLabel("PW를 확인해주세요!");
		
		NewWindowContainer.add(NewLabel);
		
		setSize(300,100);
		setResizable(false);
		setVisible(true);
	}
}

class PwSuccess extends JFrame{
	PwSuccess(){
		setTitle("Success!");
		
		JPanel NewWindowContainer = new JPanel();
		setContentPane(NewWindowContainer);
		
		JLabel NewLabel = new JLabel("Success!");
		
		NewWindowContainer.add(NewLabel);
		
		setSize(300,100);
		setResizable(false);
		setVisible(true);
	}
}

class dltSuccess extends JFrame{
	dltSuccess(){
		setTitle("Success!");
		
		JPanel NewWindowContainer = new JPanel();
		setContentPane(NewWindowContainer);
		
		JLabel NewLabel = new JLabel("Success!");
		
		NewWindowContainer.add(NewLabel);
		
		setSize(300,100);
		setResizable(false);
		setVisible(true);
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) { 
                    System.exit(0);
            }
		});
	}
}

class Gameboard extends JFrame{ //게임화면
    Gameboard(int tag, ResultSet rs, Statement stmt) throws SQLException{
		setTitle("Play");
		JButton ThreebyThree, FourbyFour, FivebyFive, MyInfo;
		
		//JLabel newLabel = new JLabel();
		setLayout(new GridLayout(0, 1, 0 , 1));
		String userName = rs.getString("user_id") + "#" + rs.getInt("user_tag") + ", Welcome!";
		add(new JLabel(userName, JLabel.CENTER));
		
		JButton LadderSearch = new JButton("Ladder & Search");
		LadderSearch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				System.out.println("Ladder & Search Button push");
				//ladder 검색용 창 넣기
				new LadderAndSearch(stmt);
			}
		});
		
		ThreebyThree = new JButton("3 * 3");//3*3게임
		ThreebyThree.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				System.out.println("3 * 3 Button push");
				new ThreebyThreeBoard(stmt, tag);
			}
		});
		
		FourbyFour = new JButton("4 * 4");//4*4게임
		FourbyFour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("4 * 4 Button push");
				new FourbyFourBoard(stmt, tag);
			}
		});
		
		FivebyFive = new JButton("5 * 5");//5*5게임
		FivebyFive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("5 * 5 Button push");
				new FivebyFiveBoard(stmt, tag);
			}
		});
		
		MyInfo = new JButton("My Info");
		MyInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("MyInfo btn push");
				new MyInfoBoard(stmt, tag);
			}
		});
		
		add(MyInfo);
		add(LadderSearch);
		add(ThreebyThree);
		add(FourbyFour);
		add(FivebyFive);
		setSize(200, 200);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class MyInfoBoard extends JFrame{
	JPanel changePwPanel, deleteUserPanel, pwPanel;
	JButton deleteUserButton = new JButton("Delete My ID");
	JButton changePwBtn = new JButton("Change Pw");
	TextField password, changePw, chkChangePw;
	JLabel jlPassword, jlChangePw, jlChkChangePw;
	GridBagLayout gblPw, gblChange;
	GridBagConstraints[] gbcPw = new GridBagConstraints[2];
	GridBagConstraints[] gbcChange = new GridBagConstraints[5];
	ResultSet rs;
	boolean chk = false;
	public MyInfoBoard(Statement stmt, int userTag) {
		
		gblPw = new GridBagLayout();
		gblChange = new GridBagLayout();
		
		pwPanel = new JPanel();
		pwPanel.setLayout(gblPw);
		changePwPanel = new JPanel();
		changePwPanel.setLayout(gblChange);
		deleteUserPanel = new JPanel(new GridLayout(1, 1));
		
		for(int i = 0; i < 2; i++)
			gbcPw[i] = new GridBagConstraints();
		
		for(int i = 0; i < 5; i++)
			gbcChange[i] = new GridBagConstraints();
		
		password = new TextField();
		changePw = new TextField();
		chkChangePw = new TextField();
		
		jlPassword = new JLabel("pw", JLabel.CENTER);
		jlChangePw = new JLabel("change Pw", JLabel.CENTER);
		jlChkChangePw = new JLabel("Chk Change Pw", JLabel.CENTER);
		
		gbcPw[0].gridx = 0;
		gbcPw[0].gridx = 0;
		gbcPw[0].weightx = 1;
		gbcPw[0].weighty = 1;
		gbcPw[0].fill = GridBagConstraints.BOTH;
		pwPanel.add(jlPassword, gbcPw[0]);
		gbcPw[1].gridx = 1;
		gbcPw[1].gridy = 0;
		gbcPw[1].weightx = 1;
		gbcPw[1].weightx = 1;
		gbcPw[1].fill = GridBagConstraints.BOTH;
		pwPanel.add(password, gbcPw[1]);
		password.setEchoChar('*');
		
		gbcChange[0].gridx = 0;
		gbcChange[0].gridy = 0;
		gbcChange[0].weightx = 1;
		gbcChange[0].weighty = 1;
		gbcChange[0].fill = GridBagConstraints.BOTH;
		changePwPanel.add(jlChangePw, gbcChange[0]);
		gbcChange[1].gridx = 1;
		gbcChange[1].gridy = 0;
		gbcChange[1].weightx = 1;
		gbcChange[1].weighty = 1;
		gbcChange[1].fill = GridBagConstraints.BOTH;
		changePwPanel.add(changePw, gbcChange[1]);
		gbcChange[2].gridx = 0;
		gbcChange[2].gridy = 1;
		gbcChange[2].weightx = 1;
		gbcChange[2].weighty = 1;
		gbcChange[2].fill = GridBagConstraints.BOTH;
		changePwPanel.add(jlChkChangePw, gbcChange[2]);
		gbcChange[3].gridx = 1;
		gbcChange[3].gridy = 1;
		gbcChange[3].weightx = 1;
		gbcChange[3].weighty = 1;
		gbcChange[3].fill = GridBagConstraints.BOTH;
		changePwPanel.add(chkChangePw, gbcChange[3]);
		gbcChange[4].gridx = 0;
		gbcChange[4].gridy = 2;
		gbcChange[4].gridheight = 1;
		gbcChange[4].gridwidth = 2;
		gbcChange[4].weightx = 1;
		gbcChange[4].weighty = 1;
		gbcChange[4].fill = GridBagConstraints.BOTH;
		changePwPanel.add(changePwBtn, gbcChange[4]);
		changePw.setEchoChar('*');
		chkChangePw.setEchoChar('*');
		
		deleteUserPanel.add(deleteUserButton);
		
		changePwBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("ChangeBtn push");
				try {
					rs = stmt.executeQuery("SELECT * FROM user");
					while(rs.next()) {
						if(password.getText().equals(rs.getString("user_pw")) && changePw.getText().equals(chkChangePw.getText()) && (userTag == rs.getInt("user_tag"))) {
							System.out.println("pw pass");
							stmt.executeUpdate("Update user set user_pw = '" + changePw.getText() +"' where user_tag = " + userTag + ";");
							chk = false;
							new PwSuccess();
							break;
						}
						else
							chk = true;
					}
					if(chk) {
						new PwFail();
						chk = false;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		deleteUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("DltBtn push");
				try {
					rs = stmt.executeQuery("SELECT * FROM user");
					while(rs.next()) {
						if(password.getText().equals(rs.getString("user_pw")) && (userTag == rs.getInt("user_tag"))) {
							System.out.println("pw pass");
							stmt.executeUpdate("delete from user where user_tag = " + userTag + ";");
							stmt.executeUpdate("delete from result where user_tag = " + userTag + ";");
							chk = false;
							new dltSuccess();
							break;
						}
						else
							chk = true;
					}
					if(chk) {
						new PwFail();
						chk = false;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		getContentPane().add("North", pwPanel);
		getContentPane().add("Center", changePwPanel);
		getContentPane().add("East", deleteUserPanel);
		setVisible(true);
		setSize(500, 250);
	}
}

class LadderAndSearch extends JFrame{
	String arr[] = new String[5];
	String str = null;
	String success;
	int a, i;
	String title[] = {"Rank", "Nickname", "Score", "Mode", "Success"};
	DefaultTableModel model = new DefaultTableModel(title, 0);
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	
	public LadderAndSearch(Statement stmt) {
		super("Ladder & Search");
		//JFrame frame = new JFrame("Ladder");
		
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel jpLadder, jpSearch;
		JRadioButton ThreebyThree = new JRadioButton("3 X 3");
		JRadioButton FourbyFour = new JRadioButton("4 X 4");
		JRadioButton FivebyFive = new JRadioButton("5 X 5");
		JRadioButton AllButton = new JRadioButton("ALL");
		
		JTable table = new JTable(model);
		
		//model.addRow(arr);
		jpLadder = new JPanel(new GridLayout(1, 3));
		jpSearch = new JPanel(new GridLayout(1, 4));
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(ThreebyThree);
		buttonGroup.add(FourbyFour);
		buttonGroup.add(FivebyFive);
		buttonGroup.add(AllButton);
		
		AllButton.setSelected(true);
		makeTable(stmt, null);
		
		ThreebyThree.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.DESELECTED) return;
				makeTable(stmt, "3*3");
				table.setModel(model);
			}
		});
		
		FourbyFour.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.DESELECTED) return;
				makeTable(stmt, "4*4");
				table.setModel(model);
			}
		});
		
		FivebyFive.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.DESELECTED) return;
				makeTable(stmt, "5*5");
				table.setModel(model);
			}
		});
		
		AllButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.DESELECTED) return;
				makeTable(stmt, null);
				table.setModel(model);
			}
		});
		
		TableColumnModel tcm = table.getColumnModel();
		
		for(int i = 0; i < tcm.getColumnCount(); i++)
			tcm.getColumn(i).setCellRenderer(dtcr);
		
		jpSearch.add(AllButton);
		jpSearch.add(ThreebyThree);
		jpSearch.add(FourbyFour);
		jpSearch.add(FivebyFive);
		
		jpLadder.add(scrollPane);
		
		getContentPane().add("North", jpSearch);
		getContentPane().add("Center", jpLadder);
		setBounds(0, 0, 400, 310);
		setVisible(true);
		setResizable(false);
		//setSize(100,100);
		
	}
	
	public void makeTable(Statement stmt, String mode) {
		int rank = 0;
		while(model.getRowCount() > 0)
			model.removeRow(0);
		try {
			ResultSet rs;
			if(mode == null) rs = stmt.executeQuery("Select * from result r, user u where r.user_tag = u.user_tag order by success desc, score desc;");
			else rs = stmt.executeQuery("Select * from result r, user u where r.user_tag = u.user_tag and r.mode ='" + mode + "' order by success desc, score desc;");
			System.out.println("결과 불러오기 성공");
			while (rs.next()) {
				if(rs.getBoolean("r.success"))
					success = "Success";
				else
					success = "Fail";
				arr[0] = String.valueOf(++rank);
				arr[1] = rs.getString("u.user_id") + "#" + rs.getString("u.user_tag");
				arr[2] = rs.getString("r.score");
				arr[3] = rs.getString("r.mode");
				arr[4] = success;
			
				model.addRow(arr);
				//model.fireTableDataChanged();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.printf("불러오기 실패");
		}
	}
}

class Helper extends JFrame{
	JPanel jpBoard = new JPanel(new GridLayout(1, 1));
	JTextArea jlLabel = new JTextArea();
	
	public Helper() {
		super("Helper");
		jlLabel.setText("방향키를 이용하여 같은 숫자기리 합쳐서 최대숫자를 만드는 게임입니다.\n"
				+ "3*3에서는 512, 4*4에서는 2048을 만드는 것이 목표입니다.\n"
				+ "5*5에서는 목표 숫자가 없고 최대 점수를 달성하는 것이 목표입니다.");
		jlLabel.setFont(new Font("굴림",Font.CENTER_BASELINE, 20));
		jpBoard.add(jlLabel);
		getContentPane().add("Center", jpBoard);
		setSize(715, 150);
		setVisible(true);
		setResizable(true);
	}
}

class ThreebyThreeBoard extends JFrame{
	JPanel jpBoard, jpScore;
	JMenuBar jmb;
	JMenu jmReset, jmHelp;
	JMenuItem mReset, mHelp;
	JLabel jlBoard[] = new JLabel[9];
	JLabel jlScore;
	int gameoverChk = 0;
	int board[] = new int[9];
	
	int score = 0;
	
	public ThreebyThreeBoard(Statement stmt, int tag) {
		super("3 * 3");
		getContentPane().setLayout(new BorderLayout());
		jmb = new JMenuBar();
		
		jmReset = new JMenu("Reset(R)");
		jmHelp = new JMenu("Help(H)");
		
		mReset = new JMenuItem("Reset");
		mHelp = new JMenuItem("Help");
		
		jmReset.add(mReset);
		
		jmHelp.add(mHelp);
		
		jmb.add(jmReset);
		jmb.add(jmHelp);
		
		jpBoard = new JPanel(new GridLayout(3, 3, 2, 2));
		jpBoard.setBackground(Color.GRAY);
		jpScore = new JPanel(new GridLayout(1, 3, 2, 2));
		jpScore.setBackground(Color.WHITE);
		System.out.println("패털 만들기 성공");
		
		jlScore = new JLabel(String.valueOf(score), JLabel.CENTER);
		jlScore.setBackground(Color.WHITE);
		jlScore.setFont(new Font("굴림", Font.BOLD, 50));
		jpScore.add(jlScore);
		System.out.println("jlScore 만듦");
		
		Arrays.fill(board, 0);
		
		for(int i = 0; i < 9; i++) {
			jlBoard[i] = new JLabel("", JLabel.CENTER);
			jlBoard[i].setFont(new Font("굴림", Font.BOLD, 50));
			jlBoard[i].setOpaque(true);
			jpBoard.add(jlBoard[i]);
		}
		
		System.out.println("보드 만들기 성공");
		
		jmReset.setMnemonic('R');
		jmHelp.setMnemonic('H');
		
		mReset.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
		mHelp.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_MASK));
		
		mReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newGame();
			}
		}); //기능 추가하기
		mHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new Helper();
			}
		}); //기능 추가하기
		
		System.out.println("매뉴만들기 성공");
		
		getContentPane().add("North", jpScore);
		getContentPane().add("Center", jpBoard);
		System.out.println("패널 넣기 성공");
		setJMenuBar(jmb);//이거 작동 안함
		setSize(500, 500);
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setVisible(true);
		makeGame();
		makeGame();
		BoardReset();
		if(gameoverChk == 0)
			addKeyListener(new keyboardAction(stmt, tag));
	}
	
	public void newGame() {
		int i;
		
		for(i = 0; i < 9; i++)
			board[i] = 0;
		
		score = 0;
		gameoverChk = 0;
		jlScore.setText(String.valueOf(score));
		
		makeGame();
		makeGame();
		BoardReset();
	}
	
	public void BoardReset() {
		for(int i = 0; i < 9 ; i++) {
			if(board[i] == 0) {
				jlBoard[i].setText("");
				jlBoard[i].setBackground(new Color(205, 193, 180));
			}
			else {
				jlBoard[i].setText(String.valueOf(board[i]));
				switch(board[i]) {
				case 2:
					jlBoard[i].setBackground(new Color(238, 228, 218));
					break;
				case 4:
					jlBoard[i].setBackground(new Color(237, 224, 200));
					break;
				case 8:
					jlBoard[i].setBackground(new Color(242, 177, 121));
					break;
				case 16:
					jlBoard[i].setBackground(new Color(245, 149, 99));
					break;
				case 32:
					jlBoard[i].setBackground(new Color(246, 124, 95));
					break;
				case 64:
					jlBoard[i].setBackground(new Color(246, 94, 59));
					break;
				default:
					jlBoard[i].setBackground(new Color(237, 207, 114));
				}
			}
		}
	}
	
	public void makeGame() { // 이거 작동하다가 뻑남
		Random random = new Random();
		int pos = 0, chk = 0;
		if(board[0] == 0 || board[1] == 0 || board[2] == 0 || board[3] == 0 || board[4] == 0 || board[5] == 0 || board[6] == 0 || board[7] == 0 || board[8] == 0) {
			do {
				pos = random.nextInt(9);
			}while(board[pos] != 0); // 여기서 무한루프 들어가는 듯
			chk = 1;
		}
		if( chk == 1) {
			int val = random.nextInt(100) > 80 ? 4 : 2;
			board[pos] = val;
			System.out.println(pos);
		}
	}
	
	public void checkGameOver(Statement stmt, int tag) {
		StringBuilder sb = new StringBuilder();
		String sql;
		int i;
		
		for(i = 0; i <= 8; i++)
			if(board[i] == 0)
				return;
		
		for(i = 0; i <= 8; i++) {
			if(i == 2 || i == 5 || i == 8 || i == 6 || i == 7)
				continue;
			if(board[i+3] == board[i] || board[i] == board[i+1])
				return;
		}
		
		for(i = 0; i <= 5; i++) {
			if(i == 2 || i == 5)
				if(board[i] == board[i+3])
					return;
		}
		
		for(i = 0; i < 8; i++) {
			if(i == 6 || i == 7)
				if(board[i] == board[i+1])
					return;
		}
		
		try {
			gameoverChk = 1;
			sql = sb.append("insert into result values('")
					 .append(tag + "', '")
					 .append(jlScore.getText() + "', ")
					 .append("0, '3*3');")
					 .toString();
			stmt.executeUpdate(sql);
			jlScore.setText("Game Over");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void check_1024(Statement stmt, int tag) {
		StringBuilder sb = new StringBuilder();
		int i;
		String sql;
		
		for(i = 0; i <= 8; i++) {
			if(board[i] == 514) {
				gameoverChk = 1;
				try {
					sql = sb.append("insert into result values('")
							 .append(tag + "', '")
							 .append(jlScore.getText() + "', ")
							 .append("1, '3*3');")
							 .toString();
					stmt.executeUpdate(sql);
					jlScore.setText("Winner Winner Chicken Dinner!");
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class keyboardAction extends KeyAdapter{
		Statement st;
		ResultSet resultSet;
		int tag;
		keyboardAction(Statement stmt, int tag){
			st = stmt;
			this.tag = tag;
		}
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			int i = -1, chk, move = 0;
			if(gameoverChk == 1)
				return;
			switch(key) {
			case KeyEvent.VK_UP:
				for(;i != 0;) {
					i = 0;
					for(chk = 3; chk <= 8; chk++) {
						if(board[chk] == 0 || board[chk] > 10000) continue;
						if(board[chk-3] != 0 && board[chk-3] != board[chk]) continue;
						if(board[chk-3] == 0) {
							board[chk-3] = board[chk];
							move = 1;
						}
						else if(board[chk] < 10000 && board[chk] == board[chk-3]) {
							board[chk-3] *= 2;
							board[chk-3] += 10000;
							score += 2 * board[chk];
							move = 1;
						}
						board[chk] = 0;
						i++;
					}
				}
				System.out.println("위");
				break;
				
			case KeyEvent.VK_DOWN:
				for(;i != 0;) {
					i = 0;
					for(chk = 5; chk >= 0; chk--) {
						if(board[chk] == 0 || board[chk] > 10000) continue;
						if(board[chk+3] != 0 && board[chk+3] != board[chk]) continue;
						if(board[chk+3] == 0) {
							board[chk+3] = board[chk];
							move = 1;
						}
						else if(board[chk] < 10000 && board[chk] == board[chk+3]) {
							board[chk+3] *= 2;
							board[chk+3] += 10000;
							score += 2 * board[chk];
							move = 1;
						}
						board[chk] = 0;
						i++;
					}
				}
				System.out.println("아래");
				break;
			case KeyEvent.VK_LEFT:
				for(; i != 0;) {
					i = 0;
					for(chk = 0; chk <= 8; chk++) {
						if(chk == 0 || chk == 3 || chk == 6) continue;
						if(board[chk] == 0 || board[chk] > 10000) continue;
						if(board[chk-1] != 0 && board[chk-1] != board[chk]) continue;
						if(board[chk-1] == 0) {
							board[chk-1] = board[chk];
							move = 1;
						}
						else if(board[chk] < 10000 && board[chk] == board[chk-1]) {
							board[chk-1] *= 2;
							board[chk-1] += 10000;
							score += 2 * board[chk];
							move = 1;
						}
						board[chk] = 0;
						i++;
					}
				}
				System.out.println("왼쪽");
				break;
			case KeyEvent.VK_RIGHT:
				for(;i != 0;) {
					i = 0;
					for(chk = 8; chk >= 0; chk--) {
						if(chk == 2 || chk == 5 || chk == 8) continue;
						if(board[chk] == 0 || board[chk] > 10000) continue;
						if(board[chk+1] != 0 && board[chk+1] != board[chk]) continue;
						if(board[chk+1] == 0) {
							board[chk+1] = board[chk];
							move = 1;
						}
						else if(board[chk] == board[chk+1]) {
							board[chk+1] *= 2;
							board[chk+1] += 10000;
							score += 2 * board[chk];
							move = 1;
						}
						board[chk] = 0;
						i++;
					}
				}
				System.out.println("오른쪽");
				break;
			}
			
			for(i = 0; i <= 8; i++)
				if(board[i] > 10000)
					board[i] -= 10000;
			System.out.println("10000 빼기");
			jlScore.setText(String.valueOf(score));
			System.out.println(move);
			if(gameoverChk == 0 && move == 1) {
				makeGame();
				System.out.println("makeGame호출 완료");
				check_1024(st, tag);
				System.out.println("check_1024 호출 완료");
				checkGameOver(st, tag);
				System.out.println("checkGameOver 호출 완료");
				BoardReset();
				System.out.println("BoardReset");
			}
		}
	}
}

class FourbyFourBoard extends JFrame{
	JPanel jpBoard, jpScore;
	JMenuBar jmb;
	JMenu jmReset, jmHelp;
	JMenuItem mReset, mHelp;
	JLabel jlBoard[][] = new JLabel[4][4];
	JLabel jlScore;
	int gameoverChk = 0;
	int board[][] = new int[4][4];
	int side = 4;
	int score = 0;
	int fullChk=0;
	int cheatChk = 0;
	
	public FourbyFourBoard(Statement stmt, int tag) {
		super("4 * 4");
		getContentPane().setLayout(new BorderLayout());
		jmb = new JMenuBar();
		
		jmReset = new JMenu("Reset(R)");
		jmHelp = new JMenu("Help(H)");
		
		mReset = new JMenuItem("Reset");
		mHelp = new JMenuItem("Help");
		
		jmReset.add(mReset);
		
		jmHelp.add(mHelp);
		jmb.add(jmReset);
		jmb.add(jmHelp);
		
		jpBoard = new JPanel(new GridLayout(4, 4, 2, 2));
		jpBoard.setBackground(Color.GRAY);
		jpScore = new JPanel(new GridLayout(1, 3, 2, 2));
		jpScore.setBackground(Color.WHITE);
		System.out.println("패털 만들기 성공");
		
		jlScore = new JLabel(String.valueOf(score), JLabel.CENTER);
		jlScore.setBackground(Color.WHITE);
		jlScore.setFont(new Font("굴림", Font.BOLD, 50));
		jpScore.add(jlScore);
		System.out.println("jlScore 만듦");
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				jlBoard[i][j] = new JLabel("", JLabel.CENTER);
				jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
				jlBoard[i][j].setOpaque(true);
				jpBoard.add(jlBoard[i][j]);
				board[i][j] = 0;
			}
		}
		
		System.out.println("보드 만들기 성공");
		
		jmReset.setMnemonic('R');
		jmHelp.setMnemonic('H');
		
		mReset.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
		mHelp.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_MASK));
		
		mReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newGame();
			}
		}); //기능 추가하기
		mHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new Helper();
			}
		}); //기능 추가하기
		
		System.out.println("매뉴만들기 성공");
		
		getContentPane().add("North", jpScore);
		getContentPane().add("Center", jpBoard);
		System.out.println("패널 넣기 성공");
		setJMenuBar(jmb);//이거 작동 안함
		setSize(500, 500);
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setVisible(true);
		makeGame();
		makeGame();
		System.out.println("처음 makeGame호출 두번 완료");
		BoardReset();
		System.out.println("BoardReset 호출 완료");
		if(gameoverChk == 0)
			addKeyListener(new keyboardAction(stmt, tag));
	}
	public void newGame() {
		int i, j;
		
		for(i = 0; i < side; i++) {
			for(j = 0; j< side; j++)
				board[i][j] = 0;
		}
		
		score = 0;
		gameoverChk = 0;
		jlScore.setText(String.valueOf(score));
		makeGame();
		makeGame();
		BoardReset();
	}
	public void BoardReset() {
		for(int i = 0; i < side; i++) {
			for(int j = 0; j < side;j++) {
				if(board[i][j] == 0) {
					jlBoard[i][j].setText("");
					jlBoard[i][j].setBackground(new Color(205, 193, 180));
					jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
				}
				else {
					jlBoard[i][j].setText(String.valueOf(board[i][j]));
					switch(board[i][j]) {
					case 2:
						jlBoard[i][j].setBackground(new Color(238, 228, 218));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
						break;
					case 4:
						jlBoard[i][j].setBackground(new Color(237, 224, 200));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
						break;
					case 8:
						jlBoard[i][j].setBackground(new Color(242, 177, 121));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
						break;
					case 16:
						jlBoard[i][j].setBackground(new Color(245, 149, 99));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
						break;
					case 32:
						jlBoard[i][j].setBackground(new Color(246, 124, 95));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
						break;
					case 64:
						jlBoard[i][j].setBackground(new Color(246, 94, 59));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
						break;
					case 1024:
					case 2048:
						jlBoard[i][j].setBackground(new Color (237,207, 114));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 30));
						break;
						
					default:
						jlBoard[i][j].setBackground(new Color(237, 207, 114));
					}
				}
				//System.out.printf("보드 [%d][%d]칸 초기화 완료\n", i, j);
			}
		}
	}
	
	public void makeGame() { // 이거 작동하다가 뻑남
		Random random = new Random();
		int pos = random.nextInt(side * side);
		int row, col;
		do {
			pos = (pos + 1) % (side * side);
			row = pos / side;
			col = pos % side;
		}while(board[row][col] != 0); // 여기서 무한루프 들어가는 듯
		int val = random.nextInt(100) > 80 ? 4 : 2;
		if(cheatChk==10)
			board[row][col] = 2048;
		else
			board[row][col] = val;
		System.out.println(pos);
	}
	
	public void checkGameOver(Statement stmt, int tag) {
		StringBuilder sb = new StringBuilder();
		String sql;
		int i, j;
		
		for(i = 0; i <side; i++)
			for(j = 0; j < side; j++)
				if(board[i][j] == 0)
					return;
		
		for(i = 0; i < side-1; i++)
			for(j = 0; j < side-1; j++)
				if(board[i][j] == board[i+1][j] || board[i][j] == board[i][j+1])
					return;
		
		for(i = 0; i < side-1; i++)
			if(board[i][side-1] == board[i+1][side-1])
				return;
		for(j = 0; j< side-1; j++)
			if(board[side-1][j] == board[side-1][j+1])
				return;
		
		try {
			gameoverChk = 1;
			sql = sb.append("insert into result values('")
					 .append(tag + "', '")
					 .append(jlScore.getText() + "', ")
					 .append("0, '" + side + "*" + side + "');")
					 .toString();
			stmt.executeUpdate(sql);
			jlScore.setText("Game Over");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void check_1024(Statement stmt, int tag) {
		StringBuilder sb = new StringBuilder();
		int i, j;
		String sql;
		
		for(i = 0; i < side; i++) {
			for(j =0; j<side; j++) {
				if(board[i][j] == 2048) {
					gameoverChk = 1;
					try {
						sql = sb.append("insert into result values('")
								.append(tag + "', '")
								.append(jlScore.getText() + "', ")
								.append("1, '" + side + "*" + side +"');")
								.toString();
						stmt.executeUpdate(sql);
						jlScore.setText("Winner Winner Chicken Dinner!");
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	class keyboardAction extends KeyAdapter{
		Statement st;
		//ResultSet resultSet;
		int tag;
		keyboardAction(Statement stmt, int tag){
			st = stmt;
			this.tag = tag;

		}
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			int chk = -1, i, j, move = 0;
			
			if(gameoverChk == 1)
				return;
			
			switch(key) {
			case KeyEvent.VK_UP:
				if(cheatChk == 0 || cheatChk == 1)
					cheatChk++;
				else
					cheatChk = 0;
				for(;chk != 0;) {
					chk = 0;
					for(i = 1; i <= 3; i++) {
						for(j = 0; j < side; j++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i-1][j] != 0 && board[i-1][j] != board[i][j]) continue;
							if(board[i-1][j] == 0) board[i-1][j] = board[i][j];
							else if(board[i][j] < 10000 && board[i][j] == board[i-1][j]) {
								board[i-1][j] *= 2;
								board[i-1][j] += 10000;
								score += 2*(board[i][j]);
							}
							board[i][j] = 0;
							chk++;
							move++;
						}
					}
				}
				System.out.println("위");
				break;
				
			case KeyEvent.VK_DOWN:
				if(cheatChk == 2 || cheatChk == 3)
					cheatChk++;
				else
					cheatChk = 0;
				for(;chk != 0;) {
					chk = 0;
					for(i = 2; i >= 0; i--) {
						for(j = 0; j < side; j++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i+1][j] != 0 && board[i+1][j] != board[i][j]) continue;
							if(board[i+1][j] == 0) board[i+1][j] = board[i][j];
							else if(board[i][j] < 10000 && board[i][j] == board[i+1][j]) {
								board[i+1][j] *= 2;
								board[i+1][j] += 10000;
								score += 2 * (board[i][j]);
							}
							board[i][j] = 0;
							chk++;
							move++;
						}
					}
				}
				System.out.println("아래");
				break;
			case KeyEvent.VK_LEFT:
				if(cheatChk == 4 || cheatChk == 6)
					cheatChk++;
				else
					cheatChk = 0;
				for(;chk != 0;) {
					chk = 0;
					for(i = 0; i < side; i++) {
						for(j = 1; j <= 3; j++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i][j-1] != 0 && board[i][j-1] != board[i][j]) continue;
							if(board[i][j-1] == 0) board[i][j-1] = board[i][j];
							else if(board[i][j] == board[i][j-1]) {
								board[i][j-1] *= 2;
								board[i][j-1] += 10000;
								score += 2 * (board[i][j]);
							}
							board[i][j] = 0;
							move++;
							chk++;
						}
					}
				}
				System.out.println("왼쪽");
				break;
			case KeyEvent.VK_RIGHT:
				if(cheatChk == 5 || cheatChk == 7)
					cheatChk++;
				else
					cheatChk = 0;
				for(;chk != 0;) {
					chk = 0;
					for(j = 2; j >= 0; j--) {
						for(i = 0; i < side; i++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i][j+1] != 0 && board[i][j+1] != board[i][j]) continue;
							if(board[i][j+1] == 0) board[i][j+1] = board[i][j];
							else if(board[i][j] < 10000 && board[i][j] == board[i][j+1]) {
								board[i][j+1] *= 2;
								board[i][j+1] += 10000;
								score += 2 * (board[i][j]);
							}
							board[i][j] = 0;
							chk++;
							move++;
						}
					}
				}
				System.out.println("오른쪽");
				break;
			case KeyEvent.VK_A:
				if(cheatChk == 9) {
					cheatChk++;
					move++;
				}
				else
					cheatChk = 0;
				break;
			case KeyEvent.VK_B:
				if(cheatChk == 8) {
					cheatChk++;
					move++;
				}
				else
					cheatChk = 0;
				break;
			}
			
			for(i = 0; i < side; i++) {
				for(j = 0; j < side; j++) {
					if(board[i][j] > 10000)
						board[i][j] -= 10000;
				}
			}
			System.out.println("10000 빼기");
			jlScore.setText(String.valueOf(score));
			System.out.println(move);
			if(gameoverChk == 0 && move != 0) {
				makeGame();
				System.out.println("makeGame호출 완료");
				check_1024(st, tag);
				System.out.println("check_1024 호출 완료");
				checkGameOver(st, tag);
				System.out.println("checkGameOver 호출 완료");
				BoardReset();
				System.out.println("BoardReset");
			}
			System.out.println(cheatChk);
		}
	}
}

class FivebyFiveBoard extends JFrame{
	JPanel jpBoard, jpScore;
	JMenuBar jmb;
	JMenu jmReset, jmHelp;
	JMenuItem mReset, mHelp;
	JLabel jlBoard[][] = new JLabel[5][5];
	JLabel jlScore;
	int gameoverChk = 0;
	int board[][] = new int[5][5];
	int side = 5;
	int score = 0;
	int fullChk=0;
	int cheatchk = 0;
	
	public FivebyFiveBoard(Statement stmt, int tag) {
		super("5 * 5");
		getContentPane().setLayout(new BorderLayout());
		jmb = new JMenuBar();
		
		jmReset = new JMenu("Reset(R)");
		jmHelp = new JMenu("Help(H)");
		
		mReset = new JMenuItem("Reset");
		mHelp = new JMenuItem("Help");
		
		jmReset.add(mReset);
		
		jmHelp.add(mHelp);
		jmb.add(jmReset);
		jmb.add(jmHelp);
		
		jpBoard = new JPanel(new GridLayout(5, 5, 2, 2));
		jpBoard.setBackground(Color.GRAY);
		jpScore = new JPanel(new GridLayout(1, 3, 2, 2));
		jpScore.setBackground(Color.WHITE);
		System.out.println("패털 만들기 성공");
		
		jlScore = new JLabel(String.valueOf(score), JLabel.CENTER);
		jlScore.setBackground(Color.WHITE);
		jlScore.setFont(new Font("굴림", Font.BOLD, 50));
		jpScore.add(jlScore);
		System.out.println("jlScore 만듦");
		
		for(int i = 0; i < side; i++) {
			for(int j = 0; j < side; j++) {
				jlBoard[i][j] = new JLabel("", JLabel.CENTER);
				jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
				jlBoard[i][j].setOpaque(true);
				jpBoard.add(jlBoard[i][j]);
				board[i][j] = 0;
			}
		}
		
		System.out.println("보드 만들기 성공");
		
		jmReset.setMnemonic('R');
		jmHelp.setMnemonic('H');
		
		mReset.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
		mHelp.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_MASK));
		
		mReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newGame();
			}
		}); //기능 추가하기
		mHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new Helper();
			}
		}); //기능 추가하기
		
		System.out.println("매뉴만들기 성공");
		
		getContentPane().add("North", jpScore);
		getContentPane().add("Center", jpBoard);
		System.out.println("패널 넣기 성공");
		setJMenuBar(jmb);//이거 작동 안함
		setSize(500, 500);
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setVisible(true);
		makeGame();
		makeGame();
		System.out.println("처음 makeGame호출 두번 완료");
		BoardReset();
		System.out.println("BoardReset 호출 완료");
		if(gameoverChk == 0)
			addKeyListener(new keyboardAction(stmt, tag));
	}
	public void newGame() {
		int i, j;
		
		for(i = 0; i < side; i++) {
			for(j = 0; j< side; j++)
				board[i][j] = 0;
		}
		
		score = 0;
		gameoverChk = 0;
		jlScore.setText(String.valueOf(score));
		makeGame();
		makeGame();
		BoardReset();
	}
	public void BoardReset() {
		for(int i = 0; i < side; i++) {
			for(int j = 0; j < side;j++) {
				if(board[i][j] == 0) {
					jlBoard[i][j].setText("");
					jlBoard[i][j].setBackground(new Color(205, 193, 180));
					jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
				}
				else {
					jlBoard[i][j].setText(String.valueOf(board[i][j]));
					jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 50));
					switch(board[i][j]) {
					case 2:
						jlBoard[i][j].setBackground(new Color(238, 228, 218));
						break;
					case 4:
						jlBoard[i][j].setBackground(new Color(237, 224, 200));
						break;
					case 8:
						jlBoard[i][j].setBackground(new Color(242, 177, 121));
						break;
					case 16:
						jlBoard[i][j].setBackground(new Color(245, 149, 99));
						break;
					case 32:
						jlBoard[i][j].setBackground(new Color(246, 124, 95));
						break;
					case 64:
						jlBoard[i][j].setBackground(new Color(246, 94, 59));
						break;
					case 128:
					case 256:
					case 512:
						jlBoard[i][j].setBackground(new Color (237,207, 114));
						break;
					default:
						jlBoard[i][j].setBackground(new Color(237, 207, 114));
						jlBoard[i][j].setFont(new Font("굴림", Font.BOLD, 30));
					}
				}
				//System.out.printf("보드 [%d][%d]칸 초기화 완료\n", i, j);
			}
		}
	}
	
	public void makeGame() { // 이거 작동하다가 뻑남
		Random random = new Random();
		int pos = random.nextInt(side * side);
		int row, col;
		do {
			pos = (pos + 1) % (side * side);
			row = pos / side;
			col = pos % side;
		}while(board[row][col] != 0); // 여기서 무한루프 들어가는 듯
		int val = random.nextInt(100) > 80 ? 4 : 2;
		board[row][col] = val;
		System.out.println(pos);
	}
	
	public void checkGameOver(Statement stmt, int tag) {
		StringBuilder sb = new StringBuilder();
		String sql;
		int i, j;
		
		for(i = 0; i <side; i++)
			for(j = 0; j < side; j++)
				if(board[i][j] == 0)
					return;
		
		for(i = 0; i < side-1; i++)
			for(j = 0; j < side-1; j++)
				if(board[i][j] == board[i+1][j] || board[i][j] == board[i][j+1])
					return;
		
		for(i = 0; i < side-1; i++)
			if(board[i][side-1] == board[i+1][side-1])
				return;
		for(j = 0; j< side-1; j++)
			if(board[side-1][j] == board[side-1][j+1])
				return;
		
		try {
			gameoverChk = 1;
			sql = sb.append("insert into result values('")
					 .append(tag + "', '")
					 .append(jlScore.getText() + "', ")
					 .append("1, '" + side + "*" + side + "');")
					 .toString();
			stmt.executeUpdate(sql);
			jlScore.setText("Game Over");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public void check_1024(Statement stmt, int tag) {
		StringBuilder sb = new StringBuilder();
		int i, j;
		String sql;
		
		for(i = 0; i < side; i++) {
			for(j =0; j<side; j++) {
				if(board[i][j] == 2048) {
					gameoverChk = 1;
					try {
						sql = sb.append("insert into result values('")
								.append(tag + "', '")
								.append(jlScore.getText() + "', ")
								.append("1, '" + side + "*" + side +"');")
								.toString();
						stmt.executeUpdate(sql);
						jlScore.setText("Winner Winner Chicken Dinner!");
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}*/
	
	class keyboardAction extends KeyAdapter{
		Statement st;
		//ResultSet resultSet;
		int tag;
		keyboardAction(Statement stmt, int tag){
			st = stmt;
			this.tag = tag;

		}
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			int chk = -1, i, j, move = 0;
			
			if(gameoverChk == 1)
				return;
			
			switch(key) {
			case KeyEvent.VK_UP:
				for(;chk != 0;) {
					chk = 0;
					for(i = 1; i <= 4; i++) {
						for(j = 0; j < side; j++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i-1][j] != 0 && board[i-1][j] != board[i][j]) continue;
							if(board[i-1][j] == 0) board[i-1][j] = board[i][j];
							else if(board[i][j] < 10000 && board[i][j] == board[i-1][j]) {
								board[i-1][j] *= 2;
								board[i-1][j] += 10000;
								score += 2*(board[i][j]);
							}
							board[i][j] = 0;
							chk++;
							move++;
						}
					}
				}
				System.out.println("위");
				break;
				
			case KeyEvent.VK_DOWN:
				for(;chk != 0;) {
					chk = 0;
					for(i = 3; i >= 0; i--) {
						for(j = 0; j < side; j++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i+1][j] != 0 && board[i+1][j] != board[i][j]) continue;
							if(board[i+1][j] == 0) board[i+1][j] = board[i][j];
							else if(board[i][j] < 10000 && board[i][j] == board[i+1][j]) {
								board[i+1][j] *= 2;
								board[i+1][j] += 10000;
								score += 2 * (board[i][j]);
							}
							board[i][j] = 0;
							chk++;
							move++;
						}
					}
				}
				System.out.println("아래");
				break;
			case KeyEvent.VK_LEFT:
				for(;chk != 0;) {
					chk = 0;
					for(i = 0; i < side; i++) {
						for(j = 1; j <= 4; j++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i][j-1] != 0 && board[i][j-1] != board[i][j]) continue;
							if(board[i][j-1] == 0) board[i][j-1] = board[i][j];
							else if(board[i][j] == board[i][j-1]) {
								board[i][j-1] *= 2;
								board[i][j-1] += 10000;
								score += 2 * (board[i][j]);
							}
							board[i][j] = 0;
							move++;
							chk++;
						}
					}
				}
				System.out.println("왼쪽");
				break;
			case KeyEvent.VK_RIGHT:
				for(;chk != 0;) {
					chk = 0;
					for(j = 3; j >= 0; j--) {
						for(i = 0; i < side; i++) {
							if(board[i][j] == 0 || board[i][j] > 10000) continue;
							if(board[i][j+1] != 0 && board[i][j+1] != board[i][j]) continue;
							if(board[i][j+1] == 0) board[i][j+1] = board[i][j];
							else if(board[i][j] < 10000 && board[i][j] == board[i][j+1]) {
								board[i][j+1] *= 2;
								board[i][j+1] += 10000;
								score += 2 * (board[i][j]);
							}
							board[i][j] = 0;
							chk++;
							move++;
						}
					}
				}
				System.out.println("오른쪽");
				break;
			}
			
			for(i = 0; i < side; i++) {
				for(j = 0; j < side; j++) {
					if(board[i][j] > 10000)
						board[i][j] -= 10000;
				}
			}
			System.out.println("10000 빼기");
			jlScore.setText(String.valueOf(score));
			System.out.println(move);
			if(gameoverChk == 0 && move != 0) {
				makeGame();
				System.out.println("makeGame호출 완료");
				//check_1024(st, tag);
				//System.out.println("check_1024 호출 완료");
				checkGameOver(st, tag);
				System.out.println("checkGameOver 호출 완료");
				BoardReset();
				System.out.println("BoardReset");
			}
		}
	}
}

public class Game {
       public static void main(String arg[]) throws SQLException {
             new LogIn();
       }
}