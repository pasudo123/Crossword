package BattleWord;

import BattleWord.GameFrame_MainFrame;
import BattleWord.Connect;	//mysql의 문제 설명 내용을 얻어내기 위함

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.*;

// 십자말풀이 단어의 설명이 나타나는 Panel
class TextExPanel extends JPanel implements KeyListener, MouseListener{
	private MySqlConnect msc;
	private MainFrame mf;
	private JLabel jExplain;		// 문제의 설명
	private JTextField jtField;		// 정답을 입력
	private JButton JCompleteBtn[];	// 정답을 누르고 확인하는 버튼
	
	// [게임 설명] 및  [게임번호] 및  [게임 정답] 및  [게임 정답 길이]
	private String gameText = "";
	private int gameNum = 0;
	private String gameValue = "";
	private int wordLen = 0;
	
	// [버튼 객체] 및  [버튼 인덱스 i] 및  [버튼 인덱스 j]
	private SeqButton seqButton[][];
	private int Button_i;
	private int Button_j;
	
	private Font Textfont = new Font("나눔바른고딕", Font.BOLD, 25);
	private JLabel textLabel[];		// 글자만 보여지는 레이블
	private JLabel inputLabel;
	private String input = "";		// JTextField로 부터 입력받은 값들을 실시간으로 레이블에 부착
	
	// 타이머 패널과 텍스트 입력 패널은 서로 연결
	private TimePanel tmPanel;
	private boolean winCheck = false;		// 모든 문제를 푸는 경우 클리어 하기 위한 변수
	
	
	// 기본 생성자 및 매개변수 있는 생성자
	public TextExPanel(){}
	public TextExPanel(MainFrame mf){
		this.mf = mf;
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		this.settingTimePanel();
		this.settingLabel();
		this.settingField();	//	JTextField 설정
		this.settingButton();	//	JButton 설정
		this.settingMySQL();	// 	MySQL 셋팅
		
		// 최종적으로 결합 // 
		this.setSize(600, 300);
		this.setLocation(0, 700);
		this.mf.add(this);
	}
	
	
	public void settingTimePanel(){
		this.tmPanel = this.mf.getTimePanel();
	}
	
	//JLabel 설정, 투명하게 하여 글자만 보이도록 설정한다.
	public void settingLabel(){
		this.jExplain = new JLabel(new ImageIcon("Images/BattleWord/TextExplain.jpg"));
		this.jExplain.setHorizontalAlignment(SwingConstants.CENTER);
		this.jExplain.setVerticalAlignment(SwingConstants.CENTER);
		this.jExplain.setSize(600, 185);
		this.jExplain.setLocation(0, 0);
		
		this.textLabel = new JLabel[3];
		
		for (int i = 0; i < 3; i++) {
			this.textLabel[i] = new JLabel();
			this.textLabel[i].setSize(600, 185);
			
			if (i == 0)
				this.textLabel[0].setLocation(0, -60);
			if (i == 1)
				this.textLabel[1].setLocation(0, 0);
			if (i == 2)
				this.textLabel[2].setLocation(0, 60);
			
			this.textLabel[i].setOpaque(false);
			this.textLabel[i].setFont(this.Textfont);
			this.add(this.textLabel[i]);
		}
		this.add(this.jExplain);
	}
	
	//JTextField 설정
	public void settingField(){
		this.inputLabel = new JLabel(new ImageIcon("images/BattleWord/inputText.png"));
		inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputLabel.setSize(400, 81);
		inputLabel.setLocation(0, 185);
		
		jtField = new JTextField();
		jtField.setFont(new Font("나눔바른고딕", Font.BOLD, 35));// 글자크기 설정
		jtField.setHorizontalAlignment(SwingConstants.CENTER);
		jtField.setText("우리말 낱말 퍼즐"); // 확인용
		jtField.setSize(379, 60);
		jtField.setLocation(10, 195);
		
		jtField.addKeyListener(this);	// JTextField 리스너 등록
		
		this.add(inputLabel);
		this.add(jtField);
	}
	
	//JButton 설정 및 리스너 등록
	public void settingButton(){
		JCompleteBtn = new JButton[2];
		JCompleteBtn[0] = new JButton(new ImageIcon("Images/BattleWord/Enter_before.png"));
		JCompleteBtn[1] = new JButton(new ImageIcon("Images/BattleWord/Enter_after.png"));
		for(int i = 0; i < JCompleteBtn.length; i++){
			JCompleteBtn[i].setSize(200, 81);
			JCompleteBtn[i].setLocation(400, 185);
			if(i == 1)
				JCompleteBtn[i].setVisible(false);
			
			this.JCompleteBtn[i].addMouseListener(this);
			this.add(JCompleteBtn[i]);
		}
	}
	
	// MySQL 객체생성, 셋팅, 연결
	public void settingMySQL(){
		this.msc = new MySqlConnect();
		this.msc.setting();
		this.msc.Connecting();
	}
	
	// 게임의 설명과 정답 그리고 정답의 길이를 설정한다.
	// wordSeq 클래스에서 MainFrame의 호출을 통해 전달 받는다.
	public void setAreaAndAns(int gameType, int gameNum, int wordLen){
		//System.out.println("TextEx class, PuzzleType  >>  " + gameType);
		
		
		/******************* FILE INPUT OUTPUT **********************/
		this.gameText = this.msc.getFILE_GameEx(gameType, gameNum);
		this.gameValue = this.msc.getFILE_GameAns(gameType, gameNum);
		/******************* FILE INPUT OUTPUT **********************/
		
		
//		this.gameText = this.msc.getGameEx(gameType, gameNum);	//  게임 설명
//		this.gameValue = this.msc.getGameAns(gameType, gameNum);//	게임 정답
		this.gameNum = gameNum;									//	게임 번호
		this.wordLen = wordLen;									//	정답 길이
		
//		System.out.println("설명 길이 : " + gameText.length());
		
		// 게임의 설명이 나오는 부분을 한줄 두줄 세줄로 구분하여 보이게 한다.
		// 한줄만
		if(gameText.length() < 34){
			this.gameText = " >> " + this.gameText;
			this.textLabel[0].setText("");
			this.textLabel[1].setText(this.gameText);
			this.textLabel[2].setText("");
		}
		// 두줄
		else if(gameText.length() < 62){
			this.gameText = " >> " + this.gameText;
			String Text1 = this.gameText.substring(0, 34);
			String Text2 = "    " + this.gameText.substring(34, gameText.length());
			
			this.textLabel[0].setText(Text1);
			this.textLabel[1].setText(Text2);
			this.textLabel[2].setText("");
		}
		// 세줄
		else{
			this.gameText = " >> " + this.gameText;
			String Text1 = this.gameText.substring(0, 34);
			String Text2 = "    " + this.gameText.substring(34, 63);
			String Text3 = "    " + this.gameText.substring(63,gameText.length());
			
			this.textLabel[0].setText(Text1);
			this.textLabel[1].setText(Text2);
			this.textLabel[2].setText(Text3);
		}
		
		this.inputLabel.setText("");
		this.jtField.setText("");		// 	클릭되고 나서 JTextField을 공백으로 표시
		this.jtField.requestFocus();	//	강제로 포커스를 지정해서 마우스로 클릭하지 않아도 커서는 계속 깜박이도록..
		
	}
	
	// 버튼의 객체, 인덱스 값을 전달받는 메소드
	// wordSeq 클래스에서 MainFrame로의 전달 그리고 MainFrame 클래스에서 TextEx 클래스로의 전달
	public void setBtnObject(SeqButton[][] seqButton, int i, int j){
		this.seqButton = seqButton;
		this.Button_i = i;
		this.Button_j = j;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		String resultText = "";
		String splitChar[];
		int charIdx = 0;
		String soundFileURL = "src/WordSource/BOOM.wav";
		
		// JTextField에서 Enter키가 입력되면 JTextField의 Text 값을 다른 변수에 저장
		// 그 변수와 설명에 나타낸 값, 즉 DB에 저장된 값과 동일한지 확인하고 동일하다면 JButton의 색상을 변경
		// 그리고 색상이 변경된 JButton들은 낱말의 한 음절씩 보이도록 설정 그 후 JTextField 초기화
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// 모두 정답을 맞춘 경우가 아닌 경우에만
			if(!winCheck){
				resultText = this.jtField.getText().toString();
				
				this.JCompleteBtn[0].setVisible(false);	//	버튼이 엔터로 색상 변경이 되게끔 설정
				this.JCompleteBtn[1].setVisible(true);
				
				
				if (this.gameValue.equals(resultText)) {
					// JButton에 글자의 내용물이 보이도록 설정하며, 점수를 제공한다.
					
					// 정답인 경우 음원파일 재생
					this.Sound(soundFileURL, false);
					
//					System.out.println("점수 >> " + wordLen);
					this.mf.sendScore(wordLen);
					// 정답인 경우 메인프레임에 나타나는 효과를 설정하기 위해서 메소드 실행

					// 연결된 타임 패널의 분과 초가 00일 경우 TimeOver 출력
					// 아닌경우 정답처리
					if (this.tmPanel.min.equals("0") && this.tmPanel.sec.equals("00")) {
						this.mf.judgeMethod("TimeOver");
					}
					else{
						this.mf.judgeMethod("RightAns");
					}
					
					splitChar = new String[this.wordLen];
					
					for (int c = 0; c < splitChar.length; c++) {
						splitChar[c] = this.gameValue.substring(c, c+1);
					}
	
					// 게임번호가 짝수 세로이며 세로의 존재여부 불린타입이 true과 게임의 번호가 마우스 리스너가 클릭한 번호와 동일하면
					// 낱말의 길이만큼 JButton 글자 설정 및 색상 변경,
					// 게임의 번호는 풀었으니 "-1"로 설정하고 세로 낱말의 존재 여부는 false; setEditable(false)
					if (this.gameNum % 2 == 0) {
						for (int i = 0; i < 7; i++) {
							if (seqButton[i][this.Button_j].height && seqButton[i][this.Button_j].gameNum_H == gameNum) {
								for (int j = i; j < i + wordLen; j++) {
									seqButton[j][this.Button_j].gameNum_H = -1;
									seqButton[j][this.Button_j].height = false;
									seqButton[j][this.Button_j].ownerStringValue = splitChar[charIdx++];	//	버튼에 정답의 음절을 설정
									seqButton[j][this.Button_j].drawingAtButton = true;						//	이미지 버튼 정답 여부 TRUE
									seqButton[j][this.Button_j].setImage(new ImageIcon("Images/BattleWord/BLOCK_2.png"));	// 정답인 경우 블럭의 색상 변경
									//seqButton[j][this.Button_j].setClickAble(false);
								}
								break;
							}
						}
					}
					// 게임번호가 홀수 가로
					else {
						for (int i = 0; i < 7; i++) {
							if (seqButton[this.Button_i][i].width && seqButton[this.Button_i][i].gameNum_W == gameNum) {
								for (int j = i; j < i + wordLen; j++) {
									seqButton[this.Button_i][j].gameNum_W = -1;
									seqButton[this.Button_i][j].width = false;
									seqButton[this.Button_i][j].ownerStringValue = splitChar[charIdx++];	//	버튼에 정답의 음절을 설정
									seqButton[this.Button_i][j].drawingAtButton = true;						//	이미지 버튼 정답 여부 TRUE
									seqButton[this.Button_i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_2.png"));	// 정답인 경우 블럭의 색상 변경
									//seqButton[this.Button_i][j].setClickAble(false);
								}
								break;
							}
						}
					}
					charIdx = 0;
					System.out.println("정답 : " + resultText);

					// 여기서 정답이 모두 맞추어졌는데 아닌지를 검사하는 체크함수를 설정하여 놓는다.
					// 바로 이거
					this.checkBlock();

				} // 큰 if( 맞춘경우 )
				else {
					// 오답인 경우 메인프레임에 나타나는 효과를 설정하기 위해서 메소드 실행
					// MainFrame이 흔들리도록 설정
					if (this.tmPanel.min.equals("0") && this.tmPanel.sec.equals("00")) {
						this.mf.judgeMethod("TimeOver");
					} else {
						System.out.println("틀렸습니다.");
						this.mf.judgeMethod("WrongAns");
					}
				}
			}
			// System.out.println(resultText);
			this.jtField.setText("");
		}
	}

	// 엔터를 떼었을 때
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.JCompleteBtn[0].setVisible(true); // 버튼이 엔터로 색상 변경이 되게끔 설정
			this.JCompleteBtn[1].setVisible(false);
		}
	}
	
	// 버튼을 눌렀을때 처리
	// KeyEvent와 동일한 처리
	@Override
	public void mousePressed(MouseEvent e) {
		String resultText = "";
		String splitChar[];
		int charIdx = 0;
		
		if(!winCheck){
			resultText = this.jtField.getText().toString();
			
			this.JCompleteBtn[0].setVisible(false);
			this.JCompleteBtn[1].setVisible(true);
			
			if (this.gameValue.equals(resultText)) {
				// JButton에 글자의 내용물이 보이도록 설정하며, 점수를 제공한다.
				// 정답인 경우 메인프레임에 나타나는 효과를 설정하기 위해서 메소드 실행
				
				// 연결된 타임 패널의 분과 초가 00일 경우 TimeOver 출력
				// 아닌경우 정답처리
				if(this.tmPanel.min.equals("0") && this.tmPanel.sec.equals("00")){
					this.mf.judgeMethod("TimeOver");
				}
				else{
					this.mf.judgeMethod("RightAns");
				}
				
				splitChar = new String[this.wordLen];
				
				for (int c = 0; c < splitChar.length; c++) {
					splitChar[c] = this.gameValue.substring(c, c+1);
				}
		
				// 게임번호가 짝수 세로이며 세로의 존재여부 불린타입이 true과 게임의 번호가 마우스 리스너가 클릭한 번호와 동일하면
				// 낱말의 길이만큼 JButton 글자 설정 및 색상 변경,
				// 게임의 번호는 풀었으니 "-1"로 설정하고 세로 낱말의 존재 여부는 false; setEditable(false)
				if (this.gameNum % 2 == 0) {
					for (int i = 0; i < 7; i++) {
						if (seqButton[i][this.Button_j].height && seqButton[i][this.Button_j].gameNum_H == gameNum) {
							for (int j = i; j < i + wordLen; j++) {
								seqButton[j][this.Button_j].gameNum_H = -1;
								seqButton[j][this.Button_j].height = false;
								seqButton[j][this.Button_j].ownerStringValue = splitChar[charIdx++];	//	버튼에 정답의 음절을 설정
								seqButton[j][this.Button_j].drawingAtButton = true;						//	이미지 버튼 정답 여부 TRUE
								seqButton[j][this.Button_j].setImage(new ImageIcon("Images/BattleWord/BLOCK_2.png"));	// 정답인 경우 블럭의 색상 변경
								//seqButton[j][this.Button_j].setClickAble(false);
							}
							break;
						}
					}
				}
				
				// 게임번호가 홀수 가로
				else{
					for (int i = 0; i < 7; i++) {
						if (seqButton[this.Button_i][i].width && seqButton[this.Button_i][i].gameNum_W == gameNum) {
							for (int j = i; j < i + wordLen; j++) {
								seqButton[this.Button_i][j].gameNum_W = -1;
								seqButton[this.Button_i][j].width = false;
								seqButton[this.Button_i][j].ownerStringValue = splitChar[charIdx++];	//	버튼에 정답의 음절을 설정
								seqButton[this.Button_i][j].drawingAtButton = true;						//	이미지 버튼 정답 여부 TRUE
								seqButton[this.Button_i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_2.png"));	// 정답인 경우 블럭의 색상 변경
								//seqButton[this.Button_i][j].setClickAble(false);
							}
							break;
						}
					}
				}
				charIdx = 0;
				System.out.println("정답 : " + resultText);

				// 십자말풀이 클리어여부
				this.checkBlock();
			} // 큰 if( 맞춘경우 )
			else {
				// 오답인 경우 메인프레임에 나타나는 효과를 설정하기 위해서 메소드 실행
				// MainFrame이 흔들리도록 설정
				if (this.tmPanel.min.equals("0") && this.tmPanel.sec.equals("00")) {
					this.mf.judgeMethod("TimeOver");
				} else {
					this.mf.judgeMethod("WrongAns");
				}
				System.out.println("틀렸습니다.");
			}
		}
		// System.out.println(resultText);
		this.jtField.setText("");
	}

	// 버튼을 떼었을때 처리
	@Override
	public void mouseReleased(MouseEvent e) {
		this.JCompleteBtn[0].setVisible(true);
		this.JCompleteBtn[1].setVisible(false);
	}
		
	
	public void checkBlock(){
		boolean small_winCheck = true;
		
		for(int i = 0; i < 7; i++){
			for(int j = 0; j < 7; j++){
				// 문제가 남아있다면 break;
				if(!this.seqButton[i][j].drawingAtButton){
					small_winCheck = false;
					break;
				}
			}
			if(!small_winCheck)
				break;
		}
		
		// 문제를 다 풀었다.
		if(small_winCheck){
			// 타임 패널이 멈추게 하며, 
			// 낱말 퍼즐에는  CLEAR 라는 문자를 출력한다.
			this.textLabel[1].setText("  >>  고생하셨습니다.");
			this.tmPanel.winCheck = true;
			this.mf.judgeMethod("Perfect");
		}
		// 문제가 남아있다
		else{
			//System.out.println("Win Check : " + winCheck);
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	
	// 사운드 재생영 메소드
	// 사운드 파일을 받아들여 해당 사운드를 재생시킨다.
	public void Sound(String File, boolean Loop){
		Clip clip;
		
		try{
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(File)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			
			if(Loop)
				clip.loop(-1);
		}
		catch(Exception e){
			System.out.println("Sound ERROR >> ");
			System.err.println();
		}
	}
}

public class GameFrame_TextEx {
	public static void main(String[]args){}
}
