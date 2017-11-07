package BattleWord;

import BattleWord.GameFrame_MainFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// 십자말풀이 게임의 로고가 보여지는 패널
class logoPanel extends Panel implements Runnable{
	private MainFrame mf;
	private Image logoImage[];
	private Thread logoThread;
	private int n = 0;
	
	public logoPanel(MainFrame mf){
		this.mf = mf;
		//this.setBackground(Color.BLACK);
		this.setLayout(null);

		this.setImage();
		this.attachPanel();
		this.startThread();
		
		this.logoThread.start();
	}
	
	
	public void setImage(){
		this.logoImage = new Image[2];
		for (int i = 0; i < logoImage.length; i++) {
			logoImage[i] = new ImageIcon("Images/BattleWord/MainLogo_" + Integer.toString(i + 1) + ".png").getImage();
		}
	}
	
	public void attachPanel(){
		this.setSize(900, 100);
		this.setLocation(0, 0); // 프레임 내에서 위치 조정
		mf.add(this);
	}

	public void startThread(){
		this.logoThread = new Thread(this);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(this.logoImage[n], -2, 0, this);
	}
	
	@Override
	public void run() {
		while(true){
			try{
				this.n = 0;
				this.repaint();
				Thread.sleep(1000);
				
				this.n = 1;
				this.repaint();
				Thread.sleep(1000);
			}
			catch(Exception e){
				
			}
		}
	}
}
public class GameFrame_logoTitle {}
