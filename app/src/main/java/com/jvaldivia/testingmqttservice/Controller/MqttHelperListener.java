package com.jvaldivia.testingmqttservice.Controller;

import com.jvaldivia.testingmqttservice.Model.MqttMessageWrapper;

public interface MqttHelperListener {
    void displayMessage(String data);
    void saveMessage(MqttMessageWrapper[] data, int size);
}
