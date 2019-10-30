package com.github.houbb.rpc.common.support.status.enums;

/**
 * <p> project: rpc-StatusEnum </p>
 * <p> create on 2019/10/30 20:37 </p>
 *
 * status=0 初始化
 *
 * status=1 可用
 *
 * status=2 等待关闭
 *
 * status=3 正常关闭完成
 *
 * status=4 超时关闭完成
 *
 * @author Administrator
 * @since 0.1.3
 */
public enum StatusEnum {
    /**
     * 初始化
     * @since 0.1.3
     */
    INIT(0),

    /**
     * 可用
     * @since 0.1.3
     */
    ENABLE(1),

    /**
     * 等待关闭
     * @since 0.1.3
     */
    WAIT_SHUTDOWN(2),

    /**
     * 关闭成功
     * @since 0.1.3
     */
    SHUTDOWN_SUCCESS(3),

    /**
     * 关闭超时
     * @since 0.1.3
     */
    SHUTDOWN_TIMEOUT(4),
    ;

    /**
     * 编码信息
     * @since 0.1.3
     */
    private final int code;

    StatusEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
