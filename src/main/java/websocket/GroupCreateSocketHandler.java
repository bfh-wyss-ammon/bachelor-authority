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
 * This websocket class handles the generation of new signature groups via the web application.
 */

package websocket;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import data.DbGroup;
import data.DbManagerKey;
import data.DbPublicKey;
import settings.DefaultSettings;
import util.DatabaseHelper;
import util.Generator;
import util.Logger;
import util.SettingsHelper;

@WebSocket
public class GroupCreateSocketHandler {

	Thread groupCreateThread;

	static List<Session> sessions = new ArrayList<>();

	@OnWebSocketConnect
	public void onConnect(Session user) throws Exception {
		sessions.add(user);
		send(user, getStatus());
	}

	@OnWebSocketClose
	public void onClose(Session user, int statusCode, String reason) {
		sessions.remove(user);
	}

	@OnWebSocketMessage
	public void onMessage(Session user, String message) {
		String code = message.toLowerCase();
		if (code.equals("status")) {
			send(user, getStatus());
		} else if (code.equals("new")) {
			// at the moment we allow only to create on group per time
			if (groupCreateThread == null || !groupCreateThread.isAlive()) {
				groupCreateThread = new Thread(new Runnable() {
					@Override
					public void run() {
						send("running");
						DbPublicKey publicKey = new DbPublicKey();
						DbManagerKey managerKey = new DbManagerKey();
						DbGroup group = new DbGroup();
						Generator.generate(SettingsHelper.getSettings(DefaultSettings.class), publicKey, managerKey);

						group.setManagerKey(managerKey);
						group.setPublicKey(publicKey);
						DatabaseHelper.Save(DbGroup.class, group);
						send("done");
					}
				});
				groupCreateThread.start();
			}
		}
	}

	private String getStatus() {
		return (groupCreateThread != null && groupCreateThread.isAlive() ? "running" : "ready");
	}

	private void send(String text) {
		sessions.forEach(session -> {
			send(session, text);
		});
	}

	private void send(Session session, String text) {
		if (session.isOpen()) {
			try {
				session.getRemote().sendString(text);
			} catch (Exception ex) {
				ex.printStackTrace();
				Logger.errorLogger(ex);
			}
		}
	}
}
