package org.altstu.otp.cellular.network;

import com.ericsson.otp.erlang.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener implements DisposableBean, Runnable {

    private Thread thread;
    private CallbackMessage callbackMessage;
    private OtpConnection conn;

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void registerCallback(CallbackMessage call, OtpConnection conn) {
        this.conn = conn;
        this.callbackMessage = call;
    }

    @Override
    public void run() {
        while(true) {
            try {
                OtpErlangTuple msg;
                OtpErlangObject data = conn.receiveRPC();
                String receivedString = data.toString();
                System.out.println("data received: " + receivedString);

                if (data instanceof OtpErlangTuple) {
                    msg = (OtpErlangTuple) data;
                    OtpErlangObject status = msg.elementAt(0);
                    OtpErlangObject body = msg.elementAt(1);
                    System.out.printf("status: %s | data: %s\n", status.toString(), body.toString());
                    // on success
                    if (status.toString().substring(0,2).equalsIgnoreCase("ok")) {
                        // message was sent
                        if(status.toString().equalsIgnoreCase("oksms")) {
                            OtpErlangTuple bodyData = (OtpErlangTuple) body;
                            String newMessage = String.format("from %s to %s: %s", bodyData.elementAt(0), bodyData.elementAt(1), bodyData.elementAt(2));
                            callbackMessage.callback(newMessage);
                        }
                    }
                }

            } catch (OtpErlangExit otpErlangExit) {
                otpErlangExit.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OtpAuthException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void destroy() throws Exception {

    }

    public interface CallbackMessage {
        void callback(String message);
    }
}
