package example;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import net.java.html.json.Model;
import net.java.html.json.Property;

@Model(targetId = "", className = "ClockModel", properties = {
    @Property(name = "clock", type = String.class)
})
public class Clock {

    public static void setUp() {
        ClockModel model = new ClockModel();
        model.setClock("Please wait...");
        model.applyBindings();
        
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                model.setClock(new Date().toString());
            }
        }, 0, 1000);
    }
}
