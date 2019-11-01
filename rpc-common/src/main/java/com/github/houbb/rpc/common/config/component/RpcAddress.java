package com.github.houbb.rpc.common.config.component;

/**
 * 地址信息
 * @author binbin.hou
 * @since 0.0.6
 */
public class RpcAddress {

    /**
     * address 信息
     * @since 0.0.6
     */
    private String address;

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

    public RpcAddress(String address, int port, int weight) {
        this.address = address;
        this.port = port;
        this.weight = weight;
    }

    public String address() {
        return address;
    }

    public RpcAddress address(String ip) {
        this.address = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public RpcAddress port(int port) {
        this.port = port;
        return this;
    }

    public int weight() {
        return weight;
    }

    public RpcAddress weight(int weight) {
        this.weight = weight;
        return this;
    }


}
