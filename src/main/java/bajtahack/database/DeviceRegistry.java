package bajtahack.database;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceRegistry {
    
    public static final java.util.logging.Logger logger = LoggingFactory.loggerForThisClass();
    
    public static final DeviceRegistry instance = new DeviceRegistry();
    
    public final Map<String, Device> DEVICE_MAP = new ConcurrentHashMap<String, Device>();
    
    public static class Device {
        
        private final String ip;
        private Date lastModified;

        public Device(String ip, Date lastModified) {
            super();
            this.ip = ip;
            this.lastModified = lastModified;
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
        
    }
    
    public void addDevice(Device d) {
        synchronized (this) {
            if (DEVICE_MAP.containsKey(d.ip)) {
                
                final Device device = DEVICE_MAP.get(d.ip);
                device.setLastModified(new Date());
                
                logger.info("Device already exists " + d.ip);
                return;
            }
            
            logger.info("Added device " + d.getIp());
            DEVICE_MAP.put(d.ip, d);
        }
    }
    

}
