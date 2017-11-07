package BattleWord;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// MainFrame에서 보여지는 다이얼로그만들기.
class MainDialog extends JDialog {
	private mPanel m_Panel;
	private MainFrame mf;
	
	public MainDialog() {
		this.setTitle("MainFrame Dialog");
		this.setLayout(new BorderLayout());

		this.setPanel();

		this.add(m_Panel);
		this.setSize(700, 400);
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(getParent());
	
		// 보이게 하는것
		this.setVisible(false);
	}
	
	// 디폴트 생성자 이후에 메인프레임 객체 전달 받는다.
	public MainDialog(MainFrame mf){
		this();
		this.mf = mf;
	}

	// 다이얼로그이 패널세팅
	public void setPanel() {
		m_Panel = new mPanel(this);
	}
	
	// 초기화면으로 돌아가기 위한 메소드
	// 패널에서 호출된다.
	public void setStart(){
		this.mf.dispose();
		this.dispose();
		new StartFrame();
	}
	
	// 한번 더
	// 새로운 게임 환경을 만들어준다.
	public void reStart(){
		this.mf.dispose();
		this.dispose();
		
		new MainFrame(this.mf.userID, this.mf.UNIT_TYPE);
	}
}

class mPanel extends JPanel implements MouseListener {
	private JLabel oneMoreBtn[] = new JLabel[2];
	private JLabel setFirstBtn[] = new JLabel[2];
	private JLabel setExitBtn[] = new JLabel[2];
	private JLabel textLabel;
	
	private MainDialog gmDialog;

	// 생성자를 통해서 다이얼로그 객체 받음
	public mPanel(MainDialog dialog) {
		gmDialog = dialog;

		// 로고와 버튼을 패널에 세팅한다.
		this.setText();
		this.setButton();
		
		this.setLocation(0, 0);
		this.setSize(700, 400);
		this.setLayout(null);
	}
	
	public void setButton(){
		for(int i = 0; i < 2; i++){
			oneMoreBtn[i] = new JLabel();
			setFirstBtn[i] = new JLabel();
			setExitBtn[i] = new JLabel();
		}
		this.oneMoreBtn[0].setIcon(new ImageIcon("Images/BattleWord/Dialog_OneMoreBtn_before.png"));
		this.oneMoreBtn[1].setIcon(new ImageIcon("Images/BattleWord/Dialog_OneMoreBtn_after.png"));
		
		this.setFirstBtn[0].setIcon(new ImageIcon("Images/BattleWord/Dialog_FirstBtn_before.png"));
		this.setFirstBtn[1].setIcon(new ImageIcon("Images/BattleWord/Dialog_FirstBtn_after.png"));
		
		this.setExitBtn[0].setIcon(new ImageIcon("Images/BattleWord/Dialog_ExitBtn_before.png"));
		this.setExitBtn[1].setIcon(new ImageIcon("Images/BattleWord/Dialog_ExitBtn_after.png"));
		
		for(int i = 0; i < 2; i++){
			oneMoreBtn[i].setSize(150, 150);
			setFirstBtn[i].setSize(150, 150);
			setExitBtn[i].setSize(150, 150);
		}
		
		this.oneMoreBtn[0].setLocation(65, 170);
		this.oneMoreBtn[1].setLocation(65, 170);
		this.oneMoreBtn[1].setVisible(false);
		
		this.setFirstBtn[0].setLocation(265, 170);
		this.setFirstBtn[1].setLocation(265, 170);
		this.setFirstBtn[1].setVisible(false);
		
		this.setExitBtn[0].setLocation(465, 170);
		this.setExitBtn[1].setLocation(465, 170);
		this.setExitBtn[1].setVisible(false);
		
		for(int i = 0; i < 2; i++){
			oneMoreBtn[i].setOpaque(false);
			setFirstBtn[i].setOpaque(false);
			setExitBtn[i].setOpaque(false);
			
			oneMoreBtn[i].addMouseListener(this);
			setFirstBtn[i].addMouseListener(this);
			setExitBtn[i].addMouseListener(this);
			
			this.add(oneMoreBtn[i]);
			this.add(setFirstBtn[i]);
			this.add(setExitBtn[i]);
		}
	}

	public void setText() {
		this.textLabel = new JLabel(new ImageIcon("Images/BattleWord/Dialog_Text.png"));
		this.textLabel.setSize(670, 159);
		this.textLabel.setLocation(15, 15);
		this.textLabel.setOpaque(false);
		this.add(textLabel);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == this.oneMoreBtn[0]){
			this.oneMoreBtn[0].setVisible(false);
			this.oneMoreBtn[1].setVisible(true);
		}
		else if(e.getSource() == this.setFirstBtn[0]){
			this.setFirstBtn[0].setVisible(false);
			this.setFirstBtn[1].setVisible(true);
		}
		else if(e.getSource() == this.setExitBtn[0]){
			this.setExitBtn[0].setVisible(false);
			this.setExitBtn[1].setVisible(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// 게임 한번 더
		if (e.getSource() == this.oneMoreBtn[0]) {
			this.oneMoreBtn[1].setVisible(false);
			this.oneMoreBtn[0].setVisible(true);
			this.gmDialog.reStart();
			// 게임이 한번더 실행된다.
		} 
		// 처음 화면
		else if (e.getSource() == this.setFirstBtn[0]) {
			this.setFirstBtn[1].setVisible(false);
			this.setFirstBtn[0].setVisible(true);
			this.gmDialog.setStart();				// MainFrame의 객체를 닫는다.
		} 
		else if(e.getSource() == this.setExitBtn[0]){
			this.setExitBtn[1].setVisible(false);
			this.setExitBtn[0].setVisible(true);
			System.exit(0);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}	
	@Override
	public void mouseExited(MouseEvent arg0) {}
}

public class GameFrame_MainDialog {
	public static void main(String[] args) {
	}
}
