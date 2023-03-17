/**
 * @author Bukowski Dawid S22310
 */

package zad2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.Locale;

public class Service {
    private final String apiKeyWeather = "ebb9646c39437c5c918338336de83a18";
    private final String kraj;
    private final Locale locale;
    private final String kod_waluty_kraju;

    public Service(String kraj) {
        this.kraj = kraj;
        this.locale = getLocaleFromNameOfCountry(kraj);
        this.kod_waluty_kraju = String.valueOf(Currency.getInstance(locale));
    }

    public String getKod_waluty_kraju() {
        return kod_waluty_kraju;
    }

    private Locale getLocaleFromNameOfCountry(String country) {
        for( Locale locale : Locale.getAvailableLocales() )
            if(country.equals(locale.getDisplayCountry())) return locale;
        return null;
    }

    public String getWeather(String miasto) {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + miasto + "," + kraj + "&APPID=" + apiKeyWeather + "&units=metric";
        try {
            URL url = new URL(urlString);
            URLConnection request = url.openConnection();
            request.connect();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(request.getInputStream()));
            return jsonObject.toJSONString();
        } catch (IOException | ParseException e) {
            return "Błąd połączenia: " + e.getMessage();
        }
    }

    public Double getRateFor(String kod_waluty) {
        if(kod_waluty_kraju.equals(kod_waluty)) {
            return (double)1;
        } else {
            String urlString = "https://api.exchangerate.host/latest?base=" + kod_waluty + "&symbols=" + kod_waluty_kraju;
            try {
                URL url = new URL(urlString);
                URLConnection request = url.openConnection();
                request.connect();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(request.getInputStream()));
                double rate = (double)((JSONObject)jsonObject.get("rates")).get(kod_waluty_kraju);
                System.out.printf("%s: %f %s%n", kod_waluty, rate, kod_waluty_kraju);
                return rate;
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return (double)-1;
            }
        }
    }

    public Double getNBPRate() {
        if(kod_waluty_kraju.equals("PLN")) {
            return (double)1;
        } else {
            String[] urlStrings = {"http://api.nbp.pl/api/exchangerates/rates/a/" + kod_waluty_kraju + "/", "http://api.nbp.pl/api/exchangerates/rates/b/" + kod_waluty_kraju + "/"};
            for( String urlString : urlStrings ) {
                try {
                    URL url = new URL(urlString);
                    URLConnection request = url.openConnection();
                    request.connect();

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(request.getInputStream()));
                    double rate = (double)((JSONObject)((JSONArray)jsonObject.get("rates")).get(0)).get("mid");
                    System.out.printf("%s: %f PLN%n", kod_waluty_kraju, rate);
                    return rate;
                } catch (FileNotFoundException e) {
                    if(urlString.equals(urlStrings[urlStrings.length - 1])) {
                        e.printStackTrace();
                        return (double)-1;
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                    return (double)-1;
                }
            }
            return (double)-1;
        }
    }
}
