package BattleWord;

import BattleWord.GameFrame_MainFrame;
import java.lang.Runnable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class TimePanel extends JPanel implements Runnable{
	private MainFrame mf;
	protected Thread timeThread;	//	시간관련 스레드
	private JLabel timeLabel;		//	시간출력 레이블
	private JLabel timeText;
	
	protected boolean winCheck = false;
	protected String min = "", sec = "";
	
	public TimePanel(){}
	public TimePanel(MainFrame mf) {
		this.mf = mf;
		this.settingLabel();
		this.attachPanel();
		this.startGame(); 	//완전히 게임이 되고나서 실행
	}
	
	// 배경과 글자을 세팅한다.
	public void settingLabel(){
		this.setLayout(null);
		this.setSize(292, 100);
		this.setLocation(601, 97);
		
//		this.setBackground(Color.RED);
		
		this.timeLabel = new JLabel(new ImageIcon("Images/BattleWord/TimePanel.png"));
		this.timeLabel.setSize(300, 100);
		this.timeLabel.setLocation(-5, 1);
		
		this.timeText = new JLabel();
		this.timeText.setFont(new Font("나눔바른고딕", Font.BOLD, 60));
		this.timeText.setLocation(70, 20);
		this.timeText.setSize(230, 70);
		
		this.timeText.setOpaque(false);
		this.add(timeText);
		this.add(timeLabel);
	}
	
	// 패널을 프레임에 부착
	public void attachPanel(){
		mf.add(this);
	}
	
	// 게임이 시작되면 
	// 스레드 객체 생성 후 start();
	public void startGame(){
		this.timeThread = new Thread(this);
		this.timeThread.start();
	}
	
	@Override
	public void run() {
		int i = 1;
		int startTime = 120;
		
		try{
			while(true){
				this.min = Integer.toString(startTime / 60);
				this.sec = Integer.toString(startTime % 60);
				
				if(this.sec.length() == 1){
					this.sec = "0" + this.sec;
				}
				
				this.timeText.setText(this.min + " : " + this.sec);
				//System.out.println(min + " : " + sec);
				
				if(this.winCheck){
					System.out.println("다 맞춤");
					break;
				}
				if(this.min.equals("0") && this.sec.equals("00")){
					System.out.println("종료");
					break;
				}
				
				startTime--;
				Thread.sleep(900);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		// 스레드가 종료되는 시점은 게임이 끝나는 시점이다.
		System.out.println("Time :: METHOD()");
		
		if(!this.winCheck)
			this.mf.judgeMethod("TimeOver");
		else
			this.mf.judgeMethod("Perfect");
	}
}

public class GameFrame_Time{
	public static void main(String[]args){
		TimePanel tp = new TimePanel();
		tp.startGame();
	}
}
