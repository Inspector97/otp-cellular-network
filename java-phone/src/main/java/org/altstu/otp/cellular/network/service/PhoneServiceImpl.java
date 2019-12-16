package org.altstu.otp.cellular.network.service;

import com.ericsson.otp.erlang.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.altstu.otp.cellular.network.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneServiceImpl implements PhoneService, MessageListener.CallbackMessage {

    @Value("${phone.network.rpc-server}")
    String SERVER_NODE_NAME;

    @Value("${phone.network.rpc-domain}")
    String RPC_DOMAIN;

    @Autowired
    private Pinger pinger;
    private String phoneNumber;
    private String stationName;
    private OtpMbox otpMbox;
    private OtpErlangPid pid;
    private OtpConnection conn;
    private MessageListener messageListener;
    private List<String> messages;

    private OtpSelf client;
    private OtpPeer peer;

    @PostConstruct
    private void initPhoneService() throws Exception {
        String number = getPhoneNumber();
        String station = getStationName();
        log.info("number = {}, station = {}", number, station);
        
        client = new OtpSelf(number, "secure");
        peer = new OtpPeer(station);
        conn = client.connect(peer);

        register(number);

        messages = new ArrayList<>();

        messageListener = new MessageListener();
        messageListener.registerCallback(this, conn);
        messageListener.start();
    }

    @Override
    public String getPhoneNumber() {
        if (Objects.isNull(phoneNumber)) {
            phoneNumber = System.getProperty("phone.number", generateNumber());
        }
        return phoneNumber;
    }
    
    private String getStationName() {
        if(Objects.isNull(stationName))
            stationName = System.getProperty("phone.station", SERVER_NODE_NAME) + "@" + RPC_DOMAIN;
        return stationName;
    }

    @Override
    public void register(String number) throws Exception {
        pinger.sendMessage(conn, "register", new OtpErlangList(new OtpErlangObject[]{
                client.pid(), // pid
                new OtpErlangString(number),  // phone number
        }
        ));
    }

    @Override
    public void sendMessage(String number, String message) throws Exception {
        pinger.sendMessage(conn, "sms", new OtpErlangList(new OtpErlangObject[]{
                new OtpErlangString(phoneNumber), // from
                new OtpErlangString(number),      // to
                new OtpErlangString(message),
        }
        ));
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }

    private String generateNumber() {
        final int length = 8;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (random.nextInt(10) + '0'));
        }
        return stringBuilder.toString();
    }

    @Override
    public void callback(String message) {
        messages.add(message);
        System.out.println(message);
    }
}
