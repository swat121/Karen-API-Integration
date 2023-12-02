package com.micro.service;

import com.micro.dto.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class MDNSServiceDiscovery {

    private static final Logger LOG = LogManager.getRootLogger();
    private final Map<String, ServiceInfo> services = new HashMap<>();

    public MDNSServiceDiscovery() {
        discoverServices();
    }

    private void discoverServices() {
        try {
            InetAddress localInetAddress = InetAddress.getLocalHost();

            LOG.info("Local ip address: " + localInetAddress);

            JmDNS jmdns = JmDNS.create(localInetAddress);

            jmdns.addServiceListener("_http._tcp.local.", new ServiceListener() {
                @Override
                public void serviceAdded(ServiceEvent event) {
                    String name = event.getName();
                    LOG.info("New service event: " + name + " ,info: " + event.getInfo());

                    if (name.contains("esp-plug")) {
                        jmdns.requestServiceInfo(event.getType(), name);
                    }
                }

                @Override
                public void serviceRemoved(ServiceEvent event) {
                    System.out.println("remove " + event.getName());
                    services.remove(event.getName());
                }

                @Override
                public void serviceResolved(ServiceEvent event) {
                    ServiceInfo serviceInfo = event.getInfo();
                    LOG.info("Resolve new event: " + serviceInfo);
                    if (serviceInfo != null && serviceInfo.getName().contains("esp-plug")) {

                        String number = serviceInfo.getName().split("-")[2];

                        services.put(event.getName(), event.getInfo());

                        Client newClient = Client.builder()
                                .ip(serviceInfo.getHostAddresses()[0])
                                .mac(serviceInfo.getPropertyString("mac"))
                                .ssid(serviceInfo.getPropertyString("ssid"))
                                .name(serviceInfo.getPropertyString("name"))
                                .version(serviceInfo.getPropertyString("version"))
                                .build();

                        LOG.info("Esp client was added: " + newClient);
                        System.out.println(newClient);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public Map<String, ServiceInfo> getServices() {
        return services;
    }
}
