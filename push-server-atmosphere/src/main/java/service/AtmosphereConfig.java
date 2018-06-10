package service;

import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.atmosphere.cpr.BroadcasterLifeCyclePolicy.*;

import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.AnnotationProcessor;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.util.VoidAnnotationProcessor;
import org.atmosphere.spring.bean.AtmosphereSpringContext;

import service.AtmosphereChatHandler;

@Configuration
public class AtmosphereConfig {

    @Bean
    public AtmosphereFramework atmosphereFramework() throws ServletException, InstantiationException, IllegalAccessException {
        AtmosphereFramework atmosphereFramework = new AtmosphereFramework(false, false);
        // atmosphereFramework.setBroadcasterCacheClassName(UUIDBroadcasterCache.class.getName());
        atmosphereFramework.addAtmosphereHandler("/chat/*", atmosphereChatHandler(), interceptors());
        return atmosphereFramework;
    }

    @Bean
    public AtmosphereChatHandler atmosphereChatHandler() {
        return new AtmosphereChatHandler();
    }

    private List<AtmosphereInterceptor> interceptors() {
        List<AtmosphereInterceptor> atmosphereInterceptors = new ArrayList<>();
        // atmosphereInterceptors.add(new TrackMessageSizeInterceptor());
        return atmosphereInterceptors;
    }

    @Bean
    public BroadcasterFactory broadcasterFactory() throws ServletException, InstantiationException, IllegalAccessException {
        return atmosphereFramework().getAtmosphereConfig().getBroadcasterFactory();
    }

    @Bean
    public AtmosphereSpringContext atmosphereSpringContext() {
        AtmosphereSpringContext atmosphereSpringContext = new AtmosphereSpringContext();
        Map<String, String> map = new HashMap<>();
        map.put("org.atmosphere.cpr.broadcasterClass", org.atmosphere.cpr.DefaultBroadcaster.class.getName());
        map.put(AtmosphereInterceptor.class.getName(), TrackMessageSizeInterceptor.class.getName());
        map.put(AnnotationProcessor.class.getName(), VoidAnnotationProcessor.class.getName());
        map.put("org.atmosphere.cpr.broadcasterLifeCyclePolicy", ATMOSPHERE_RESOURCE_POLICY.IDLE_DESTROY.toString());
        atmosphereSpringContext.setConfig(map);
        return atmosphereSpringContext;
    }
}
