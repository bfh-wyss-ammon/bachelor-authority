/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package util;

import static org.junit.Assert.*;

import org.junit.Test;

import data.AuthoritySettings;

public class TestHash {

	@Test
	public void testHashAndCredentialHelper() {
		System.out.println(CredentialHelper.GetHash("test"));
		System.out.println(CredentialHelper.securePassword(CredentialHelper.GetHash("test")));
		assertTrue(CredentialHelper.GetHash("haha").equals(CredentialHelper.GetHash("haha")));
		assertTrue(!CredentialHelper.GetHash("1337").equals(CredentialHelper.GetHash("1336")));
		
		AuthoritySettings settings = SettingsHelper.getSettings(AuthoritySettings.class);
		String salt = settings.getSalt();
		
		//check settings
		assertTrue(settings != null);
		assertTrue(salt != null && !salt.equals(""));
		
		//check salting
		String hash = CredentialHelper.GetHash("mobility");
		assertTrue(!hash.equals(CredentialHelper.securePassword(hash)));
		
	}
}
