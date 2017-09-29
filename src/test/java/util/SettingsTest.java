package util;

import static org.junit.Assert.*;

import org.junit.Test;

import data.AuthoritySettings;
import settings.DefaultSettings;

public class SettingsTest {
	@Test
	public void testSave() {
		AuthoritySettings settings = new AuthoritySettings();
		DefaultSettings defaultSettings = new DefaultSettings();
		
		settings.setLc(defaultSettings.getlc());
		settings.setLe(defaultSettings.getle());
		settings.setlE(defaultSettings.getlE());
		settings.setlQ(defaultSettings.getlQ());
		settings.setModulus(defaultSettings.getModulus());
		settings.setPrime_certainty(defaultSettings.getPrimeCertainty());
		
		SettingsHelper.saveSettings(settings);
		
	}
	
	@Test
	public void testRead() {
		AuthoritySettings settings =SettingsHelper.getSettings();
		assertNotNull(settings);
	}
}
