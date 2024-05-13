package com.hongson.xing;

import com.hongson.xing.common.JFinalClubConfig;
import com.jfinal.server.undertow.UndertowServer;

public class startApp {

    public static void main(String[] args) {
        UndertowServer.start(JFinalClubConfig.class);
    }
}
