package org.altstu.otp.cellular.network.rest;

import lombok.RequiredArgsConstructor;
import org.altstu.otp.cellular.network.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class AppController {
    @Autowired
    private PhoneService phoneService;
    private final AtomicInteger counter = new AtomicInteger(0);

    @RequestMapping("/messages")
    public List<String> messages() {
        return phoneService.getMessages();
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void sendMessage(@RequestParam String phone, @RequestParam String message) {
//        String targetNumber = phoneService.getPhoneNumber();//new StringBuilder(phoneService.getPhoneNumber()).reverse().toString();
//        if(targetNumber.startsWith("8800"))
//            targetNumber = "555555666";
//        else
//            targetNumber = "88005553535";
//        
//        String message = "message " + counter.incrementAndGet();
        try {
            phoneService.sendMessage(phone, message);
        } catch (Exception e) {
            //TODO user logger
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getPhoneNumber", method = RequestMethod.GET)
    public String getPhoneNumber() {
        return phoneService.getPhoneNumber();
    }
}
