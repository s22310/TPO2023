/**
 * @author Bukowski Dawid S22310
 */

package zad2;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    Service s = new Service("Italy");
    String weatherJson = s.getWeather("Paris");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();

    launch(args);

  }

  @Override
  public void start(Stage stage) throws Exception{
    try {
      Parent root = FXMLLoader.load(getClass().getResource("TPO1.fxml"));
      Scene scene = new Scene(root);
      stage.setTitle("TPO1 - s22310");
      stage.setResizable(false);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}