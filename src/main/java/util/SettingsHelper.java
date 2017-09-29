package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import com.google.gson.Gson;

import data.AuthoritySettings;

public class SettingsHelper {
	private static Gson gson;
	private static String filePath = "settings.json";

	static {
		gson = new Gson();
	}

	public static AuthoritySettings getSettings() {
		FileReader reader;
		try {
			reader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(reader);

	        String line;
	        String json = "";

	        while ((line = bufferedReader.readLine()) != null) {
	           json += line;
	        }
	        reader.close();
	        return gson.fromJson(json, AuthoritySettings.class);
	        
		} catch (Exception e) {
			// TODO ex handling
			e.printStackTrace();
		}
		return null;
        
	}

	public static void saveSettings(AuthoritySettings settings) {
		String jsonSettings = gson.toJson(settings);

		FileWriter writer;
		try {
			writer = new FileWriter(filePath);
			writer.write(jsonSettings);
			writer.close();
		} catch (Exception e) {
			// TODO ex handling
			e.printStackTrace();
		}
	}
}
