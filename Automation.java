
/**
 * Automation
 */
// Java program to move a mouse from the initial
// location to a specified location
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Little project that can automate a certain process for a website
 */
public class Automation extends Frame implements ActionListener {
	static JFrame frame;
	int startingChar_Id = 65;
	Robot r;
	int sleepDuration = 1000;

	public static void main(String[] args) {
		Automation a = new Automation();

		frame = new JFrame("Thing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 500);

		Button start = new Button("Start");
		start.addActionListener(a);

		Panel p = new Panel();
		p.add(start);

		frame.add(p);

		frame.setSize(100, 90);
		frame.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		sleep(2000);
		try {
			r = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		while (startingChar_Id < 94) {
			sleep(sleepDuration);
			r.keyPress(startingChar_Id);
			startingChar_Id++;

			sleep(sleepDuration);
			r.keyPress(10);

			sleep(sleepDuration);
			r.mousePress(16);
			r.mousePress(16);

		}

		r.mouseRelease(16);
	}

	void sleep(long duration) {
		try {
			Thread.sleep(duration);
		} catch (Exception e) {
		}
	}
}
