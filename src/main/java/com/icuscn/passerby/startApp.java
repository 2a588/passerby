package com.icuscn.passerby;

import com.icuscn.passerby.common.JFinalClubConfig;
import com.jfinal.server.undertow.UndertowServer;

public class startApp {

    public static void main(String[] args) {
        UndertowServer.start(JFinalClubConfig.class);
    }
}
