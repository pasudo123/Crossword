package BattleWord;

import BattleWord.GameFrame_MainFrame;
import BattleWord.GameFrame_TextEx;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

// 십자말풀이가 보여지는 패널
// 십자말풀이의 버튼을 클릭하였을 경우에 
// 게임의 설명이 JTextArea에 출력되고 게임의 정답은 따로 저장되어있다.
class wordSeqPanel extends JPanel {
	private MainFrame mf;
	protected SeqButton seqButton[][];
	protected int click_X;			// JButton에서 클릭된 X의 좌표
	protected int click_Y;			// JButton에서 클릭된 Y의 좌표
	private String pos;				// JButton에서 클릭된 좌우상하
	private int firA, firB, secA, secB;
	private int gn = 0;				// 게임의 타입을 나타내는 변수 ( 이후에 설정 ) 
	
	
	static int puzzleType = 0;	// 텍스트 파일로 읽어들인 낱말퍼즐의 타입 저장 (STATIC)
	
	
	protected JPanel thisPanel;		
	protected JLabel effectLabel[];		// 이미지가 보여지는 레이블
	protected JLabel doorEffect;
	protected int Image_X = 0;
	protected int Image_Y = 0;		// 이미지 좌표 XY 
	protected int ButtonEffect_i = 0;
	protected int ButtonEffect_j = 0;
	private Scanner sc;				// 입출력
			
	public wordSeqPanel(MainFrame mf){ 
		this.mf = mf;
		this.setPanel();
		this.setEffectLabel();	// 	이펙트 레이블 설정		
		this.madePanel();		//	십자말풀이 버튼 설정
		this.attachPanel();		//	패널 프레임에 부착
	}
		
	// 패널설정(색상, 레이아웃)
	public void setPanel(){
		this.setBackground(Color.BLACK);
		//this.setLayout(new GridLayout(7, 7));
		this.setLayout(null);		// 널 값
		this.setLocation(0, 100); 	// 프레임 내에서 위치 조정
	}
	
	// 설정된 패널을 프레임에 부착
	public void attachPanel(){
		mf.add(this);
	}
	
	//세가지 이펙트를 가지는 레이블 객체 생성 및 사이즈 조정
	public void setEffectLabel(){
		this.effectLabel = new JLabel[5];
		this.effectLabel[0] = new JLabel(new ImageIcon("Images/BattleWord/Judge_GOOD_Big.png"));
		this.effectLabel[1] = new JLabel(new ImageIcon("Images/BattleWord/Judge_MISS_Big.png"));
		this.effectLabel[2] = new JLabel(new ImageIcon("Images/BattleWord/Judge_TimeOver.png"));
		this.effectLabel[3] = new JLabel(new ImageIcon("Images/BattleWord/Judge_PERFECT.png"));
		this.effectLabel[4] = new JLabel(new ImageIcon("Images/BattleWord/Judge_CLEAR.png"));
		
		for(int i = 0; i < effectLabel.length; i++){
			this.effectLabel[i].setSize(140,50);
			if(i == 2)
				this.effectLabel[i].setSize(500, 350);
			if(i == 3)
				this.effectLabel[i].setSize(600, 400);
		}
			
		this.thisPanel = new JPanel();
		this.thisPanel.setSize(600, 600);
		this.thisPanel.setLocation(0, 0);
		this.thisPanel.setBackground(Color.WHITE);
		this.thisPanel.setLayout(null);
		
		for(int i = 0; i < effectLabel.length; i++)
			this.thisPanel.add(effectLabel[i]);
		
		// 문열리는 효과
		this.doorEffect = new JLabel(new ImageIcon("Images/BattleWord/Door.gif"));
		this.doorEffect.setSize(600, 600);
		this.doorEffect.setLocation(0, 0);
		
		this.thisPanel.setOpaque(false);
		
		this.add(this.doorEffect);
		this.add(this.thisPanel);
	}
	
	// JButton으로부터 받은 XY의 좌표값 저장 => 보여지는 문제의 설명을 판단할 수 있다.
	// 현재 사용하지 않는다.
	public void setClick_XY(String XY){
		StringTokenizer stnzer = new StringTokenizer(XY, "&");
		this.click_X = Integer.parseInt(stnzer.nextToken());
		this.click_Y = Integer.parseInt(stnzer.nextToken());
	}
	
	// JButton으로 찍힌 XY 좌표의 위치 상하좌우, 게임번호(가로:홀수 | 세로:짝수), 낱말의 길이(가로:홀수 | 세로:짝수)
	// JButton으로 찍힌 실시간의 좌표의 값을 MainFrame에 전송
	public void setPos_XY(String pos_XY, int gameNum_W, int gameNum_H, int wordLen_W, int wordLen_H){
		this.pos = pos_XY;
		
//		System.out.println("wordSeq class, PuzzleType >> "+puzzleType);
		//System.out.println("pos >> " + pos);
		if(this.pos.equals("All")){	//firA, firB 존재
			// 가로가 존재하면
			if(gameNum_W != 0){
				// 존재하는 가로가, 정답이라면
				if(gameNum_W == -1){
					this.mf.sendInfo(this.puzzleType, gameNum_H, wordLen_H);
				}
				else{
					this.mf.sendInfo(this.puzzleType, gameNum_W, wordLen_W);
				}
			}
			// 가로가 존재하지 않으면 세로가 존재
			else{
				this.mf.sendInfo(this.puzzleType, gameNum_H, wordLen_H);
			}
		}
		//firA, firB, secA, secB가 존재, 가로의 낱말을 선택해준다.
		else if(this.pos.equals("Right") || this.pos.equals("Left")){
			this.mf.sendInfo(this.puzzleType, gameNum_W, wordLen_W);
		}
		//firA, firB, secA, secB가 존재, 세로의 낱말을 선택해준다.
		else if(this.pos.equals("Up") || this.pos.equals("Down")){
			this.mf.sendInfo(this.puzzleType, gameNum_H, wordLen_H);
		}
	}
	
	// JButton의 객체 배열을 MainFrame으로 전달, 딱 한번 호출
	public void sendButtonArr(SeqButton[][] seqButton){
		this.mf.sendButtonArr(seqButton);
	}
	
	// seqButton[i][j] 의 테두리를 설정한다.
	// JButton으로 찍힌 2차원 배열의 i 와  j 의 인덱스 값을 얻어오고 MainFrame으로 전달
	public void setJButton(int i, int j){
//		System.out.println("SetJButton : i >> " + i);
//		System.out.println("SetJButton : j >> " + j);\
		
		//System.out.println("오류 : 객체가 여러번 생성");
		// 버튼의 인덱스 값 설정 -> 효과 나타내기용
		this.ButtonEffect_i = i;
		this.ButtonEffect_j = j;
		this.mf.setButtonIndex(i, j);
	}
	
	
	// 패널의 버튼들을 배틀가로세로로 표현
	public void madePanel(){
		try {
			/*************************************************************
			// 이후에 Random함수를 통해서 게임의 종류를 언더바를 기준으로 최소 5개 만들어놓자.
			// Random r = new Random();
			 * ***********************************************************/
			// 16.07.25 코드, 1 ~ 4 까지 랜덤 값 넣고 그 값으로 게임을 형성한다.
			Random r = new Random();
			wordSeqPanel.puzzleType = (int)((Math.random()*4)+1);

			/*************************************************************/
			this.sc = new Scanner(new FileInputStream("src/WordSource/game_"+Integer.toString(this.puzzleType) + ".text"));
			//System.out.println("src/WordSource/game_"+Integer.toString(this.puzzleType) + ".text");
			seqButton = new SeqButton[7][7];
			
			this.setSize(600, 600);
			int i = 0,j = 0;
			String numStr = "";
			
			for (i = 0; i < seqButton.length; i++) {
				for (j = 0; j < seqButton[i].length; j++) {
					
					numStr = sc.next();
					
					seqButton[i][j] = new SeqButton(i, j);
					
					// FileInputStream으로 읽어들인 값을 JButton에 삽입
					if (!(numStr.equals("*"))) {				
						if (numStr.equals("0")) { // 0, 클릭 못하게 => BLOCK_0
							seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_0.png"));
							seqButton[i][j].drawingAtButton = true;	// 의미없는 버튼이기 때문에 정답으로 체크
							//seqButton[i][j].setClickAble(false);
							// 버튼을 클릭되지 못하게 설정하면서, 색상은 회색이 아닌 본래의 색으로 하는 방법이 찾기
						} 
						else { // 그외, 클릭 가능, 흰색, 이벤트 처리, 스트링 값 정리 => BLOCK_1
							this.arrange(numStr,i, j);	// 스트링값 정리(중요!)
							seqButton[i][j].setImage(new ImageIcon("Images/BattleWord/BLOCK_1.png"));
							seqButton[i][j].setButton(this);	// 리스너 등록
							seqButton[i][j].setClickAble(true);
						}
						
						// 널 값으로 설정되어 있기 때문에 하나하나 설정
						seqButton[i][j].setSize(85, 85);
						if(i == 0)
							seqButton[i][j].setLocation(j*86, 86*0);
						else if(i == 1)
							seqButton[i][j].setLocation(j*86, 86*1);
						else if(i == 2)
							seqButton[i][j].setLocation(j*86, 86*2);
						else if(i == 3)
							seqButton[i][j].setLocation(j*86, 86*3);
						else if(i == 4)
							seqButton[i][j].setLocation(j*86, 86*4);
						else if(i == 5)
							seqButton[i][j].setLocation(j*86, 86*5);
						else if(i == 6)
							seqButton[i][j].setLocation(j*86, 86*6);
						
						this.add(seqButton[i][j]);
					} 
					else{
						System.out.println("GameFrame_wordSeq.Class에서 출력됩니다.");
						i = seqButton.length;
						break;
					}
				} // for
			} // for
		} catch (Exception e) {
			System.out.println("ERROR >> " + e);
			//System.out.println("파일을 찾지 못하였습니다.");
		}
		//딱 한번 호출
		this.sendButtonArr(this.seqButton);
	}// madePanel
	
	// 배열로 들어오는 스트링 값을 게임번호와 낱말길이로 분리하고 JButton에게 알려준다.
	public void arrange(String arrText, int i, int j){
		String firText = "";
		String secText = "";
		String splitText[];
		
		// 가로세로 모두 낱말 포함
		if(arrText.contains("_")){
			StringTokenizer stn = new StringTokenizer(arrText, "_");
			firText = stn.nextToken();
			secText = stn.nextToken();
			
			splitText = firText.split("&");				// 언더바로 나누어진 첫번째 토큰을 &으로 쪼갠다.
			this.firA = Integer.parseInt(splitText[0]);	// 게임번호(짝홀 여부에 따라 가로 세로 나뉨)
			this.firB = Integer.parseInt(splitText[1]);	// 낱말길이
			
			splitText = secText.split("&");				// 언더바로 나누어진 두번째 토큰을 &으로 쪼갠다.
			this.secA = Integer.parseInt(splitText[0]);	// 게임번호(짝홀 여부에 따라 가로 세로 나뉨)
			this.secB = Integer.parseInt(splitText[1]);	// 낱말길이
			
			// 짝수면 세로 낱말
			if(firA % 2 == 0){
				seqButton[i][j].setSeqButton(this.secA, this.firA, this.secB, this.firB, true, true);
			}
			else{
				seqButton[i][j].setSeqButton(this.firA, this.secA, this.firB, this.secB, true, true);
			}
		}
		// 하나만 포함
		else{
			firText = arrText;
			splitText = firText.split("&");
			firA = Integer.parseInt(splitText[0]);	// 게임번호(짝홀 여부에 따라 가로 세로 나뉨)
			firB = Integer.parseInt(splitText[1]);	// 낱말길이
			
			// 짝수면 세로 낱말
			if(firA % 2 == 0){
				seqButton[i][j].setSeqButton(0, firA, 0, firB, false, true);
			}
			// 홀수면 가로 낱말
			else{
				seqButton[i][j].setSeqButton(firA, 0, firB, 0, true, false);
			}
		}
	}//arrange
		
	
	// 낱말 퍼즐의 패널내에 낱말의 정답 유무에 따라서 
	// 각기 다른 판단 Label이 뜰 수 있도록 설정한다.
	// 처음 :: 화면이 열린다.
	// 맞춘 경우 :: Good
	// 틀린 경우 :: Fail
	// 시간 초과 :: Time Over
	// 모두 통과 :: WIN
	// 투명
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
//		System.out.println("wordSeqPanel : Paint()");
		
		if(this.mf.judgeEffectStr.equals("RightAns")){
			this.effectLabel[0].setVisible(true);
			this.effectLabel[0].setLocation(this.Image_X, this.Image_Y);
		}
		
		else if(this.mf.judgeEffectStr.equals("WrongAns")){
			this.effectLabel[1].setVisible(true);
			this.effectLabel[1].setLocation(this.Image_X, this.Image_Y);
		}
		
		else if(this.mf.judgeEffectStr.equals("TimeOver")){
			this.effectLabel[2].setLocation(50, 100);
			if (this.mf.bright) {
				this.effectLabel[2].setVisible(true);
				
			} else {
				this.effectLabel[2].setVisible(false);
			}
		}
		else if(this.mf.judgeEffectStr.equals("Perfect")){
			this.effectLabel[3].setLocation(10, 100);
			if (this.mf.PERFECT) {
				this.effectLabel[3].setVisible(true);
				
			} else {
				this.effectLabel[3].setVisible(false);
			}
		}
		else if(this.mf.judgeEffectStr.equals("Clear")){
			for(int i = 0; i < this.effectLabel.length; i++){
				this.effectLabel[i].setVisible(false);
			}
		}
	}
}


// 십자말풀이 안에 들어가는 버튼, 클릭 리스너 등록
// 버튼의 클릭은 TextArea의 설명과 연결되어있다.
class SeqButton extends JButton implements MouseListener{
	private wordSeqPanel wp;
	private int button_i = 0;			// 패널에 존재하는 버튼의 2차원 배열 인덱스 i
	private int	button_j = 0;			// 패널에 존재하는 버튼의 2차원 배열 인덱스 j
	private int x, y;					// X와 Y의 위치 값
	protected int gameNum_W = 0;		// 게임 번호(가로:홀수)
	protected int gameNum_H = 0;		// 게임 번호(세로:짝수)
	private int wordLen_W = 0;			// 낱말의 길이(가로:홀수)
	private int wordLen_H = 0;			// 낱말의 길이(세로:짝수)
	protected boolean width = false;	// 가로 낱말 존재 여부
	protected boolean height = false;	// 세로 낱말 존재 여부
	private boolean clickable = false;	// 클릭이 되지 않는 요소는 검은색으로 처리할 예정
	
	private String before_pos = "NONE";	// JButton이 다른 버튼을 클릭했을때 동일한 문제인지 여부
	private String pos = "NONE";		// JButton 상에서의 좌표값이 상하좌우를 저장
	
	protected boolean drawingAtButton = false;	// 클릭된 이미지 버튼의 정답여부
	protected String ownerStringValue = "";		// 소유하고 있는 낱말의 한 음절
	
	// 매개변수 있는 생성자이며, 이것을 통해서 인덱스 i, j의 값을 받는다.
	public SeqButton(int i, int j){
		//System.out.println(i + ", " + j);
		this.button_i = i;
		this.button_j = j;
		//System.out.println(button_i + ", " + button_j);
	}
	// 마우스 리스너 등록
	public void setButton(wordSeqPanel wp){
		this.addMouseListener(this);
		this.wp = wp;
	}

	// 클릭 여부 설정, false는 클릭할 수 없도록 true는 클릭은 가능하며 그 JButton은 JTextArea의 객체를 반환
	public void setClickAble(boolean able){
		this.clickable = able;
		if(!this.clickable)
			this.setEnabled(clickable);
	}
	
	// JButton내에서의 좌표값을 찍어준다. => 클릭한 좌표 패널값에 전달
	// 찍은 좌표값에 따라서 위로 향하고 있는 혹은 아래로 향하고 있는 부분에 대해서 설명이 보이게끔
	@Override
	public void mousePressed(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
		
		// 마우스에서 클릭한 좌표의 상하좌우를 확인
		String pos = (new LocationXY()).Location_XY(this.x, this.y, this.width, this.height, this.gameNum_W, this.gameNum_H);
	
		// 마우스 클릭으로 찍힌 버튼의 인덱스 i, j 를 전달
		wp.setJButton(this.button_i, this.button_j);
		wp.setPos_XY(pos, this.gameNum_W, this.gameNum_H, this.wordLen_W, this.wordLen_H);
	}
	
	// 게임 번호(가로:홀수), 게임 번호(세로:짝수), 
	// 낱말의 길이(가로:홀수), 낱말의 길이(세로:짝수), 
	// 가로 낱말 존재 여부, 낱말의 길이(세로:짝수)
	public void setSeqButton(int gameNum_W, int gameNum_H, int wordLen_W, int wordLen_H, boolean width, boolean height){
		this.gameNum_W = gameNum_W;
		this.gameNum_H = gameNum_H;
		this.wordLen_W = wordLen_W;
		this.wordLen_H = wordLen_H;
		this.width = width;
		this.height = height;
	}
	
	// 텍스트 필드로 받은 문자열의 값이 정답인 경우, 
	// 폰트 설정 이후에 setText() 설정
	public void setWordAtButton(String charWord){
		this.setFont(new Font("Gothic", Font.BOLD, 20));
		this.setText(charWord);
	}
	
	// JButton을 상속받은 SeqButton에 setImage를 통해 버튼에 이미지를 형성
	// 중앙으로 정렬
	public void setImage(ImageIcon ButtonIcon){
		this.setIcon(ButtonIcon);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	
	// 이미지 버튼에 글씨를 써 넣는다.
	// 만약에 정답일 경우 글자의 이미지를 보이게 한다. 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
//		System.out.println("seqButton : paint()");
	
		Graphics2D g2 = (Graphics2D) g;
		// 이미지 버튼이 정답으로 클릭되었을 경우에
		// 그 이미지 버튼위에 글씨를 그린다.
		if(drawingAtButton){
			Font font = new Font("나눔고딕", Font.BOLD, 35);
			g2.setFont(font);
			g2.setColor(Color.BLUE);
			g2.setColor(new Color(12, 86, 212));
			g2.drawString(ownerStringValue, 27, 52);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}

// JButton의 위치를 계산하는 클래스 및 메소드
class LocationXY{
	// 중심 좌표 값(42, 42)으로부터 상, 하, 좌, 우, 위치값
	private int y_Left;
	private int y_Right;
	
	public LocationXY(){}
	
	// 좌표 값을 받아서 좌표의 위치를 통해 게임번호가 홀 인지 짝인지 구분
	// JButton의 중심값은 42(x), 42(y)
	public String Location_XY(int x, int y, boolean width, boolean height, int gameNum_W, int gameNum_H){
		
		// 가로 세로의 낱말이 존재하는 경우
		if(width && height){
			// (x >= 42)
			if (x >= 42) {
				this.y_Left = (42 - x) + 42;
				this.y_Right = (42 + x) - 42;

				if (y > y_Left && y < y_Right) {
					return "Right";	// 우측
				}
				else if(y <= y_Left){
					return "Up";	// 상
				}
				else if(y >= y_Right){
					return "Down";	// 하
				}
				
			}
			// (x < 42)
			else{
				this.y_Left = (42 + x) - 42;
				this.y_Right = (42 - x) + 42;
				
				if(y > y_Left && y < y_Right){
					return "Left";	// 좌측
				}
				else if(y <= y_Left){
					return "Up";	// 상
				}
				else if(y >= y_Right){
					return "Down";	// 하
				}
			}
		}// 큰 if (가로 세로 낱말이 모두 존재하는 경우)
		
		
		// 가로의 낱말만 존재하는 경우
		// 버튼 좌표의 전체는 그 버튼이 속한 낱말의 설명만 보일수 있도록 설정
		else if(width){
			return "All";
		}// else if
		
		// 세로의 낱말만 존재하는 경우
		// 버튼 좌표의 전체는 그 버튼이 속한 낱말의 설명만 보일수 있도록 설정
		else if(height){
			//System.out.println("+++++++++++++");
			return "All";
		}// else if
		
		// 조건문에서 값을 위치값을 확인하지 못한 경우
		return "X";
	}
}
public class GameFrame_wordSeq {
	public static void main(String[]args){}
}
