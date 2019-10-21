package com.github.houbb.rpc.common.config.component;

/**
 * 地址信息
 * @author binbin.hou
 * @since 0.0.6
 */
public class Address {

    /**
     * ip 信息
     * @since 0.0.6
     */
    private String ip;

    /**
     * 端口号
     * @since 0.0.6
     */
    private int port;

    /**
     * 权重
     * @since 0.0.6
     */
    private int weight;

    public String ip() {
        return ip;
    }

    public Address ip(String ip) {
        this.ip = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public Address port(int port) {
        this.port = port;
        return this;
    }

    public int weight() {
        return weight;
    }

    public Address weight(int weight) {
        this.weight = weight;
        return this;
    }
}
