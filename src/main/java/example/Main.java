package example;

import com.sun.javafx.application.PlatformImpl;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import net.java.html.boot.fx.FXBrowsers;

public class Main {

    public static void main(String[] args) throws Exception {
        //just scaffolding
        EventQueue.invokeLater(() -> {
            //show Swing frame with a JavaFX panel
            JFrame f = new JFrame();
            JFXPanel jfxPanel = new JFXPanel();

            f.getContentPane().setLayout(new BorderLayout());
            f.getContentPane().add(jfxPanel, BorderLayout.CENTER);
            f.getContentPane().add(new JButton(new AbstractAction("Exit") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            }), BorderLayout.SOUTH);
            f.setSize(800, 800);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);

            //configure the panel
            PlatformImpl.startup(() -> {
                Stage stage = new Stage();

                stage.setTitle("");
                stage.setResizable(true);

                StackPane root = new StackPane();
                Scene scene = new Scene(root, 80, 20);
                stage.setScene(scene);

                ObservableList<Node> children = root.getChildren();

                WebView browser = new WebView();

                children.add(browser);

                jfxPanel.setScene(scene);

                loadWebView(browser);
            });
        });
    }

    private static void loadWebView(WebView browser) {
        FXBrowsers.load(browser, Main.class.getResource("index.html"), Clock.class, "setUp");
    }

}
