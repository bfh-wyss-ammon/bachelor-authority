package util;

import static org.junit.Assert.*;

import org.junit.Test;

import data.AuthoritySettings;
import settings.DefaultSettings;

public class SettingsTest {
	@Test
	public void testSave() {
		AuthoritySettings settings = new AuthoritySettings();
		
		SettingsHelper.saveSettings(settings);
		
	}
	
	@Test
	public void testRead() {
		AuthoritySettings settings =SettingsHelper.getSettings();
		assertNotNull(settings);
	}
}
