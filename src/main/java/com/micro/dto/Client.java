package com.micro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private String name;
    private String ip;
    private String mac;
    private String ssid;
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client that = (Client) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(name, that.name) &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(ssid, that.ssid) &&
                Objects.equals(version, that.version);
    }

    public List<String> getDifferences(Client other) {
        List<String> differences = new ArrayList<>();

        if (!Objects.equals(ip, other.ip)) differences.add("ip");
        if (!Objects.equals(name, other.name)) differences.add("name");
        if (!Objects.equals(mac, other.mac)) differences.add("mac");
        if (!Objects.equals(ssid, other.ssid)) differences.add("ssid");
        if (!Objects.equals(version, other.version)) differences.add("version");

        return differences;
    }
}
