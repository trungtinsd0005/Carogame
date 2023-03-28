import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

public class Caro extends JFrame implements ActionListener {
	Color background_cl = Color.white;
	Color x_cl = Color.red;
	Color y_cl = Color.blue;
	int column = 15, row = 15, count = 0;
	int xUndo[] = new int[column * row];
	int yUndo[] = new int[column * row];
	boolean tick[][] = new boolean[column + 2][row + 2];
	int Size = 0;
	Container cn;
	JPanel pn, pn2;
	JLabel lb;
	JLabel timerX, timerO;
	JButton newGame_bt, undo_bt, exit_bt;
	public String turnX = "Lượt của X";
	public String turnO = "Lượt của O";
	private JButton b[][] = new JButton[column + 2][row + 2];
	public StopWatch stopWatch1;
	String timeLose = "10:000";
	public StopWatch2 stopWatch2;
	private boolean paused1;
	private boolean paused2;
	private JLabel labelTimeX, labelTimeY;
	public Caro(String s) {
		super(s);
		stopWatch1 = new StopWatch();
		stopWatch2 = new StopWatch2();
		cn =this.getContentPane();
		pn = new JPanel();
		pn.setLayout(new GridLayout(column,row));
		for (int i = 0; i <= column + 1; i++)
			for (int j = 0; j <= row + 1; j++) {
				b[i][j] = new JButton("");
				b[i][j].setActionCommand(i + " " + j);
				b[i][j].setBackground(background_cl);
				b[i][j].addActionListener(this);
				tick[i][j] = true;
			}
		for (int i = 1; i <= column; i++)
			for (int j = 1; j <= row; j++)
				pn.add(b[i][j]);
		lb = new JLabel("X Đánh Trước");
		newGame_bt = new JButton("New Game");
		undo_bt = new JButton("Undo");
		exit_bt = new JButton("Exit");
		labelTimeX = new JLabel("Thời gian chơi của X:");
		timerX = new JLabel("00:000");
		labelTimeY = new JLabel("Thời gian chơi của O:");
		timerO = new JLabel("00:000");
		newGame_bt.addActionListener(this);
		undo_bt.addActionListener(this);
		exit_bt.addActionListener(this);
		exit_bt.setForeground(x_cl);
		cn.add(pn);
		pn2 = new JPanel();
		pn.setSize(400, 400);
		pn2.setLayout(new FlowLayout());
		pn2.add(lb);
		pn2.add(labelTimeX);
		pn2.add(timerX);
		pn2.add(newGame_bt);
		pn2.add(undo_bt);
		pn2.add(exit_bt);
		pn2.add(labelTimeY);
		pn2.add(timerO);
		cn.add(pn2,"North");
		cn.setSize(400, 400);
		this.setVisible(true);
		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		undo_bt.setEnabled(false);
	}
	public boolean checkWin(int i, int j) {
		int d = 0, k = i, h;
		// kiểm tra hàng
		while (b[k][j].getText() == b[i][j].getText()) {
			d++;
			k++;
		}
		k = i - 1;
		while (b[k][j].getText() == b[i][j].getText()) {
			d++;
			k--;
		}
		if (d > 4) return true;
		d = 0; h = j;
		// kiểm tra cột
		while(b[i][h].getText() == b[i][j].getText()) {
			d++;
			h++;
		}
		h = j - 1;
		while(b[i][h].getText() == b[i][j].getText()) {
			d++;
			h--;
		}
		if (d > 4) return true;
		// kiểm tra đường chéo 1
		h = i; k = j; d = 0;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h++;
			k++;
		}
		h = i - 1; k = j - 1;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h--;
			k--;
		}
		if (d > 4) return true;
		// kiểm tra đường chéo 2
		h = i; k = j; d = 0;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h++;
			k--;
		}
		h = i - 1; k = j + 1;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h--;
			k++;
		}
		if (d > 4) return true;
		// nếu không đương chéo nào thỏa mãn thì trả về false.
		return false;
	}
	public void undo() {
		if (Size > 0) {
			b[xUndo[Size - 1]][yUndo[Size - 1]].setText(" ");
			b[xUndo[Size - 1]][yUndo[Size - 1]].setActionCommand(xUndo[Size - 1]+ " " + yUndo[Size - 1]);
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(background_cl);
			tick[xUndo[Size - 1]][yUndo[Size - 1]] = true;
			count--;
			if (count % 2 == 0) lb.setText("Lượt Của X"); 
				else lb.setText("Lượt Của O");
			Size--;
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(Color.gray);
			if (Size == 0)
				undo_bt.setEnabled(false);
		}
	}
	public void addPoint(int i, int j) {
		if (Size > 0)
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(background_cl);
		xUndo[Size] = i;
		yUndo[Size] = j;
		Size++;
		if (count % 2 == 0) {
			b[i][j].setText("X");
			b[i][j].setForeground(x_cl);
			lb.setText("Lượt Của O");
			// paused1 = true;
			startTimerO();
			
			stopWatch1.pause();
			stopWatch2.resume();
		}else {
			b[i][j].setText("O");
			b[i][j].setForeground(y_cl);
			lb.setText("Lượt Của X");
			// paused2 = true;
			startTimerX();
			stopWatch2.pause();
			stopWatch1.resume();
			
			// stopWatch2.resume();
		}
		count = 1 - count;
		tick[i][j] = false;
		b[i][j].setBackground(Color.GRAY);
		undo_bt.setEnabled(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "New Game") {
			new Caro("CODELEARN - GAME DEMO");
			this.dispose();
		}
		else
		if (e.getActionCommand() == "Undo") {
			undo();
		} 
		else
		if (e.getActionCommand() == "Exit") {
			System.exit(0);;
		}
		else {
			String s = e.getActionCommand();
			int k = s.indexOf(32);
			int i = Integer.parseInt(s.substring(0, k));
			int j = Integer.parseInt(s.substring(k + 1, s.length()));
			if (tick[i][j]) {
				if(lb.getText().equals("Lượt Của O")) {
					// stopWatch2.resume();
					paused2 = false;
				}else if(lb.getText().equals("Lượt Của X")) {
					// stopWatch1.resume();
					// stopWatch2.resume();
					paused1 = false;
				}
				addPoint(i, j);
				
			}
			if (checkWin(i, j)) {
				lb.setBackground(Color.MAGENTA);
				for (i = 1; i <= column; i++)
				for (j = 1; j <= row; j++) 
				b[i][j].setEnabled(false);
				undo_bt.setEnabled(false);
				newGame_bt.setBackground(Color.YELLOW);
				stopWatch1.stop();
				paused1 = true;
				stopWatch2.stop();
				paused2 = true;
				JOptionPane.showMessageDialog(null, "Trò chơi kết thúc");
			}
		}	
	}
	
	private void startTimerX() {
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                stopWatch1.start();
                while (true) {
                    if (!paused1) {
                        final String timeString = new SimpleDateFormat("ss:SSS").format(stopWatch1.getElapsedTime());
                        timerX.setText("" + timeString);
                    }

					if(timerX.getText().equals(timeLose)) {
						stopWatch1.stop();
						JOptionPane.showMessageDialog(null, "X đã thua");
						break;
					}
                }
            }
        });
//        jButton_resume.setEnabled(false);
        thread1.start();
    }
	
	private void startTimerO() {
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                stopWatch2.start();
                while (true) {
                    if (!paused2) {
                        final String timeString = new SimpleDateFormat("ss:SSS").format(stopWatch2.getElapsedTime());
                        timerO.setText("" + timeString);
                    }

					if(timerO.getText().equals(timeLose)) {
						stopWatch2.stop();
						JOptionPane.showMessageDialog(null, "O đã thua");
						break;
					}
                }
            }
        });
//        jButton_resume.setEnabled(false);
        thread2.start();
    }
	public static void main(String[] args) {
		new Caro("Caro game");
	}
}