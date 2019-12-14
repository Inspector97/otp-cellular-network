package org.altstu.otp.cellular.network.service;

import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class Pinger {
    public void sendMessage(OtpConnection otpConnection, OtpErlangObject message) throws Exception {
        otpConnection.sendRPC("global", "send", new OtpErlangObject[]{
                new OtpErlangAtom("baseStation"),
                new OtpErlangTuple(new OtpErlangObject[]{
                        new OtpErlangAtom("sms"),
                        message
                }
                ),
        });
    }
}
