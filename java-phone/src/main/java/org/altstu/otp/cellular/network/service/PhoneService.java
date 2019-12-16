package org.altstu.otp.cellular.network.service;

import java.util.List;

public interface PhoneService {
    String getPhoneNumber();
    void register(String number) throws Exception;
    void sendMessage(String number, String message) throws Exception;
    List<String> getMessages();
}
