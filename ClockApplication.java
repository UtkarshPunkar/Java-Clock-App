import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class ClockApplication extends JFrame {
    private CardLayout cardLayout;
    private JPanel clockPanel;

    public ClockApplication() {
        setTitle("Clock Application");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton digitalButton = new JButton("Digital Clock");
        JButton analogButton = new JButton("Analog Clock");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(digitalButton);
        buttonPanel.add(analogButton);

        clockPanel = new JPanel();
        cardLayout = new CardLayout();
        clockPanel.setLayout(cardLayout);

        DigitalClock digitalClock = new DigitalClock();
        AnalogClock analogClock = new AnalogClock();

        clockPanel.add(digitalClock, "Digital");
        clockPanel.add(analogClock, "Analog");

        digitalButton.addActionListener(e -> cardLayout.show(clockPanel, "Digital"));
        analogButton.addActionListener(e -> cardLayout.show(clockPanel, "Analog"));

        add(buttonPanel, BorderLayout.NORTH);
        add(clockPanel, BorderLayout.CENTER);

        cardLayout.show(clockPanel, "Digital");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClockApplication app = new ClockApplication();
            app.setVisible(true);
        });
    }
}

class DigitalClock extends JLabel {
    public DigitalClock() {
        setFont(new Font("Arial", Font.BOLD, 48));
        setHorizontalAlignment(SwingConstants.CENTER);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                LocalTime time = LocalTime.now();
                setText(String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond()));
            }
        }, 0, 1000);
    }
}

class AnalogClock extends JPanel {
    public AnalogClock() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                repaint();
            }
        }, 0, 1000);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        LocalTime time = LocalTime.now();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int clockRadius = Math.min(getWidth(), getHeight()) / 2 - 40;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g2.drawOval(centerX - clockRadius, centerY - clockRadius, clockRadius * 2, clockRadius * 2);

        // Draw numbers
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 90);
            int x = (int) (centerX + Math.cos(angle) * (clockRadius - 20));
            int y = (int) (centerY + Math.sin(angle) * (clockRadius - 20));
            g2.drawString(String.valueOf(i), x - 5, y + 5);
        }

        // Draw hour hand
        double hourAngle = Math.toRadians((hours % 12 + minutes / 60.0) * 30 - 90);
        int hourX = (int) (centerX + Math.cos(hourAngle) * clockRadius * 0.5);
        int hourY = (int) (centerY + Math.sin(hourAngle) * clockRadius * 0.5);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(centerX, centerY, hourX, hourY);

        // Draw minute hand
        double minuteAngle = Math.toRadians((minutes + seconds / 60.0) * 6 - 90);
        int minuteX = (int) (centerX + Math.cos(minuteAngle) * clockRadius * 0.75);
        int minuteY = (int) (centerY + Math.sin(minuteAngle) * clockRadius * 0.75);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(centerX, centerY, minuteX, minuteY);

        // Draw second hand
        double secondAngle = Math.toRadians(seconds * 6 - 90);
        int secondX = (int) (centerX + Math.cos(secondAngle) * clockRadius * 0.85);
        int secondY = (int) (centerY + Math.sin(secondAngle) * clockRadius * 0.85);
        g2.setColor(Color.RED);
        g2.drawLine(centerX, centerY, secondX, secondY);
    }
}
