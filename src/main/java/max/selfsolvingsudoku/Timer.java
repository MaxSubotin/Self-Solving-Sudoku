package max.selfsolvingsudoku;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {
    private Timeline time;
    private int runningTime;
    private final Label timer;

    public Timer(Label timer) {
        this.timer = timer;
    }

    // Start the timer from 00:00
    public void startTimer() {
        startTimerFrom("00:00");
    }

    // Start the timer from a specific time (in the format "mm:ss")
    public void startTimerFrom(String startTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            Date startDate = sdf.parse(startTime);
            runningTime = (int) (startDate.getTime() / 1000);

            if (time != null && time.getStatus() == Animation.Status.RUNNING) {
                return; // Timer is already running
            }

            this.timer.setText(startTime);

            // Create a Timeline to update the timer every second
            time = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
            time.setCycleCount(Animation.INDEFINITE);
            time.play();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Stop the timer
    public void stopTimer() {
        if (time != null) {
            time.stop();
        }
    }

    // Update the timer display
    private void updateTimer() {
        runningTime++;
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(new Date(runningTime * 1000L));
        timer.setText(time);
    }
}
