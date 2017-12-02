package bajtahack.easysql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author <a href="mailto:gustinmi@gmail.com">Mitja Guštin</a>
 *
 */
public interface ConnectionFactory {

	public abstract Connection getConnection() throws SQLException;

}