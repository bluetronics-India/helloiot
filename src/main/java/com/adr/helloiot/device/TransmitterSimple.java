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
import com.adr.helloiot.device.format.MiniVar;

/**
 *
 * @author adrian
 */
public class TransmitterSimple extends Device implements DeviceSend {
    
    protected TopicsManager manager;
    
    @Override
    public final void construct(TopicsManager manager) {
        this.manager = manager;
    }

    @Override
    public final void destroy() {
    }

    @Override
    public String getDeviceName() {
        return resources.getString("devicename.transmittersimple");
    }

    @Override
    public void sendStatus(byte[] event) {
        manager.publish(getTopicPublish(), getQos(), event, isRetained());
    }

    @Override
    public void sendStatus(MiniVar event) {
        sendStatus(getFormat().devalue(event));
    }

    @Override
    public void sendStatus(String event) {
        sendStatus(getFormat().parse(event));
    }
}
