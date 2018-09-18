package org.jordan.app.connect.model;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/18下午9:33
 */
public class RedisData implements Serializable {
    private final SimpleStringProperty keytype = new SimpleStringProperty();
    private final SimpleStringProperty key = new SimpleStringProperty();

    public RedisData(String keyType, String key) {
        this.keytype.set(keyType);
        this.key.set(key);
    }

    public void setKeytype(String keytype) {
        this.keytype.set(keytype);
    }
    public String getKeytype() {
        return keytype.get();
    }
    public SimpleStringProperty keytypeProperty() {
        return keytype;
    }
    public void setKey(String key) {
        this.key.set(key);
    }
    public String getKey() {
        return key.get();
    }
    public SimpleStringProperty keyProperty() {
        return key;
    }
}
