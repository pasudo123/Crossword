package BattleWord;

import BattleWord.GameFrame_MainFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// 유저가 보여지는 패널
// 
class UserPanel extends JPanel{
	private MainFrame mf;
	private JLabel scoreLabel;							// 	스코어 레이블
	private JLabel seenUserImage;						//  유저가 보이는 레이블
	private JLabel seenUserName;						//	유저의 이름이 보이는 레이블
	
	private JLabel userImage[];			// 	유저 이미지 
	private JLabel userIdLabel;			//	유저 이름
	private JLabel userScore;			//	스코어 쓰는 레이블
	private JLabel testImage;	
	
	private String userName = "";
	private String unitType = "";
	protected int ScoreValue = 0;		// 점수 값
	
	public UserPanel(MainFrame mf) {
		this.mf = mf;
	}
	
	// 유저의 아이디와 유저의 유닛 타입을 설정한다.
	public void settingUserPanel(String userID, JLabel[] userUNIT){
		System.out.println("setUserPanel");
		this.userName = userID;
		this.userImage = userUNIT;
		
		this.setUserSeen();
		this.setMoveLabel();
		this.attachLabelAtLabel();
		this.settingPanel();
	}

	// 스코어, 유저가 보이는 레이블, 유저 이름 레이블을 셋팅
	public void setUserSeen(){
		// 스코어 레이블 설정
		this.scoreLabel = new JLabel(new ImageIcon("Images/BattleWord/ScoreLabel.png"));
		this.scoreLabel.setSize(300, 85);
		this.scoreLabel.setLocation(-3, 0);
		
		// 유저 모습 패널 설정
		this.seenUserImage = new JLabel(new ImageIcon("Images/BattleWord/UserImage.png"));
		this.seenUserImage.setSize(300, 340);
		this.seenUserImage.setLocation(-5, 80);
		
		// 유저 이름 레이블 설정
		this.seenUserName = new JLabel(new ImageIcon("Images/BattleWord/UserName.png"));
		this.seenUserName.setSize(300, 90);
		this.seenUserName.setLocation(-5, 414);
	}
	
	// 변하는 레이블들 세팅
	public void setMoveLabel(){		
		this.userScore = new JLabel();							//	유저 스코어
		this.userScore.setFont(new Font("나눔바른고딕", Font.BOLD, 60));
		this.userScore.setForeground(Color.MAGENTA);
		this.userScore.setText("0");
		this.userScore.setSize(100, 80);
		this.userScore.setLocation(95, 3);
		this.userScore.setHorizontalAlignment(SwingConstants.CENTER);
		this.userScore.setVerticalAlignment(SwingConstants.CENTER);
		this.userScore.setOpaque(false);
		
		// 테스트용
		//this.testImage = new JLabel(new ImageIcon("Images/BattleWord/test.gif"));		// 유저 이미지 세팅
		// 실제
		
		for(int i = 0; i < 3; i++){
			this.userImage[i].setSize(320, 370);	// 이 사이즈로
			this.userImage[i].setLocation(-50, -10);	// 이 위치로
			this.userImage[i].setOpaque(false);		// 불투명성
			this.userImage[i].setVisible(false);
		}
		
		this.userImage[0].setVisible(true);
		
		this.userIdLabel = new JLabel();		// 	유저 ID
		this.userIdLabel.setFont(new Font("나눔바른고딕", Font.BOLD, 35));
		this.userIdLabel.setForeground(Color.BLACK);
		this.userIdLabel.setText(this.userName);
		this.userIdLabel.setSize(180, 80);
		this.userIdLabel.setLocation(60, 10);
		this.userIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.userIdLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.userIdLabel.setOpaque(false);
		
		// 투명한 레이블을 상위 컴포넌트에 부착
		this.scoreLabel.add(userScore);
		//this.seenUserImage.add(testImage);
		for(int i = 0; i < 3; i++)
			this.seenUserImage.add(userImage[i]);
		this.seenUserName.add(userIdLabel);
		
		
	}
	
	// 커스터마이징한 레이블에 이름과 캐릭터 스코어 셋팅
	public void attachLabelAtLabel(){
		this.add(scoreLabel);
		this.add(seenUserImage);
		this.add(seenUserName);
	}
	
	
	// this의 UserPanel을 셋팅
	public void settingPanel() {
		this.setLayout(null);
		this.setSize(300, 500);
		this.setLocation(600, 200);
		
		this.mf.add(this);
	}
	
	// 스코어를 더해주고, 그에 따른 진화를 검사한다.
	public void addScore(){
		int before = Integer.parseInt(this.userScore.getText());
		int after = this.ScoreValue;
		
		this.userScore.setText(Integer.toString(before + after));
		
		int updateScore = Integer.parseInt(this.userScore.getText());
		
		// 스코어에 따른 1차 진화
		if((updateScore < 26) && (updateScore > 13)){
			this.userImage[0].setVisible(false);
			this.userImage[1].setVisible(true);
		}
		// 스코어에 따른 2차 진화
		else if((updateScore >= 26)){
			this.userImage[1].setVisible(false);
			this.userImage[2].setVisible(true);
		}
	}
}

// 십자말풀이의 게임 유저를 표시하는 클래스
public class GameFrame_User extends JFrame{
	public static void main(String[]args){
	}
}
