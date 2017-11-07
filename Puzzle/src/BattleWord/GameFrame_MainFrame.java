package BattleWord;

import BattleWord.GameFrame_wordSeq;
import BattleWord.GameFrame_logoTitle;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/*
 * 1. 윈도우 창 크기를 구해서 너비랑 높이를 각각 반으로 하여 두 개의 창 구현
 * */
class MainFrame extends JFrame {
	//private Connect mscConnect;		// DB 연동하는 클래스 
	private wordSeqPanel wsp; 			// 십자말풀이 게임패널
	private logoPanel lgp;				// 로고 패널
	private TimePanel tmp;				// 시간경과패널
	private TextExPanel tep;			// 십자말풀이 게임설명패널 
	private UserPanel uerP;				// 유저가 보이는 패널
	protected MainDialog mainDialog;	// 게임종료, 혹은 클리어시 보이는 다이얼로그
	
	private SeqButton seqButton[][];
	
	protected ImageIcon numIcon[];				// 숫자 폰트
	protected JudgeThread judgeThread;			// 정답의 판단에 따른 스레드 효과
	protected String judgeEffectStr = "Clear";	// 정답의 판단에 따른 효과 설정
	protected boolean bright = false;
	protected boolean PERFECT = false;			// 완전히 클리어한 경우
	
	protected int before_GameNum = -999;
	
	// 게임 시작 프레임에서 받은 캐릭터와, 사용자 이름을 부여받는다.
	// 받은 캐릭터는 3단계의 진화이기 때문에 각 낱말의 맞춘 점수로써, 
	// 3개짜리의 gif로 만들어서 설정(초기 - 중기 - 말기)
	protected String userID;						// 	사용자 이름
	protected String UNIT_TYPE;					// 	유닛 타입
	private JLabel userUNIT[] = new JLabel[3];	//	유닛이미지	
	
	/**********************************************************************************************/
	
	// 생성자
	public MainFrame(){
		this.setTitle("Main Frame");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		System.out.println("디폴트 생성자");
		this.setSize(900, 1000);
		this.setLayout(null);
		this.setLocationRelativeTo(getParent());//정중앙
		this.setResizable(false);//절대사이즈
		this.setFrame();
	}
	
	// 매개변수 있는 생성자 먼저 호출
	public MainFrame(String userID, String unitType){
		this();									// 디폴트 생성자 호출
//		System.out.println("매개변수 있는 생성자");	
		this.setNumberFont();
		this.setUser(userID, unitType);			// User의 정보를 설정	
		this.setVisible(true);					// 마지막으로 보이도록 설정
	}
	// 프레임을 셋팅
	public void setFrame(){
		//this.mscConnect = new Connect();
		this.tmp = new TimePanel(this);			// 시간경과 패널
		this.wsp = new wordSeqPanel(this);		// 게임십자말풀이 패널
		this.tep = new TextExPanel(this);		// 게임설명과 정답 패널
		this.uerP = new UserPanel(this);		// 유저가 보여지는 패널
		this.mainDialog = new MainDialog(this);	// 게임종료, 혹은 클리어시 보이는 다이얼로그
		
		this.lgp = new logoPanel(this);			// 로고 패널
	}
	
	public void setNumberFont(){
		numIcon = new ImageIcon[12];
		for(int i = 0; i < numIcon.length; i++){
			numIcon[i] = new ImageIcon("Images/numFont/" + Integer.toString(i) + ".png");
			if(i == 10)
				numIcon[i] = new ImageIcon("Images/numFont/00.png");
			if(i == 11)
				numIcon[i] = new ImageIcon("Images/numFont/clone.png");
		}
	}
	
	// 타임패널 객체 반환
	public TimePanel getTimePanel(){
		return tmp;
	}
	
	// 스레드를 셋팅
	public void setThread(){
		// 십자말풀이의 판정과 관련된 스레드 객체 생성(프레임과 십자말풀이 패널을 보낸다.)
		this.judgeThread = new JudgeThread(this, this.wsp);	
	}
	
	// wordSeq 클래스에서 받은 게임의 타입과 게임의 문제번호를 TextExPanel에 전달한다.
	// 그리고 클릭된 낱말들에 대해서는 색깔을 변경하여 준다.
	public void sendInfo(int gameType, int gameNum, int wordLen){
		// 처음 클릭 시
		if(this.before_GameNum == -999){
			this.before_GameNum = gameNum;
			
			// 세로 짝수
			if(gameNum % 2 == 0){
				for(int i = 0; i < 7; i++)
					for(int j = 0; j < 7; j++)
						if(this.seqButton[i][j].gameNum_H == gameNum)
							this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_999.png"));
			}
			// 홀수
			else{
				for(int i = 0; i < 7; i++)
					for(int j = 0; j < 7; j++)
						if(this.seqButton[i][j].gameNum_W == gameNum)
							this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_999.png"));
			}
			
		}
		// 두번째 클릭 시
		else{
			if(this.before_GameNum == gameNum){
				// 그대로
			}
			else{
				// 다르면 이전의 번호의 값들을 원래대로 원상복귀하는데, 
				// 그 번호의 가로나 혹은 세로가 정답으로 처리되어 있는 경우에는 그대로
				// 세로 짝수
				if(this.before_GameNum % 2 == 0){
					for(int i = 0; i < 7; i++)
						for(int j = 0; j < 7; j++){
							if(this.seqButton[i][j].gameNum_H == before_GameNum && !(this.seqButton[i][j].drawingAtButton))
								this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_1.png"));
							if(this.seqButton[i][j].gameNum_H == before_GameNum && this.seqButton[i][j].drawingAtButton)
								this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_2.png"));
						}
				}
				// 홀수
				else{
					for(int i = 0; i < 7; i++)
						for(int j = 0; j < 7; j++){
							if(this.seqButton[i][j].gameNum_W == before_GameNum && !(this.seqButton[i][j].drawingAtButton))
								this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_1.png"));
							if(this.seqButton[i][j].gameNum_W == before_GameNum && this.seqButton[i][j].drawingAtButton)
								this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_2.png"));
						}
				}
				
				// 세로 짝수
				if(gameNum % 2 == 0){
					for(int i = 0; i < 7; i++)
						for(int j = 0; j < 7; j++)
							if(this.seqButton[i][j].gameNum_H == gameNum)
								this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_999.png"));
				}
				// 홀수
				else{
					for(int i = 0; i < 7; i++)
						for(int j = 0; j < 7; j++)
							if(this.seqButton[i][j].gameNum_W == gameNum)
								this.seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_999.png"));
				}
				
				// 새로운 이전의 번호를 넣어준다.
				this.before_GameNum = gameNum;
			}
		}
		this.tep.setAreaAndAns(gameType, gameNum, wordLen);
	}
	
	// wordSeq 클래스에서 madePanel 메소드 실행 시 마지막 구문에 sendButtonArr로
	// 버튼배열들의 객체를 보내며, 프레임 생성 시 딱 한번 실행된다.
	public void sendButtonArr(SeqButton[][] seqButton){
		this.seqButton = seqButton;
	}
	
	// 버튼 속에서 좌표를 클릭한 값을 
	// 이벤트 처리시 지속적인 호출, SeqButton의 객체와 배열 인덱스의 값을 전달
	public void setButtonIndex(int i, int j){
		this.tep.setBtnObject(this.seqButton, i, j);
	}
	
	// TextEx의 클래스로부터 받은 판단에 따라서 Thread의 활동을 설정
	// 우선, 필드의 이펙트 효과가 무엇인지 값을 삽입
	// 스레드 객체를 계속 새로 생성하여, 판단의 유무를 설정한다.
	public synchronized void judgeMethod(String judge){
		this.judgeEffectStr = judge;	// 어떤 효과인지 값 삽입
		this.setThread();	// 스레드 객체 생성
		this.judgeThread.setJudge(judge);
		this.judgeThread.start();
	}
	
	
	// TextEx로부터 받은 점수를 캐릭터가 존재하는 패널로 보내는메소드
	// 이 메소드를 받을 때마다, 점수를 계속 갱신한다.
	public void sendScore(int score){
		this.uerP.ScoreValue = score;
		this.uerP.addScore();
	}
	
	// 게임 시작 프레임의 화면으로부터 받은 유저의 아이디와 유닛의 타입
	// 유저의 패널화면에 보여지도록 설정
	// 딱 한번 호출
	public void setUser(String userID, String unitType){
		this.userID = userID;
		this.UNIT_TYPE = unitType;
		
//		System.out.println("Main Frame :: setUser() 호출");
//		System.out.println(userID);
//		System.out.println(unitType);
		
		for(int i = 0; i < this.userUNIT.length; i++){
			userUNIT[i] = new JLabel();
		}
		
		// 유닛에 타입에 맞게 3단계의 이미지를 설정하여 주고
		// gif파일을 배열값에 집어넣는다.
		if(this.UNIT_TYPE.equals("BEAR")){			//	유닛의 타입 곰
			userUNIT[0].setIcon(new ImageIcon("Images/BattleWord/Unit_Be&Ti.gif"));
			userUNIT[1].setIcon(new ImageIcon("Images/BattleWord/Unit_BEAR2.gif"));
			userUNIT[2].setIcon(new ImageIcon("Images/BattleWord/Unit_BEAR3.gif"));
		}
		else if(this.UNIT_TYPE.equals("TIGER")){	//	유닛의 타입 호랑이
			userUNIT[0].setIcon(new ImageIcon("Images/BattleWord/Unit_Be&Ti.gif"));
			userUNIT[1].setIcon(new ImageIcon("Images/BattleWord/Unit_TIGER2.gif"));
			userUNIT[2].setIcon(new ImageIcon("Images/BattleWord/Unit_TIGER3.gif"));
		}
		else if(this.UNIT_TYPE.equals("PIG")){		//	유닛의 타입 돼지
			userUNIT[0].setIcon(new ImageIcon("Images/BattleWord/Unit_Pi&Go.gif"));
			userUNIT[1].setIcon(new ImageIcon("Images/BattleWord/Unit_PIG2.gif"));
			userUNIT[2].setIcon(new ImageIcon("Images/BattleWord/Unit_PIG3.gif"));
		}
		else if(this.UNIT_TYPE.equals("GOBLIN")){	//	유닛의 타입 도깨비
			userUNIT[0].setIcon(new ImageIcon("Images/BattleWord/Unit_Pi&Go.gif"));
			userUNIT[1].setIcon(new ImageIcon("Images/BattleWord/Unit_GOBLIN2.gif"));
			userUNIT[2].setIcon(new ImageIcon("Images/BattleWord/Unit_GOBLIN3.gif"));
		}
		else if(this.UNIT_TYPE.equals("RABBIT")){	//	유닛의 타입 토끼
			userUNIT[0].setIcon(new ImageIcon("Images/BattleWord/Unit_Ra&Fr.gif"));
			userUNIT[1].setIcon(new ImageIcon("Images/BattleWord/Unit_RABBIT2.gif"));
			userUNIT[2].setIcon(new ImageIcon("Images/BattleWord/Unit_RABBIT3.gif"));
		}
		else if(this.UNIT_TYPE.equals("FROG")){		//	유닛의 타입 개구리
			userUNIT[0].setIcon(new ImageIcon("Images/BattleWord/Unit_Ra&Fr.gif"));
			userUNIT[1].setIcon(new ImageIcon("Images/BattleWord/Unit_FROG2.gif"));
			userUNIT[2].setIcon(new ImageIcon("Images/BattleWord/Unit_FROG3.gif"));
		}
		
		
		// 정보를 전달한다.
		this.uerP.settingUserPanel(this.userID, this.userUNIT);
	}//setUser
}


// TextEx의 클래스로부터 받은 판단에 따라서 
// 정답, 오답, 타임초과에 관련된 스레드 처리를 실시한다.
// RightAns, WrongAns, TimeOver
class JudgeThread extends Thread{
	private wordSeqPanel wSeqPanel;
	private MainFrame mf;
	private String judgeStr = "";
	private int n = 0;
	
	protected double effectWidth = 0;			//	이펙트 너비
	protected double effectHeight = 0;			//	이펙트 높이
	
	public JudgeThread(MainFrame mf, wordSeqPanel wsp){
		this.mf = mf;
		this.wSeqPanel = wsp;
	}
	
	public synchronized void setJudge(String judge){
		this.judgeStr = judge;
	}
	
	public void run() {
		this.n = 0;
		int blockTime = 3;
		int gameTime = 5;
		int exitTime = 0;
		
		if(judgeStr.equals("RightAns")||judgeStr.equals("WrongAns"))
			exitTime = blockTime;
		else
			exitTime = gameTime;
		
		// 	이벤트 발생 시, 버튼의 인덱스는 올바르게 찍힌다.
		int i = this.wSeqPanel.ButtonEffect_i;
		int j = this.wSeqPanel.ButtonEffect_j;
		//	효과는 y값을 줄임으로써 올라가게끔 설정 
		
//		System.out.println("MainFrame 스레드 i >> " + i);
//		System.out.println("MainFrame 스레드 j >> " + j);
		
		this.effectWidth = (j+1) * 65.5;
		this.effectHeight = (i+1) * 65.5;
		
		while (n < exitTime) {
			// 정답인 경우, 맞췄다는 것을
			if (judgeStr.equals("RightAns")) {
				try{
					this.wSeqPanel.repaint();
					this.effectWidth = this.effectWidth - 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);

					this.wSeqPanel.repaint();
					this.effectWidth = this.effectWidth + 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);
			
					this.wSeqPanel.repaint();
					this.effectWidth = this.effectWidth - 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);
		
					this.wSeqPanel.repaint();
					this.effectWidth = this.effectWidth + 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);

					n++;
				}
				catch(Exception e){
					System.out.println("ERROR Thread >> ");
					System.out.println(e);
					return;
				}
			}
			
			// 오답인 경우, MainFrame은 진동을 한다.
			// wordSeqPanel 에는 오답이라는 글자가 버튼의 위치값에서 올라간다.
			else if (judgeStr.equals("WrongAns")) {
				try {
					// 좌
					this.mf.setLocation(this.mf.getX() - 5, this.mf.getY());
					this.effectWidth = this.effectWidth - 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);

					// 상
					this.mf.setLocation(this.mf.getX(), this.mf.getY() - 5);
					this.effectWidth = this.effectWidth + 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);

					// 하
					this.mf.setLocation(this.mf.getX(), this.mf.getY() + 5);
					this.effectWidth = this.effectWidth - 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);

					// 우
					this.mf.setLocation(this.mf.getX() + 5, this.mf.getY());
					this.effectWidth = this.effectWidth + 2;
					this.effectHeight = this.effectHeight - 5;
					this.wSeqPanel.Image_Y = (int)this.effectHeight;
					this.wSeqPanel.Image_X = (int)this.effectWidth;
					this.wSeqPanel.repaint();
					Thread.sleep(30);

					n++;
				} catch (Exception e) {
					System.out.println("ERROR Thread >> ");
					System.out.println(e);
					return;
				}
			}
			
			// 시간초과인경우 깜박깜박하도록 설정한다.
			else if (judgeStr.equals("TimeOver")) {
				try {
					this.wSeqPanel.repaint();
					
					if(this.mf.bright)
						this.mf.bright = false;
					else
						this.mf.bright = true;
				
					Thread.sleep(500);
					n++;
				}
				catch(Exception e){
					System.out.println("ERROR Thread >> ");
					System.out.println(e);
					return;
				}
			}
			
			else if(judgeStr.equals("Perfect")){
				try {
					this.wSeqPanel.repaint();
					
					if (this.mf.PERFECT)
						this.mf.PERFECT = false;
					else
						this.mf.PERFECT = true;
				
					Thread.sleep(600);
					n++;
				}
				catch(Exception e){
				}
			}
		}//while
		
		n = 0;
		
		// 다이얼로그를 띄어서 게임을 더 할 것인지 말 것인지 결정
		if(judgeStr.equals("TimeOver") || judgeStr.equals("Perfect")){
			this.mf.dispose();
			this.mf.mainDialog.setVisible(true);
		}
		
		// 이미지들을 투명하게 만든다.. 6시간 걸렸다.
		this.mf.judgeEffectStr = "Clear";
		this.wSeqPanel.repaint();
	}
}

public class GameFrame_MainFrame {
//	MainFrame mf;
	public static void main(String[] args) {
//		GameFrame_MainFrame gf = new GameFrame_MainFrame();
//		gf.mf = new MainFrame();
	}
}
