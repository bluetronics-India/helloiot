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
package com.adr.helloiot.unit;

import com.adr.helloiot.device.DeviceSubscribe;
import com.adr.helloiot.HelloIoTAppPublic;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 *
 * @author adrian
 */
public class ViewText extends Tile {

    @FXML
    private Label level;

    private DeviceSubscribe device = null;
    private final Object messageHandler = Units.messageHandler(this::updateStatus);    

    @Override
    protected Node constructContent() {
        return loadFXML("/com/adr/helloiot/fxml/viewtext.fxml");
    }

    @FXML
    public void initialize() {
        level.setText(null);
    }

    private void updateStatus(byte[] newstatus) {
        level.setText(device.getFormat().format(newstatus));
    }

    @Override
    public void construct(HelloIoTAppPublic app) {
        super.construct(app);
        device.subscribeStatus(messageHandler);
        updateStatus(null);
    }

    @Override
    public void destroy() {
        super.destroy();
        device.unsubscribeStatus(messageHandler);
    }

    public void setDevice(DeviceSubscribe device) {
        this.device = device;
        if (getLabel() == null) {
            setLabel(device.getProperties().getProperty("label"));
        }
    }

    public DeviceSubscribe getDevice() {
        return device;
    }
}
