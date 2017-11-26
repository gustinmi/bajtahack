package bajtahack.database;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Device {
    
    final String id;
    final String ip;
    private Date lastModified;
    
    private Set<DeviceState> state = new HashSet<DeviceState>();
    
    public Device(String ip, Date lastModified) {
        super();
        this.id = ip.substring(0, 2);
        this.ip = ip;
        this.lastModified = lastModified;
    }
    
    public String getId() {
        return this.id;
    }

    public String getIp() {
        return this.ip;
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
        
        state.add(newState);
    }
}