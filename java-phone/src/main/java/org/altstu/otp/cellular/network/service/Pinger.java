package org.altstu.otp.cellular.network.service;

import com.ericsson.otp.erlang.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class Pinger {

    @Value("${phone.network.rpc-module}")
    String BASE_STATION;

    public void sendMessage(OtpConnection otpConnection, String proc, OtpErlangList message) throws Exception {
        otpConnection.sendRPC(BASE_STATION, proc, message);
    }
}
