package itu.framework.utils;

import java.sql.Connection;

import jakarta.servlet.ServletContext;
import mg.razherana.database.DatabaseConnection;
import mg.razherana.framework.web.givers.Giver;
import mg.razherana.framework.web.utils.proxies.annotations.Impl;
import mg.razherana.framework.web.utils.proxies.annotations.Resolve;

public abstract class DbConnGiver implements Giver {
  @Impl
  public Connection getConnection(ServletContext context) throws Exception {
    return DatabaseConnection.fromDotEnv(context.getAttribute(ConfigKeys.DB_CONN_FILE_PATH.key()).toString())
        .getConnection();
  }

  @Resolve
  public abstract Connection getConnection() throws Exception;
}
