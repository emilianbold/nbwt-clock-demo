package example;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.ModelOperation;
import net.java.html.json.OnPropertyChange;
import net.java.html.json.Property;

@Model(targetId = "", className = "ClockModel", properties = {
    @Property(name = "clock", type = String.class)
    ,
    @Property(name = "city", type = String.class)
})
@JavaScriptResource("plotly-latest.min.js")
public class Clock {

    @OnPropertyChange("city")
    static void cityChanged(ClockModel model) {
        updateClock(model);
    }

    @Function
    static void currentLocale(ClockModel model) {
        model.setCity(ZoneId.systemDefault().getId());
    }

    @JavaScriptBody(args = {}, body = "return {\n" +
"        autosize: false,\n" +
"        height: 220,\n" +
"        margin: {\n" +
"            l: 0,\n" +
"            r: 0,\n" +
"            t: 0,\n" +
"            b: 0,\n" +
"            pad: 0\n" +
"        },\n" +
"        geo: {\n" +
"            scope: 'world',\n" +
"            resolution: 50,\n" +
"            lonaxis: {\n" +
"                'range': [-10, 160]\n" +
"            },\n" +
"            lataxis: {\n" +
"                'range': [20, 70]\n" +
"            },\n" +
"            showland: true,\n" +
"            landcolor: '#EAEAAE'\n" +
"        }\n" +
"    }")
    public static native Object createGeoLayout();

    @JavaScriptBody(args = {"cities", "lat", "lon", "textPosition"}, body = "return [{\n" +
"        type: 'scattergeo',\n" +
"        mode: 'markers+text',\n" +
"        text: cities,\n" +
"        lon: lon,\n" +
"        lat: lat,\n" +
"        marker: {\n" +
"            size: 10,\n" +
"            line: {\n" +
"                width: 1\n" +
"            }\n" +
"        },\n" +
"        name: 'Clock cities',\n" +
"        textposition: textPosition,\n" +
"    }]")
    public static native Object createGeoData(String[] cities, double[] lat, double [] lon, String[] textPosition);

    @JavaScriptBody(args = {"divName", "data", "layout"}, body = "Plotly.newPlot(divName, data, layout, {displayModeBar: false})")
    public static native void newPlot(String divName, Object data, Object layout);

    @Function
    static void showPlot(ClockModel model) {
         newPlot("plot", createGeoData(new String[]{"London", "Paris", "Tokyo"},
                 new double[]{51.5074, 48.8566, 35.6895},
                 new double[]{-0.1278, 2.3522, 139.6917},
                 new String[]{"top right", "bottom right", "top center"}),
                 createGeoLayout());
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
