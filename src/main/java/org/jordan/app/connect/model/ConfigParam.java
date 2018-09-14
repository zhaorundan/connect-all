package org.jordan.app.connect.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/23上午9:42
 */
@Setter
@Getter
@ToString
//@XmlRootElement
@EqualsAndHashCode
public class ConfigParam implements Serializable {

    private static final long serialVersionUID = -4458074170372300213L;
    private String id;
    private ConfigType type;
    private String name;
    private String host;
    private String userName;
    private String password;
    private String datebase;
    private Integer port;
    private Date createTime;
    private Date updateTime;
    private boolean useSSL;

    public static enum ConfigType{
        MYSQL,REDIS,MONGON
    }

}
