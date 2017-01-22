import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class BluestRed implements KeyListener{

	static JFrame frame;
	static boolean run = false;

	public static void init(){
		frame = new JFrame();
		frame.setSize(1200,900);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new BluestRed());
		frame.setTitle("On Tilt");
		for(int i = 0; i <= 1200; i+= 50){
			for(int j = 0; j < Math.random()*1+1; j++) Block.generate(i);	
		}
		Player.player1 = new Player(200,50,"P1");
		Player.player2 = new Player(900,50,"P2");
		Player.player1.color = Color.blue;
		Player.player2.color = Color.red;
	}

	public static void update(){
		Player.player1.update();
		Player.player2.update();
		Player.collide();
		Player.hit();
		Block.update();
		frame.add(new Canvas());
		frame.setVisible(true);
		try{
			Thread.sleep(10);
		}catch(Exception e){

		}
	}

	public static class Canvas extends JPanel{
		static boolean start = true;
		static int count = 3;
		public void paintComponent(Graphics g2){
			Graphics2D g = (Graphics2D) g2;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(new Color(25,25,30));
			g.fillRect(0,0,frame.getWidth(),frame.getHeight());
			for(int i = 0; i < Block.all.size(); i++){
				Block b = Block.all.get(i);
				g.setColor(b.color);
				g.fillRect((int)b.x+4,(int)b.y+4,50,8);
			}
			g.setColor(Player.player1.color);
			g.fillOval((int)(Player.player1.x-Player.player1.size),(int)(Player.player1.y-Player.player1.size),Player.player1.size*2,Player.player1.size*2);
			g.drawString(Player.player1.name,(int)Player.player1.x+Player.player1.size*2,(int)Player.player1.y+Player.player1.size*2);
			g.setColor(Player.player2.color);
			g.fillOval((int)(Player.player2.x-Player.player2.size),(int)(Player.player2.y-Player.player2.size),Player.player2.size*2,Player.player2.size*2);
			g.drawString(Player.player2.name,(int)Player.player2.x+Player.player2.size*2,(int)Player.player2.y+Player.player2.size*2);
			if(start) starter(g,count);
		}
	}

	public static class Lost extends JPanel{
		public void paintComponent(Graphics g2){
			Graphics2D g = (Graphics2D) g2;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				g.setFont(new Font("Arial", Font.BOLD, 50)); 
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0,0,frame.getWidth(),frame.getHeight());
			if(Player.player1.lost){
				g.setColor(Color.red);
				g.drawString("P2 WON",460,450);	
			}	
			else{
				g.setColor(Color.blue);
				g.drawString("P1 WON",460,450);		
			}
		}
	}

	public static class Menu extends JPanel{
		public void paintComponent(Graphics g2){
			Graphics2D g = (Graphics2D) g2;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0,0,frame.getWidth(),frame.getHeight());
			g.setColor(Color.white);
			g.drawString("INSTRUCTIONS:",400,100);
			g.drawString("#1. Land on a platform with same color as yourself to grow bigger",200,200);
			g.drawString("#2. Landing on a platform with different color reduces your size",200,300);
			g.drawString("#3. The larger player will knock the other down during contact",200,400);
			g.drawString("#4. Dont fall, or else you lose",200,500);
			g.drawString("#5. P1 controls are s and d, P2 controls k and l",200,600);
			g.drawString("#6. Press r to insert coin and begin the game!",200,700);
		}
	}

	public static void starter(Graphics g, int count){
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 100)); 
		g.drawString(count+"",525,450);
		Canvas.count--;
	}
	
	public static void main(String[] args){
		init();
		while(true){
			Canvas.start = true;
			frame.add(new Menu());
			frame.setVisible(true);
			while(!run){
				try{Thread.sleep(2);}catch(Exception e){}
			}
			for(int i = 0; i <= 3; i++){
				update();
				try{Thread.sleep(500);}catch(Exception e){}
			}
			while(true){
				update();
				Canvas.start = false;
				Player.lost();
				if(Player.player1.lost || Player.player2.lost){
					frame.add(new Lost());
					frame.setVisible(true);
					try{Thread.sleep(1000);}catch(Exception e){}
					Block.all = new ArrayList<Block>();
					Block.fallen = new ArrayList<Block>();
					for(int i = 0; i <= 1200; i+= 50){
						for(int j = 0; j < Math.random()*1+1; j++) Block.generate(i);	
					}
					Player.player1 = new Player(75,50,"P1");
					Player.player2 = new Player(1125,50,"P2");
					Player.player1.color = Color.red;
					Player.player2.color = Color.blue;
					Canvas.count = 3;
					run = false;
					break;
				}
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S){
			Player.player1.shift += -3.6;
		}
		else if(e.getKeyCode() == KeyEvent.VK_D){
			Player.player1.shift += 3.6;
		}
		else if(e.getKeyCode() == KeyEvent.VK_K){
			Player.player2.shift += -3.6;
		}
		else if(e.getKeyCode() == KeyEvent.VK_L){
			Player.player2.shift += 3.6;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R){
			run = !run;
		}
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S){
			Player.player1.shift = 0;
		}
		else if(e.getKeyCode() == KeyEvent.VK_D){
			Player.player1.shift = 0;
		}
		else if(e.getKeyCode() == KeyEvent.VK_K){
			Player.player2.shift = 0;
		}
		else if(e.getKeyCode() == KeyEvent.VK_L){
			Player.player2.shift = 0;
		}
	}
	public void keyTyped(KeyEvent arg0) {
	}
}
