import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class CoordinateTheFingers {

	public static void main(String[] args) {

		JFrame frame = new JFrame("Visualize X Axis");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		YAxisLine yAxisLine = new YAxisLine();
		frame.add(yAxisLine);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);

		Controller controller = new Controller();

		float oldy = 0;
		while (true) {
			if (controller.isConnected()) {
				HandList hands = controller.frame().hands();
				Hand hand = hands.get(0);
				if (hand != null) {
					float y = hand.palmPosition().getY();
					if (oldy != y) {
						yAxisLine.setHands(hands);
						oldy = y;
					}
				}
			}
			yAxisLine.setConnected(controller.isConnected());
		}
	}

	static class YAxisLine extends JPanel {

		private static final int WIDTH = 260;
		private static final int HEIGHT = 730;

		private static final long serialVersionUID = 1L;
		private boolean connected = true;
		private HandList handList;
		private NumberFormat numberFormat;

		public YAxisLine() {
			numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(WIDTH, HEIGHT);
		}

		@Override
		protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			drawAbstractLM(graphics);

			if (handList != null && connected) {
				drawLine(graphics);
			}
		}

		private void drawLine(Graphics graphics) {
			graphics.setColor(Color.BLACK);
			if (handList.count() > 0) {
				int factor = 0;
				if (handList.count() == 1) {
					factor = 2;
				} else {
					factor = 1;
				}
				for (Hand hand : handList) {
					int roundedY = (int) (Math
							.round(hand.palmPosition().getY()));
					int four = (WIDTH / 4) * factor;
					int border = HEIGHT - 40;
					int height = HEIGHT - 40 - roundedY;
					String cmString = "h: "
							+ numberFormat.format((double) hand.palmPosition()
									.getY() / 10) + " cm";
					String yString = "y: "
							+ (Math.round(hand.palmPosition().getY()));
					String fingerString = "f: " + hand.fingers().count();

					graphics.drawLine(four, border, four, height);
					graphics.drawLine(four - 10, height, four + 10, height);
					graphics.drawString(fingerString, four - 20, height - 50);
					graphics.drawString(cmString, four - 20, height - 30);
					graphics.drawString(yString, four - 20, height - 10);

					factor += 2;
				}
			}
		}

		private void drawAbstractLM(Graphics graphics) {
			graphics.setColor(Color.GRAY);
			graphics.drawRoundRect(WIDTH / 4, HEIGHT - 40, WIDTH / 2, 30, 5, 5);
			graphics.fillRoundRect(WIDTH / 4, HEIGHT - 40, WIDTH / 2, 30, 5, 5);

			graphics.setColor(!connected ? Color.RED : Color.GREEN);
			graphics.fillRoundRect((WIDTH / 5) * 3, HEIGHT - 22, 18, 6, 3, 3);
		}

		public void setHands(HandList handList) {
			this.handList = handList;
			repaint();
		}

		public void setConnected(boolean connected) {
			this.connected = connected;
			repaint();
		}
	}
}