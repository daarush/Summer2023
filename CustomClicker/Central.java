import java.awt.AWTException;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Central
 */
public class Central {
    public static JFrame frame;
    public static Point mousePos;

    static JFormattedTextField xCoordsTextField;
    static JFormattedTextField yCoordsTextField;

    static JFormattedTextField delayInpuTextField;
    static JFormattedTextField initialDelayTextField;

    static Checkbox useCoordsEnteredCheckbox;
    static int x_coord;
    static int y_coord;

    static JButton recordCoordsButton;
    static JButton startButton;

    static boolean isOnRecordAction;
    static boolean isClicking;
    static boolean beginningSleepDone;

    public static void main(String[] args) throws IOException, AWTException, InterruptedException {

        isOnRecordAction = false;
        isClicking = false;
        beginningSleepDone = false;

        initializeWindow();
        Robot r = new Robot();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("                                                                                   ", mouseTab());
        frame.add(tabbedPane);

        frame.setFocusable(true);
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                System.out.println("sdasdasasdasd");
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (isOnRecordAction == true && isClicking == false) {
                        isOnRecordAction = false;
                        System.out.println("getXCoord()");
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        frame.setVisible(true);

        while (true) {
            setCoordsButtonState();
            setStartButtonState();

            if (isOnRecordAction == true) {
                mousePos = MouseInfo.getPointerInfo().getLocation();
                setXCoord(mousePos.x);
                setYCoord(mousePos.y);
            } else {
                if (isClicking == true) {
                    if (beginningSleepDone == false) {
                        Thread.sleep(Long.parseLong(getInitialDelayInput()));
                        beginningSleepDone = true;
                        if (getIfUsingCoords() == false) {
                            mousePos = MouseInfo.getPointerInfo().getLocation();
                            setXCoord(mousePos.x);
                            setYCoord(mousePos.y);
                        } else {
                            int x = Integer.parseInt(getXCoord().replaceAll(",", ""));
                            int y = Integer.parseInt(getYCoord().replaceAll(",", ""));

                            r.mouseMove(x, y);
                        }

                    }

                    if (getIfUsingCoords() == true) {
                        int x = Integer.parseInt(getXCoord().replaceAll(",", ""));
                        Point newMousePos = MouseInfo.getPointerInfo().getLocation();
                        if (newMousePos.x != x) {
                            isClicking = false;
                        }

                    } else {
                        Point newMousePos = MouseInfo.getPointerInfo().getLocation();
                        if (newMousePos.x != mousePos.x) {
                            isClicking = false;
                        }
                    }

                    int ms = Integer.parseInt(getDelayInput());
                    r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    r.delay(ms);
                }
            }
        }

    }

    private static void initializeWindow() {
        frame = new JFrame("Custom Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width - frame.getSize().width / 1, dim.height / 2 - frame.getSize().height / 2);
    }

    private static Component mouseTab() {
        JPanel mainPanel = new JPanel();

        JPanel coordsPanel = new JPanel();

        JPanel xPanel = new JPanel();
        JLabel xLabel = new JLabel("X Pos: ");

        xCoordsTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        xCoordsTextField.setValue(0);
        xCoordsTextField.setColumns(3);

        xPanel.add(xLabel);
        xPanel.add(xCoordsTextField);

        JPanel yPanel = new JPanel();
        JLabel yLabel = new JLabel("Y Pos: ");

        yCoordsTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        yCoordsTextField.setValue(0);
        yCoordsTextField.setColumns(3);

        yPanel.add(yLabel);
        yPanel.add(yCoordsTextField);

        coordsPanel.add(xPanel);
        coordsPanel.add(yPanel);

        JPanel recordCoordsPanel = new JPanel();
        recordCoordsButton = new JButton("Click to start recording mouse coords");
        recordCoordsButton.setFont(new Font("Arial", Font.BOLD, 11));
        recordCoordsButton.setFocusable(false);
        recordCoordsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isOnRecordAction == false) {
                    isOnRecordAction = true;
                    setCoordsButtonState();
                } else {
                    isOnRecordAction = false;
                    setCoordsButtonState();
                }
            }
        });

        useCoordsEnteredCheckbox = new Checkbox("Use Entered Coords Position");
        useCoordsEnteredCheckbox.setFocusable(false);
        recordCoordsPanel.add(useCoordsEnteredCheckbox);
        recordCoordsPanel.add(recordCoordsButton);

        JPanel delayPanel = new JPanel();
        JLabel delayLabel = new JLabel("Delay (ms): ");
        delayInpuTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        delayInpuTextField.setValue(0);
        delayInpuTextField.setColumns(3);
        delayInpuTextField.setText("500");

        JLabel initialDelayLabel = new JLabel("Initial Delay (ms): ");
        initialDelayTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        initialDelayTextField.setValue(0);
        initialDelayTextField.setColumns(3);
        initialDelayTextField.setText("3000");

        delayPanel.add(delayLabel);
        delayPanel.add(delayInpuTextField);
        delayPanel.add(initialDelayLabel);
        delayPanel.add(initialDelayTextField);

        JPanel startClickPanel = new JPanel();
        startButton = new JButton();
        startButton.setFont(new Font("Arial", Font.BOLD, 11));
        startButton.setFocusable(false);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isClicking == false) {
                    beginningSleepDone = false;
                    isClicking = true;
                    setStartButtonState();
                } else {
                    isClicking = false;
                    setStartButtonState();
                }
            }
        });
        startClickPanel.add(startButton);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(coordsPanel);
        mainPanel.add(recordCoordsPanel);
        mainPanel.add(delayPanel);
        mainPanel.add(startClickPanel);

        return mainPanel;
    }

    public static String getXCoord() {
        return xCoordsTextField.getText();
    }

    public static void setXCoord(int num) {
        xCoordsTextField.setValue(num);
    }

    public static String getYCoord() {
        return yCoordsTextField.getText();
    }

    public static void setYCoord(int num) {
        yCoordsTextField.setValue(num);
    }

    public static String getDelayInput() {
        return delayInpuTextField.getText();
    }

    public static String getInitialDelayInput() {
        return initialDelayTextField.getText();
    }

    public static boolean getIfUsingCoords() {
        return useCoordsEnteredCheckbox.getState();
    }

    public static void setCoordsButtonState() {
        if (isOnRecordAction == true) {
            recordCoordsButton.setText("Press Space to stop recording");
        } else {
            recordCoordsButton.setText("Click to start recording mouse coords");
        }
    }

    public static void setStartButtonState() {
        if (isClicking == false) {
            startButton.setText("CLICK TO START");
        } else {
            startButton.setText("MOUSE MOVE TO STOP");
        }
    }
}