package com.andronauts.quizard.api;

import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class WebSocketClient {
    private static Socket mSocket;
    private static final String url = "http://192.168.0.106:4000/";

    public static Socket getMSocket(){
        if(mSocket != null){
            return mSocket;
        }

        try{
            mSocket = IO.socket(url);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return mSocket;
    }
}
