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
    private final ClientService clientService;

    private final BotService botService;

    private static final Logger LOG = LogManager.getRootLogger();
    private final Map<String, ServiceInfo> clients = new HashMap<>();

    public MDNSServiceDiscovery(ClientService clientService, BotService service) {
        this.clientService = clientService;
        this.botService = service;
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
                    LOG.warn("Esp client was disconnected: " + event.getName());
                    clients.remove(event.getName());
                    botService.notifyKarenBot("Client disconnect: " + event.getName(), true);
                }

                @Override
                public void serviceResolved(ServiceEvent event) {
                    ServiceInfo serviceInfo = event.getInfo();
                    LOG.info("Resolve new event: " + serviceInfo);
                    if (serviceInfo != null && serviceInfo.getName().contains("esp-plug")) {

                        String name = serviceInfo.getName().split("-")[2];
                        String ip = serviceInfo.getHostAddresses()[0];
                        String mac = serviceInfo.getPropertyString("mac");
                        String ssid = serviceInfo.getPropertyString("ssid");
                        String version = serviceInfo.getPropertyString("version");

                        if (mac == null || ssid == null || version == null) {
                            LOG.warn("One of fields is null for: " + serviceInfo.getName());
                            return;
                        }

                        if (clients.get(event.getName()) != null) {
                            LOG.warn("Esp data already present " + serviceInfo.getName());
                            return;
                        }
                        clients.put(event.getName(), event.getInfo());

                        Client newClient = Client.builder()
                                .ip(ip)
                                .mac(mac)
                                .ssid(ssid)
                                .name(name)
                                .version(version)
                                .build();

                        LOG.info("Esp client was added: " + newClient);
                        clientService.checkAndProcessClient(newClient);
                        clientService.postBoardConfigInKarenData(serviceInfo.getHostAddresses()[0]);
                    }
                }
            });

        } catch (Exception e) {
            LOG.error("Error discover services: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public Map<String, ServiceInfo> getClients() {
        return clients;
    }
}
