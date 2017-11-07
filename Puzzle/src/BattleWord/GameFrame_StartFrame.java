package BattleWord;

import BattleWord.GameFrame_MainFrame;
import BattleWord.GameFrame_StartFrame_UP;
import BattleWord.GameFrame_StartFrame_Down;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// 처음 실행되는 Frame
// 로고와 버튼이 존재하며, 이후에 게임하기를 눌렀을 경우 게임이 실행된다.
class StartFrame extends JFrame{
	private Up_Panel1 logoPanel;	// 로고가 보여지는 패널	
	private Up_Panel2 choicePanel;	// 캐릭터를 선택할 수 있는 패널
	private Down_Panel downPanel;	// StartFrame내에서 버튼이 보여지는 패널
	private MainFrame mainFrame;	// 메인 프레임
	
	public StartFrame(){
		this.setPanel();
		this.sendPanel();
		this.settingFrame();
	}	
	
	// Panel들의 객체들을 얻는다.
	public void setPanel(){
		this.logoPanel = new Up_Panel1(this);
		this.choicePanel = new Up_Panel2(this);
		this.downPanel = new Down_Panel(this);
	}
	
	public void sendPanel(){
		this.downPanel.catchPanel(logoPanel, choicePanel);
	}
	
	public void settingFrame(){
		this.setTitle("Start Frame");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		
		this.setLocationRelativeTo(getParent());
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void sendInfoToDP(){
		this.downPanel.setQuizBtn();
	}
	
	// 프레임간의 전환을 나타낸다.
	// 메인 프레임으로 유저의 정보를 전달한다.
	// 매개변수 있는 생성자를 먼저 호출.
	// 이후에 디폴트 생성자 호출
	public void convertFrame(){
		this.setVisible(false);
		String userName = this.choicePanel.userName;
		String unitType = this.choicePanel.userType;
		
		mainFrame = new MainFrame(userName, unitType);
		this.dispose();
		
	}
}
// 게임이 시작되고 
public class GameFrame_StartFrame {
	public static void main(String[]args){
		new StartFrame();
	}
}
