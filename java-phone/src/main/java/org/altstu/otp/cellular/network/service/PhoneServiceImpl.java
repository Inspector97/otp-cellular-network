package org.altstu.otp.cellular.network.service;

import com.ericsson.otp.erlang.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {
    private final Pinger pinger;
    private String phoneNumber;
    private OtpMbox otpMbox;
    private OtpErlangPid pid;
    private OtpConnection conn;

    @PostConstruct
    private void initPhoneService() throws IOException, OtpAuthException {
        OtpSelf client = new OtpSelf(getPhoneNumber());
        OtpPeer peer = new OtpPeer("sh1");
        conn = client.connect(peer);
    }

    @Override
    public String getPhoneNumber() {
        if (Objects.isNull(phoneNumber)) {
            phoneNumber = System.getProperty("phone.number", generateNumber());
        }
        return phoneNumber;
    }

    @Override
    public void sendMessage(String number, String message) throws Exception {
        pinger.sendMessage(conn, new OtpErlangTuple(new OtpErlangObject[]{
                new OtpErlangString(number),
                new OtpErlangString(message),
        }
        ));
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
}
