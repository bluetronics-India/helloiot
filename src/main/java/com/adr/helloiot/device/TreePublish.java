//    HelloIoT is a dashboard creator for MQTT
//    Copyright (C) 2017 Adrián Romero Corchado.
//
//    This file is part of HelloIot.
//
//    HelloIot is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    HelloIot is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with HelloIot.  If not, see <http://www.gnu.org/licenses/>.
//
package com.adr.helloiot.device;

import com.adr.helloiot.TopicsManager;
import com.adr.helloiot.util.CompletableAsync;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author adrian
 */
public class TreePublish extends Device {

    protected TopicsManager manager;
    private ScheduledFuture<?> sf = null;
    private final Object sflock = new Object();

    public TreePublish() {
        super();
    }

    @Override
    public String getDeviceName() {
        return resources.getString("devicename.treepublish");
    }

    @Override
    public void construct(TopicsManager manager) {
        this.manager = manager;
    }

    @Override
    public void destroy() {
    }

    public final void sendMessage(String branch, byte[] message) {
        cancelTimer();
        manager.publish(getTopicPublish() + "/" + branch, getQos(), message, isRetained());
    }

    public final void sendMessage(String branch, String message) {
        sendMessage(branch, getFormat().parse(message));
    }

    public void sendMessage(String branch, byte[] message, long delay) {
        synchronized (sflock) {
            cancelTimer();
            sf = CompletableAsync.scheduleTask(delay, () -> {
                sendMessage(branch, message);
            });
        }
    }

    public void sendMessage(String branch, String message, long delay) {
        sendMessage(branch, getFormat().parse(message), delay);
    }

    public final void sendMessage(String branch) {
        sendMessage(branch, new byte[0]);
    }

    public void sendMessage(String branch, long delay) {
        sendMessage(branch, new byte[0], delay);
    }

    public void cancelTimer() {
        synchronized (sflock) {
            if (sf != null) {
                sf.cancel(false);
                sf = null;
            }
        }
    }
}
