package BattleWord;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// 버튼을 중점적으로 보여주기 위한 클래스
class Down_Panel extends JPanel implements MouseListener{
	// [게임하기] 버튼의 클릭 전과 클릭 후의 모습들
	private JButton startBtn_before = new JButton(new ImageIcon("Images/BattleWord/StartBtn_before.png"));
	private JButton startBtn_after = new JButton(new ImageIcon("Images/BattleWord/StartBtn_after.png"));
	
	// [게임끝내기] 버튼의 클릭 전과 클릭 후의 모습들
	private JButton finishBtn_before = new JButton(new ImageIcon("Images/BattleWord/finishBtn_before.png"));
	private JButton finishBtn_after = new JButton(new ImageIcon("Images/BattleWord/finishBtn_after.png"));
	
	// [게임 시작] 버튼의 클릭 전과 클릭 후의 모습들 설정
	private JButton quizBtn_before = new JButton(new ImageIcon("Images/BattleWord/quizBtn_before.png"));
	private JButton quizBtn_after = new JButton(new ImageIcon("Images/BattleWord/quizBtn_after.png"));
	
	// [캐릭터를 선택하세요] Label 
	private JLabel SelectLabel = new JLabel(new ImageIcon("Images/BattleWord/ChoiceLabel.png"));
	
	private StartFrame stFrame;
	private Up_Panel1 up1;	// 로고 패널
	private Up_Panel2 up2;	// 캐릭터 선택 패널
	
	//private 
	public Down_Panel(StartFrame stFrame){
		this.stFrame = stFrame;
		this.setting();
	}
	
	public void setting(){
		this.setBackground(Color.WHITE);
		this.setSize(800, 200);
		this.setLocation(0, 190);
		this.setLayout(null);	// 레이아웃 null로 설정
		
		// [게임하기] 버튼의 클릭 전과 클릭 후의 모습의 위치와 크기 설정
		// 클릭 후의 모습은 안 보이게 설정
		this.startBtn_before.setBounds(60, 390, 340, 110);
		this.startBtn_after.setBounds(60, 390, 340, 110);
		this.startBtn_after.setVisible(false);
		
		
		// [게임 끝내기] 버튼의 클릭 전과 클릭 후의 모습의 위치와 크기 설정
		// 클릭 후의 모습은 안 보이게 설정
		this.finishBtn_before.setBounds(400, 390, 340, 110);
		this.finishBtn_after.setBounds(400, 390, 340, 110);
		this.finishBtn_after.setVisible(false);
		
		
		// [게임 시작] 버튼의 클릭 전과 클릭 후의 모습들 설정
		// 처음 패널 설정 시 보이지 않도록 설정
		this.quizBtn_before.setBounds(222, 410, 340, 110);
		this.quizBtn_before.setVisible(false);
		this.quizBtn_after.setBounds(222, 410, 340, 110);
		this.quizBtn_after.setVisible(false);

		// [캐릭터를 선택하세요] Label 위치, 크기 설정
		// JLabel이 보이지 않도록 설정
		this.SelectLabel.setBounds(160, 415, 500, 80);
		this.SelectLabel.setVisible(false);
		
		
		// JPanel에 존재하는 버튼들에 대해서 버튼 배경 이미지 설정
		// JPanel에 존재하는 버튼들에 대해서 리스너를 등록한다.
		this.settingButton();
		this.addListenerAtButton();
		
		this.stFrame.add(this);
	}
	
	// JPanel에 존재하는 버튼들에 대해서 버튼 배경 이미지 설정
	// JPanel에 존재하는 레이블 부착
	public void settingButton(){
		this.startBtn_before.setBorderPainted(false);
		this.startBtn_before.setFocusPainted(true);
		this.startBtn_before.setContentAreaFilled(false);
		
		this.startBtn_after.setBorderPainted(false);
		this.startBtn_after.setFocusPainted(true);
		this.startBtn_after.setContentAreaFilled(false);
		
		this.finishBtn_before.setBorderPainted(false);
		this.finishBtn_before.setFocusPainted(true);
		this.finishBtn_before.setContentAreaFilled(false);
		
		this.finishBtn_after.setBorderPainted(false);
		this.finishBtn_after.setFocusPainted(true);
		this.finishBtn_after.setContentAreaFilled(false);
		
		this.quizBtn_before.setBorderPainted(false);
		this.quizBtn_before.setFocusPainted(true);
		this.quizBtn_before.setContentAreaFilled(false);
		
		this.quizBtn_after.setBorderPainted(false);
		this.quizBtn_after.setFocusPainted(true);
		this.quizBtn_after.setContentAreaFilled(false);
		
		this.add(startBtn_before);
		this.add(startBtn_after);
		this.add(finishBtn_before);
		this.add(finishBtn_after);
		this.add(quizBtn_before);
		this.add(quizBtn_after);
		
		//JLabel 부착
		this.add(this.SelectLabel);
	}
	
	// JPanel에 존재하는 버튼들에 대해서 리스너를 등록한다.
	public void addListenerAtButton(){
		this.startBtn_before.addMouseListener(this);
		this.startBtn_after.addMouseListener(this);
		
		this.finishBtn_before.addMouseListener(this);
		this.finishBtn_after.addMouseListener(this);
		
		this.quizBtn_before.addMouseListener(this);
		this.quizBtn_after.addMouseListener(this);
	}
	
	// StartFrame에서 로고 패널과 캐릭터 선택 패널을 받는다.
	public void catchPanel(Up_Panel1 up1, Up_Panel2 up2){
		this.up1 = up1;	//	로고패널
		this.up2 = up2;	//	캐릭터패널
	}
	
	// 마우스가 눌렸을 때, 색상 변경
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource().equals(this.startBtn_before)){
			this.startBtn_after.setVisible(true);
			this.startBtn_before.setVisible(false);
		}
		else if(e.getSource().equals(this.finishBtn_before)){
			this.finishBtn_after.setVisible(true);
			this.finishBtn_before.setVisible(false);
		}
		else if(e.getSource().equals(this.quizBtn_before)){
			this.quizBtn_after.setVisible(true);
			this.quizBtn_before.setVisible(false);
		}
	}// MouseListener :: MousePressed()
	
	// 마우스를 누른 상태에서 떼었을 때, 이벤트 처리
	@Override
	public void mouseReleased(MouseEvent e) {
		// 패널을 변경시킨다.
		// 로고가 움직이는 패널의 스레드는 리턴시켜 종료시킨다.
		if(e.getSource().equals(this.startBtn_before)){
			this.startBtn_before.setVisible(true);
			this.startBtn_after.setVisible(false);
			
			//this.up2.set
			// 패널 변경 및 스레드 죽임
			this.up2.setVisible(true);
			this.up1.deathThread();
			this.up1.setVisible(false);
			
			// 패널은 변경되며, 두개의 버튼들은 사라지며 하나의 버튼이 생성된다.
			this.startBtn_before.setVisible(false);
			this.finishBtn_before.setVisible(false);
			this.SelectLabel.setVisible(true);
		}
		
		// 게임 종료
		else if(e.getSource().equals(this.finishBtn_before)){
			this.finishBtn_before.setVisible(true);
			this.finishBtn_after.setVisible(false);
			System.exit(0);
		}
	
		//	게임 시작 버튼을 클릭하였을 경우
		//	기존의 프레임은 사라지고, 게임 화면이 나타난다.
		//	Up_Panel에서 선택한 값을 MainFrame에 전달해야 한다.
		else if(e.getSource().equals(this.quizBtn_before)){
			this.quizBtn_before.setVisible(true);
			this.quizBtn_after.setVisible(false);
			
			this.stFrame.convertFrame();
		}
	}// MouseListener :: MouseRealeased()
	
	
	// 캐릭터가 선택되고 나서
	// 이 경우 선택하는 변수를 설정하여 놓는다.
	// 게임시작하는 버튼
	public void setQuizBtn(){
		this.SelectLabel.setVisible(false);		//	Label은 가려지고
		this.quizBtn_before.setVisible(true);	//	Button은 보여진다.
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}


//사용하지 않는 클래스
class GameFrame_StartFrame_Down{
	public GameFrame_StartFrame_Down(){
		
	}
}