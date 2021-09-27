package fun.bookish.blueberry.server.sip;

import fun.bookish.blueberry.server.sip.conf.SipProperties;
import fun.bookish.blueberry.sip.SipServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SipServerConfiguration {

    @Bean
    public SipServer sipServer(SipProperties sipProperties) {
        SipServer sipServer = new SipServer.Builder(sipProperties.mapToSipServerConf())
                .build();
        sipServer.start();
        return sipServer;
    }

}
