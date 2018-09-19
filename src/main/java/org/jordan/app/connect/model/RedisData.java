package org.jordan.app.connect.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
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
    private final SimpleStringProperty field = new SimpleStringProperty();
    private final SimpleStringProperty value = new SimpleStringProperty();
    private final SimpleDoubleProperty score = new SimpleDoubleProperty();

    public RedisData setColumnData(String keytype,String key) {
        this.keytype.set(keytype);
        this.key.set(key);
        return this;
    }

    public RedisData setStringValue(String keytype,String value) {
        this.keytype.set(keytype);
        this.value.set(value);
        return this;
    }

    public RedisData setHashValue(String keytype, String field, String value) {
        this.keytype.set(keytype);
        this.field.set(field);
        this.value.set(value);
        return this;
    }

    public RedisData setSortedSetValue(String keytype, String value, double score) {
        this.keytype.set(keytype);
        this.value.set(value);
        this.score.set(score);
        return this;
    }

    public void setField(String field) {
        this.field.set(field);
    }
    public String getField() {
        return field.get();
    }
    public SimpleStringProperty fieldProperty() {
        return field;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
    public String getValue() {
        return this.value.get();
    }
    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setScore(double score) {
        this.score.set(score);
    }
    public double getScore() {
        return this.score.get();
    }
    public SimpleDoubleProperty scoreProperty() {
        return score;
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
