package example;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.ModelOperation;
import net.java.html.json.Property;

@Model(targetId = "", className = "ClockModel", properties = {
    @Property(name = "clock", type = String.class)
    ,
    @Property(name = "city", type = String.class)
})
public class Clock {

    @Function
    static void currentLocale(ClockModel model) {
        model.setCity(ZoneId.systemDefault().getId());
    }

    public static void setUp() {
        ClockModel model = new ClockModel();
        model.setClock("Please wait...");
        model.setCity("Europe/London");
        model.applyBindings();
        
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateClock(model);
            }
        }, 0, 1000);
    }

    @ModelOperation
    static void updateClock(ClockModel model) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(model.getCity());
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);

        model.setClock(DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(zdt));
    }
}
