package bajtahack.database;

import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.logging.Level;
import bajtahack.easysql.Database;
import bajtahack.easysql.SqlQueryParam;
import bajtahack.easysql.StatementWithParams;

public class Device {
    
    public static enum DeviceType {
        TEMPERATURE,
        HUMIDTY,
        MOTION,
        LIGHT,
        BUTTON,
        WATER
    }
    
    public static final java.util.logging.Logger logger = LoggingFactory.loggerForThisClass();
    
    final String id;
    final String ip;
    private Date lastModified;
    
    final EnumSet<DeviceType> suportedDevices;
    
    private Set<DeviceState> state = new HashSet<DeviceState>();
    
    public Device(String ip, Date lastModified) {
        super();
        this.id = ip.substring(0, 2);
        this.ip = ip;
        this.lastModified = lastModified;
        
        if (this.id.equalsIgnoreCase("l1")) {
            suportedDevices = EnumSet.of(DeviceType.LIGHT, DeviceType.BUTTON);
            // lucke 4
            // gumbe
        }
        else if (this.id.equalsIgnoreCase("l2")) {
            suportedDevices = EnumSet.of(DeviceType.LIGHT, DeviceType.BUTTON, DeviceType.TEMPERATURE, DeviceType.MOTION, DeviceType.HUMIDTY);
        }
        else if (this.id.equalsIgnoreCase("l3")) {
            suportedDevices = EnumSet.of(DeviceType.LIGHT, DeviceType.WATER);
        }else {
            logger.warning("No default config for device: " + this.id);
            suportedDevices = EnumSet.noneOf(DeviceType.class);
        }
    }
    
    public String getId() {
        return this.id;
    }

    public String getIp() {
        return this.ip;
    }
    

    public String getSuportedDevices() {
        final StringBuilder sb = new StringBuilder("[");
        for (Iterator iterator = suportedDevices.iterator(); iterator.hasNext();) {
            DeviceType deviceState = (DeviceType) iterator.next();
            sb.append(" \"" + deviceState.name() + "\"");
            if (iterator.hasNext())
                sb.append(",");
            
        }
        sb.append("]");
        return sb.toString();
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    public Set<DeviceState> getState() {
        return this.state;
    }

    public void setState(Set<DeviceState> state) {
        this.state = state;
    }

    public void addState(DeviceState newState) {
        if (state.contains(newState)) {
            state.remove(newState);
        }
        newState.setDevice(this.id);
        state.add(newState);
        
        final StatementWithParams st = new StatementWithParams("INSERT INTO devicestate (device, service, dtype, dvalue) VALUES (?, ?, ?, ?)");
        st.addParam(new SqlQueryParam(Types.VARCHAR, newState.getDevice()));
        st.addParam(new SqlQueryParam(Types.VARCHAR, newState.getService()));
        st.addParam(new SqlQueryParam(Types.VARCHAR, newState.getType()));
        st.addParam(new SqlQueryParam(Types.VARCHAR, newState.getValue()));
        
        try {
            Database.instance.execUpdateQuery(st);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }
}