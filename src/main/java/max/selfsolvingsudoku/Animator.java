package max.selfsolvingsudoku;

import javafx.animation.ScaleTransition;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class Animator {

    public static void btnOnHover(Event e, double duration, double scale) {
        Button btn = (Button) e.getSource();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(duration), btn);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);

        scaleTransition.play();
    }

}
