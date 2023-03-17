package zad2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI implements Initializable {

    @FXML
    private TextArea countryField, cityField, currencyField, weatherField, rateField, NBPField;
    @FXML
    private WebView webView;
    private WebEngine engine;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countryField.setText("Poland");
        cityField.setText("Warsaw");
        currencyField.setText("USD");
        engine = webView.getEngine();
        engine.load("https://www.google.com");

        updateFields();

    }

    @FXML
    private void updateFields(){
        Service s = new Service(countryField.getText());
        String weatherJson = s.getWeather(cityField.getText());
        Double rate1 = s.getRateFor(currencyField.getText());
        Double rate2 = s.getNBPRate();
        String currCode = s.getKod_waluty_kraju();
        weatherField.setText(getWeatherFromJSON(weatherJson));
        rateField.setText("1 " + currencyField.getText() + ": " + rate1.toString() + " " + currCode);
        NBPField.setText("1 " + currCode + ": " + rate2.toString() + " PLN");
        loadPage();
    }

    private void loadPage(){
        engine.load("https://en.wikipedia.org/wiki/" + cityField.getText());
    }

    private String getWeatherFromJSON(String s){
        JSONParser jsonParser = new JSONParser();
        StringBuffer weather = new StringBuffer();
        try {
            JSONObject weatherJSON = (JSONObject) jsonParser.parse(s);
            String temp = ((JSONObject)weatherJSON.get("main")).get("temp") + " ℃";
            String tempFelt = ((JSONObject)weatherJSON.get("main")).get("feels_like") + " ℃";
            String pressure = ((JSONObject)weatherJSON.get("main")).get("pressure") + " hPa";
            String humidity = ((JSONObject)weatherJSON.get("main")).get("humidity") + " %";
            String windSpeed = ((JSONObject)weatherJSON.get("wind")).get("speed") + " km/h";

            weather.append("Temperature: " + temp +
                    "\nFeels like: " + tempFelt +
                    "\nPressure: " + pressure +
                    "\nHumidity: " + humidity +
                    "\nWind speed: " + windSpeed);
            return String.valueOf(weather);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "City not found.";
    }

}
