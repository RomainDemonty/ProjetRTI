package Modele;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
    private static final String CONFIG_FILE = "prop.properties";
    private Properties properties;

    public ReadProperties() {
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(CONFIG_FILE);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerAddress() {
        return properties.getProperty("serveur_adresse");
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("serveur_port"));
    }

}
