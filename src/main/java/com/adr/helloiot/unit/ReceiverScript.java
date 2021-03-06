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

import com.adr.helloiot.EventMessage;
import com.adr.helloiot.HelloIoTAppPublic;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class ReceiverScript extends ReceiverBase {

    private final static Logger logger = Logger.getLogger(ReceiverScript.class.getName());

    private final Map<String, Object> params = new HashMap<>();
    private ScriptCode code = null;
    private String condition = null;

    @Subscribe
    public void receivedStatus(EventMessage message) {
        if (code == null) {
            logger.warning("There is no action code to execute.");
        } else {
            String topic = message.getTopic();
            byte[] status = message.getMessage();
            if (condition == null || (this.getDevice().getFormat().format(status).matches(condition))) {
                Map<String, Object> mergedParams = new HashMap<>();
                mergedParams.putAll(params);
                mergedParams.put("_device", getDevice());
                mergedParams.put("_topic", topic);
                mergedParams.put("_status", status);
                Futures.addCallback(code.run(mergedParams), new FutureCallback<Object>() {
                    @Override
                    public void onSuccess(Object v) {}
                    @Override
                    public void onFailure(Throwable ex) {
                        logger.log(Level.WARNING, "There has been an error running this action", ex);
                    }
                
                }, MoreExecutors.directExecutor());
            }
        }
    }

    @Override
    public void construct(HelloIoTAppPublic app) {
        super.construct(app);
        code.construct(app);
    }

    public Map<String, Object> getParameters() {
        return params;
    }

    public void setScriptCode(ScriptCode code) {
        this.code = code;
    }

    public ScriptCode getScriptCode() {
        return code;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
