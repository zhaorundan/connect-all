package org.jordan.app.connect.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/18下午9:33
 */
@Getter
@Setter
@NoArgsConstructor
public class RedisData implements Serializable {
    private String keyType;
    private String key;

    public RedisData(String keyType, String key) {
        this.keyType = keyType;
        this.key = key;
    }
}
