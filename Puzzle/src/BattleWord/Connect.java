package BattleWord;

import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.sql.*;

class MySqlConnect{
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String URL;
	String uId;
	String uPw;
	String query = "";
	
	public Scanner in;
	public MySqlConnect(){}
	
	public void setting(){
		this.URL = "jdbc:mysql://localhost/wordgame";
		this.uId = "swing";
		this.uPw = "swingpass";
	}// setting : URL, id, pw 설정
	
	public void Connecting(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, uId, uPw);
			
			System.out.println("mysql 연동 성공");
		}
		catch(SQLException e){
			System.out.println("mysql 연동 실패");
		}
		catch(ClassNotFoundException e){
			System.out.println("class Not Found");
		}
		finally{
			//나중에 구현
		}
	}// Connecting : MySQL을 연결하기 위한 작업
	
	
	// 파일 입출력을 통해 읽어들인 값을 디비에 저장한다.
	public void insertDB(int gameType, int gameNum, String gEx, String cAns){
		int i = 1;
		
		try{
			query = "INSERT INTO gameTable VALUES(?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, gameType);
			pstmt.setInt(2, gameNum);
			pstmt.setString(3, gEx);
			pstmt.setString(4, cAns);
			pstmt.executeUpdate();
			
			System.out.println("Insert()");
		}
		catch(SQLException e){
			System.out.println("INSERT ERROR");
		}
	}
	
	// 파일을 읽어들이고 값을 디비에 저장
	public void readFileAndInsertDB(){
		int gameNum = 0;
		String gEx  = "";	// 낱말 설명
		String cAns = "";	// 낱말 정답
		
		try {
			// fileIdx는 게임의 타입이다.
			for (int fileIdx = 1; fileIdx <= 1; fileIdx++) {
				this.in = new Scanner(
						new FileInputStream("src/WordSource/game" + Integer.toString(fileIdx) + "_explain.text"));
				String textLine = "";
				
				while (in.hasNextLine()) {
					textLine = in.nextLine();

					// 별이 아니면 값을 디비에 저장
					if (!textLine.startsWith("*")) {
						// (공백과 콜론을 기준으로 자름)
						StringTokenizer strToken1 = new StringTokenizer(textLine, " ");
						StringTokenizer strToken2 = new StringTokenizer(textLine, ":");

						// 숫자 저장
						gameNum = Integer.parseInt(strToken1.nextToken());

						// 문제의 정답 저장
						cAns = strToken1.nextToken();

						// 낱말의 설명 저장
						strToken2.nextToken();
						gEx = strToken2.nextToken();
						
						System.out.println(fileIdx);
						System.out.println(gameNum);
						System.out.println(cAns);
						System.out.println(gEx);
						
						// 저장된 값을 DB로 전달
//						this.insertDB(fileIdx, gameNum, gEx, cAns);

						in.nextLine();
					}
					// 별이면 종료
					else {
						System.out.println("game" + Integer.toString(fileIdx) + "_explain.text 다 읽어들임");
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR >> " + e);
		}
	} // 데이터 삽입하는 메소드
	
	
	// 문제의 설명을 얻는 메소드(게임타입, 게임번호)
	// 게임의 타입과 번호를 추출하여 문제의 설명을 얻어냄
	public String getGameEx(int gameType_P, int gameNum_P){
		String gameEx = "";
		String gameType = Integer.toString(gameType_P);
		String gameNum = Integer.toString(gameNum_P);
	
		try{
			String getQuery = "SELECT GameEx FROM gameTable WHERE GameType=" + gameType + " AND GameNum=" + gameNum;
//			System.out.println("문제의 설명 쿼리 : " + getQuery);
			pstmt = conn.prepareStatement(getQuery);
			rs = pstmt.executeQuery();
			
			rs.next();
			gameEx = rs.getString("GameEx");
		}
		catch(SQLException e){
			System.out.println(e);
		}
		System.out.println("DB에서 추출한 값 : " + gameEx);
		return gameEx;
	}
	
	// 문제의 정답을 얻는 메소드(게임타입, 게임번호)
	// 게임의 타입과 번호를 추출하여 문제의 설명을 얻어냄
	public String getGameAns(int gameType_P, int gameNum_P){
		String gameAns = "";
		String gameType = Integer.toString(gameType_P);
		String gameNum = Integer.toString(gameNum_P);
		
		try{
			String getQuery = "SELECT CorrectAns FROM gameTable WHERE GameType=" + gameType + " AND GameNum=" + gameNum;
			//System.out.println("문제의 정답 쿼리 : " + getQuery);
			pstmt = conn.prepareStatement(getQuery);
			rs = pstmt.executeQuery();
			
			rs.next();
			gameAns = rs.getString("CorrectAns");
		}
		catch(Exception e){
			System.out.println(e);
		}
		System.out.println("DB에서 추출한 값 : " + gameAns + "\n");
		return gameAns;
	}
	
	/*************************************************************************************************************/
	// 파일 입출력으로 정보를 얻어오기 위한 메소드
	// 디비가 아닌 파일 입출력을 통한 정보를 얻어온다.
	public String getFILE_GameEx(int gameType_P, int gameNum_P){
		String gameEx = "";
		String gameType = Integer.toString(gameType_P);
		String gameNum = Integer.toString(gameNum_P);
		String textLine = "";
		
		try{
			Scanner in = new Scanner(new FileInputStream("src/WordSource/game" + Integer.toString(wordSeqPanel.puzzleType) + "_explain.text"), "UTF-8");
			
			while (in.hasNextLine()) {
				textLine = in.nextLine();
				// 만약 게임번호가 텍스트 라인의 번호와 동일하지않으면
				if (!textLine.startsWith(Integer.toString(gameNum_P))) {
					in.nextLine();
				}
				// 동일하면 설명을 보낸다.
				else {
					// (콜론을 기준으로 자름)
					// 낱말의 설명 저장
					StringTokenizer strToken = new StringTokenizer(textLine, ":");
					strToken.nextToken();
					gameEx = strToken.nextToken();
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println("ERROR FILE INPUT AND OUTPUT >> ");
			System.out.println(e);
		}
		
		
		System.out.print("\nFILE OUTPUT EX >> ");
		System.out.println(gameEx);
		return gameEx;
	}
	
	
	public String getFILE_GameAns(int gameType_P, int gameNum_P){
		String gameAns = "";
		String gameType = Integer.toString(gameType_P);
		String gameNum = Integer.toString(gameNum_P);
		String textLine = "";
		
		try{
			Scanner in = new Scanner(new FileInputStream("src/WordSource/game" + Integer.toString(wordSeqPanel.puzzleType) + "_explain.text"), "UTF-8");
			
			while (in.hasNextLine()) {
				textLine = in.nextLine();
				// 만약 게임번호가 텍스트 라인의 번호와 동일하지않으면
				if (!textLine.startsWith(Integer.toString(gameNum_P))) {
					in.nextLine();
				}
				// 동일하면 정답을 보낸다.
				else {
					// (콜론을 기준으로 자름)
					// 낱말의 설명 저장
					StringTokenizer strToken = new StringTokenizer(textLine, " ");
					strToken.nextToken();
					gameAns = strToken.nextToken();
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println("ERROR FILE INPUT AND OUTPUT >> ");
			System.out.println(e);
		}
		
		System.out.print("FILE OUTPUT Ans >> ");
		System.out.println(gameAns);
		return gameAns;
	}
	/*************************************************************************************************************/
}


// mysql과 연동하기 위한 클래스
public class Connect {
	MySqlConnect msc;
	
	public static void main(String[]args){
		Connect c = new Connect();
		c.msc = new MySqlConnect();
		
		c.msc.setting();
		c.msc.Connecting();
		//c.msc.readFileAndInsertDB();
		//c.msc.getGameEx(1, 1);
		//c.msc.getGameAns(1, 1);
	}
}
