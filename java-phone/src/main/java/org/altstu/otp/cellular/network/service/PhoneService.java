package org.altstu.otp.cellular.network.service;

public interface PhoneService {
    String getPhoneNumber();

    void sendMessage(String number, String message) throws Exception;
}
