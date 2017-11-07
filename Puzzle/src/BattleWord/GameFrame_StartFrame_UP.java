package BattleWord;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//1. 로고가 나타나는 패널
class Up_Panel1 extends JPanel implements Runnable{
	private StartFrame stFrame;
	private ImageIcon logoImage [];	// 게임 시작 화면에 보여지는 로고
	private Image testImage[];		// 게임 시작 화면에 보여지는 로고
	private Thread logoThread; 
	private int imageIdx = 1;
	private boolean flag = true;
	
	public Up_Panel1(StartFrame stFrame){
		this.stFrame = stFrame;
		this.setting();
		
		// Thread를 셋팅 완료 시킨다.
		this.settingThread();
		this.logoThread.start();
	}
	
	// 매개변수가 있는 생성자로부터 받은 StartFrame의 객체에
	// this(객체 자기 자신)인 패널을 부착한다.
	public void setting(){
		this.logoImage = new ImageIcon[4];
		this.testImage = new Image[4];
		
		for(int i = 0; i < logoImage.length; i++){
			String iStr = Integer.toString(i+1);
			String imageUrl = "Images/BattleWord/GameLogo_" + iStr + ".png";
			this.logoImage[i] = new ImageIcon(imageUrl);
			this.testImage[i] = logoImage[i].getImage();
		}
		
		this.setBackground(Color.WHITE);
		this.setSize(800, 390);
		this.setLocation(0, 0);
		this.stFrame.add(this);
	}
	
	// 스레드 셋팅
	public void settingThread(){
		this.logoThread = new Thread(this);
	}
	
	// 로고가 스레드에 따라서 계속 바뀌어진다.
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(testImage[imageIdx-1], 0, 0, null);
	}
	
	@Override
	// 로고가 변하게 설정하기 위한 스레드
	public void run() {
		while(true){
			if(flag){
				try{
					this.imageIdx = 1;
					this.repaint();
					Thread.sleep(400);
					
					this.imageIdx = 2;
					this.repaint();
					Thread.sleep(400);
					
					this.imageIdx = 3;
					this.repaint();
					Thread.sleep(400);
					
					this.imageIdx = 4;
					this.repaint();
					Thread.sleep(400);
				}
				catch(Exception e){	
					System.out.println("Panel ERROR >> " + e);
				}
			}//if(flag)
			else
				break;
		}
		// 스레드 죽는다.
		return;
	}
	public void deathThread(){
		this.flag = false;
	}
}


//	2. 캐릭터를 선택할 수 있는 패널
class Up_Panel2 extends JPanel implements MouseListener{
	private StartFrame stFrame;
	private JLabel smallLogo;
	
	private JLabel unitLabel_be[] = new JLabel[3];		// 유닛 곰
	private JLabel unitLabel_ti[] = new JLabel[3];		// 유닛 호랑이
	private JLabel unitLabel_pi[] = new JLabel[3];		// 유닛 돼지
	private JLabel unitLabel_go[] = new JLabel[3];		// 유닛 도깨비
	private JLabel unitLabel_ra[] = new JLabel[3];		// 유닛 토끼
	private JLabel unitLabel_fr[] = new JLabel[3];		// 유닛 개구리
	
	// 기본이미지
	private JLabel unitLabel_BeTi = new JLabel(new ImageIcon("Images/BattleWord/Unit_Be&Ti.gif"));
	private JLabel unitLabel_PiGo = new JLabel(new ImageIcon("Images/BattleWord/Unit_Pi&Go.gif"));
	private JLabel unitLabel_RaFr = new JLabel(new ImageIcon("Images/BattleWord/Unit_Ra&Fr.gif"));
	
	private Image panImage[];			// 유닛 밑에 존재하는 판 이미지
	protected int flagInt = 0;			// 유닛 밑에 존재하는 판의 번호 부여
	
	
	protected String userName = "";		// 유저의 닉네임
	protected String userType = "";		// 유저의 타입
	
	public Up_Panel2(StartFrame stFrame){
		this.stFrame = stFrame;
		
		this.setUnitAndPan();
		this.setting();
	}
	
	public void setting(){
		this.setBackground(Color.WHITE);
		this.setSize(800, 390);
		this.setLocation(0, 0);
		this.setLayout(null);
		this.setVisible(false);
	
		this.stFrame.add(this);
	}
	
	// Label에 유닛과 이미지에 판 세팅
	public void setUnitAndPan(){
		this.panImage = new Image[4];
		
		for(int i = 0; i < 3; i++){
			this.unitLabel_be[i] = new JLabel();
			this.unitLabel_ti[i] = new JLabel();
			this.unitLabel_pi[i] = new JLabel();
			this.unitLabel_go[i] = new JLabel();
			this.unitLabel_ra[i] = new JLabel();
			this.unitLabel_fr[i] = new JLabel();
		}
		
		this.unitLabel_BeTi.setSize(255, 250);
		this.unitLabel_BeTi.setLocation(5, 100);
		
		this.unitLabel_PiGo.setSize(255, 250);
		this.unitLabel_PiGo.setLocation(280, 100);
		
		this.unitLabel_RaFr.setSize(250, 280);
		this.unitLabel_RaFr.setLocation(547, 75);
		
		this.unitLabel_BeTi.setOpaque(false);
		this.unitLabel_PiGo.setOpaque(false);
		this.unitLabel_RaFr.setOpaque(false);
		
		this.unitLabel_BeTi.addMouseListener(this);
		this.unitLabel_PiGo.addMouseListener(this);
		this.unitLabel_RaFr.addMouseListener(this);
		
		this.add(unitLabel_BeTi);
		this.add(unitLabel_PiGo);
		this.add(unitLabel_RaFr);
		
		// unitPan_Selected_num 별로 Image을 만들어주며,
		// 0은 아무것도 선택되지 않은 상태이다.
		for(int i = 0; i < this.panImage.length; i++){
			this.panImage[i] = (new ImageIcon("Images/BattleWord/unitPan_Selected_" + i + ".png")).getImage();
		}
	}
	
	// 다이얼로그의 정보를 시작 프레임으로 알려준다.
	public void sendSelectedInfo(){
		this.stFrame.sendInfoToDP();
	}
	
	// 캐릭터가 선택된 자리에 그 발판에 불빛이 들어올 수 있도록 설정해놓는다.
	// flagInt의 변수는 변경된다.
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawImage((new ImageIcon("Images/BattleWord/small_Logo.png")).getImage(), -5, 0, this);
		
		if(flagInt == 0){
			g.drawImage(this.panImage[flagInt], 0, 320, this);
		}
		else if(flagInt == 1){
			g.drawImage(this.panImage[flagInt], 0, 320, this);
		}
		else if(flagInt == 2){
			g.drawImage(this.panImage[flagInt], 0, 320, this);
		}
		else if(flagInt == 3){
			g.drawImage(this.panImage[flagInt], 0, 320, this);
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		selectedDialog sDialog;
		// 곰, 호랑이 선택 시
		if(e.getSource() == this.unitLabel_BeTi){
			sDialog = new selectedDialog(0, this);
		}
		// 돼지, 도깨비 선택 시
		else if(e.getSource() == this.unitLabel_PiGo){
			sDialog = new selectedDialog(1, this);
		}
		// 토끼, 개구리 선택 시
		else{
			sDialog = new selectedDialog(2, this);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}



// 선택한 캐릭터에 대해서 클릭리스너
class selectedDialog extends JDialog implements MouseListener, ActionListener{
	// 캐릭터 선택 RdaioButton
	protected JLabel inputLabelImage = new JLabel();
	
	protected JLabel nameLabel = new JLabel(new ImageIcon("Images/BattleWord/nameText.png"));
	protected JLabel LabelBtn1[] = new JLabel[2];
	protected JLabel LabelBtn2[] = new JLabel[2];
	protected JLabel LabelBtn3[] = new JLabel[2];
	protected JLabel LabelBtn4[] = new JLabel[2];
	protected JLabel LabelBtn5[] = new JLabel[2];
	protected JLabel LabelBtn6[] = new JLabel[2];
	
	protected int unitType;
	protected JTextField input_Field = new JTextField(10);
	protected JButton select = new JButton("완료");
	
	private Up_Panel2 up2;
	
	public selectedDialog(){
		this.setTitle("Unit Selected");
		this.getContentPane();
		this.setLayout(null);
		
		this.settingButtons();
		this.settingNames();
	}
	
	public selectedDialog(int type, Up_Panel2 up2){
		this();
		
		// 패널2의 객체를 전달받음
		this.up2 = up2;
		
		// 타입을 설정
		this.unitType = type;
		
		if(unitType == 0){
			LabelBtn1[0].setVisible(true);
			LabelBtn2[0].setVisible(true);

		}
		else if(unitType == 1){
			LabelBtn3[0].setVisible(true);
			LabelBtn4[0].setVisible(true);
		}
		else{
			LabelBtn5[0].setVisible(true);
			LabelBtn6[0].setVisible(true);
		}
		
		
		this.setSize(700, 400);
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(getOwner());
		this.setVisible(true);
	}
	
	// 캐릭터 설정
	public void settingButtons(){
		for(int i = 0; i < 2; i++){
			LabelBtn1[i] = new JLabel(new ImageIcon("Images/BattleWord/btn_bear" + Integer.toString(i+1) + ".png"));
			LabelBtn2[i] = new JLabel(new ImageIcon("Images/BattleWord/btn_tiger" + Integer.toString(i+1) + ".png"));
			LabelBtn3[i] = new JLabel(new ImageIcon("Images/BattleWord/btn_pig" + Integer.toString(i+1) + ".png"));
			LabelBtn4[i] = new JLabel(new ImageIcon("Images/BattleWord/btn_goblin" + Integer.toString(i+1) + ".png"));
			LabelBtn5[i] = new JLabel(new ImageIcon("Images/BattleWord/btn_rabbit" + Integer.toString(i+1) + ".png"));
			LabelBtn6[i] = new JLabel(new ImageIcon("Images/BattleWord/btn_frog" + Integer.toString(i+1) + ".png"));
			
			LabelBtn1[i].setBounds(150, 160, 165, 165);
			LabelBtn2[i].setBounds(400, 160, 165, 165);
			
			LabelBtn3[i].setBounds(150, 160, 165, 165);
			LabelBtn4[i].setBounds(400, 160, 165, 165);
			
			LabelBtn5[i].setBounds(150, 160, 165, 165);
			LabelBtn6[i].setBounds(400, 160, 165, 165);
			
			LabelBtn1[i].setVisible(false);
			LabelBtn2[i].setVisible(false);
			LabelBtn3[i].setVisible(false);
			LabelBtn4[i].setVisible(false);
			LabelBtn5[i].setVisible(false);
			LabelBtn6[i].setVisible(false);
			
			LabelBtn1[i].addMouseListener(this);
			LabelBtn2[i].addMouseListener(this);
			LabelBtn3[i].addMouseListener(this);
			LabelBtn4[i].addMouseListener(this);
			LabelBtn5[i].addMouseListener(this);
			LabelBtn6[i].addMouseListener(this);
			
			this.nameLabel.setBounds(150, 50, 180, 58);
			
			this.add(nameLabel);
			this.add(LabelBtn1[i]);
			this.add(LabelBtn2[i]);
			this.add(LabelBtn3[i]);
			this.add(LabelBtn4[i]);
			this.add(LabelBtn5[i]);
			this.add(LabelBtn6[i]);
		}
	}
	
	// 이름설정
	public void settingNames(){
		this.input_Field.setBounds(360, 65, 150, 30);
		this.select.setBounds(509, 65, 90, 30);
		
		//Add Listener
		this.input_Field.addActionListener(this);
		this.select.addActionListener(this);
		
//		this.add(this.inputLabelImage);
		this.add(input_Field);
		this.add(select);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 텍스트 필드 혹은 셀렉트에 이름 입력시 
		if((e.getSource() == input_Field) || (e.getSource() == select)){
			// 체크된 값을 패널에 전달 그리고 그 값을 프레임에 전달
			this.up2.userName = this.input_Field.getText();
					
			if(this.unitType == 0){
				this.up2.flagInt = 1;
				
				if(this.LabelBtn1[1].isVisible()){
					this.up2.userType = "BEAR";
				}
				else if(this.LabelBtn2[1].isVisible()){
					this.up2.userType = "TIGER";
				}
			}
			else if(this.unitType == 1){
				this.up2.flagInt = 2;
				
				if(this.LabelBtn3[1].isVisible()){
					this.up2.userType = "PIG";
				}
				else if(this.LabelBtn4[1].isVisible()){
					this.up2.userType = "GOBLIN";
				}
			}
			else if(this.unitType == 2){
				this.up2.flagInt = 3;
				
				if(this.LabelBtn5[1].isVisible()){
					this.up2.userType = "RABBIT";
				}
				else if(this.LabelBtn5[1].isVisible()){
					this.up2.userType = "FROG";
				}
			}
			
			// 캐릭터가 선택됨을 알린다.
			this.up2.sendSelectedInfo();
			this.dispose();
		}
	}

	@Override
	// 다이얼로그에 띄어진 버튼 클릭 효과
	public void mousePressed(MouseEvent e) {
		
		if(e.getSource() == this.LabelBtn1[0]){
			this.LabelBtn2[0].setVisible(true);
			this.LabelBtn2[1].setVisible(false);
			this.LabelBtn1[1].setVisible(true);
			this.LabelBtn1[0].setVisible(false);
		}
		else if(e.getSource() == this.LabelBtn1[1]){
			this.LabelBtn2[0].setVisible(true);
			this.LabelBtn2[1].setVisible(false);
			this.LabelBtn1[0].setVisible(true);
			this.LabelBtn1[1].setVisible(false);
		}
		if(e.getSource() == this.LabelBtn2[0]){
			this.LabelBtn1[0].setVisible(true);
			this.LabelBtn1[1].setVisible(false);
			this.LabelBtn2[1].setVisible(true);
			this.LabelBtn2[0].setVisible(false);
		}
		else if(e.getSource() == this.LabelBtn2[1]){
			this.LabelBtn1[0].setVisible(true);
			this.LabelBtn1[1].setVisible(false);
			this.LabelBtn2[0].setVisible(true);
			this.LabelBtn2[1].setVisible(false);
		}
		
		if(e.getSource() == this.LabelBtn3[0]){
			this.LabelBtn4[0].setVisible(true);
			this.LabelBtn4[1].setVisible(false);
			this.LabelBtn3[1].setVisible(true);
			this.LabelBtn3[0].setVisible(false);
		}
		else if(e.getSource() == this.LabelBtn3[1]){
			this.LabelBtn4[0].setVisible(true);
			this.LabelBtn4[1].setVisible(false);
			this.LabelBtn3[0].setVisible(true);
			this.LabelBtn3[1].setVisible(false);
		}
		
		if(e.getSource() == this.LabelBtn4[0]){
			this.LabelBtn3[0].setVisible(true);
			this.LabelBtn3[1].setVisible(false);
			this.LabelBtn4[1].setVisible(true);
			this.LabelBtn4[0].setVisible(false);
		}
		else if(e.getSource() == this.LabelBtn4[1]){
			this.LabelBtn3[0].setVisible(true);
			this.LabelBtn3[1].setVisible(false);
			this.LabelBtn4[0].setVisible(true);
			this.LabelBtn4[1].setVisible(false);
		}
		
		if(e.getSource() == this.LabelBtn5[0]){
			this.LabelBtn6[0].setVisible(true);
			this.LabelBtn6[1].setVisible(false);
			this.LabelBtn5[1].setVisible(true);
			this.LabelBtn5[0].setVisible(false);
		}
		else if(e.getSource() == this.LabelBtn5[1]){
			this.LabelBtn6[0].setVisible(true);
			this.LabelBtn6[1].setVisible(false);
			this.LabelBtn5[0].setVisible(true);
			this.LabelBtn5[1].setVisible(false);
		}
		
		if(e.getSource() == this.LabelBtn6[0]){
			this.LabelBtn5[0].setVisible(true);
			this.LabelBtn5[1].setVisible(false);
			this.LabelBtn6[1].setVisible(true);
			this.LabelBtn6[0].setVisible(false);
		}
		else if(e.getSource() == this.LabelBtn6[1]){
			this.LabelBtn5[0].setVisible(true);
			this.LabelBtn5[1].setVisible(false);
			this.LabelBtn6[0].setVisible(true);
			this.LabelBtn6[1].setVisible(false);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}


// 사용하지 않는 클래스
class GameFrame_StartFrame_UP{
	public GameFrame_StartFrame_UP(){		
	}
}
