package itu.framework.configs;

import jakarta.servlet.ServletContext;
import mg.razherana.framework.configs.AppConfig;
import mg.razherana.framework.configs.Config;

@AppConfig
public class FileConfigs {
  @Config(override = false)
  public String dbConnFilePath(ServletContext servletContext) {
    return servletContext.getRealPath("/WEB-INF/env/database/database.xml");
  }
}
