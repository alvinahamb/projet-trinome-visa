package itu.framework.configs;

import itu.framework.utils.AssetUtil;
import mg.razherana.framework.configs.AppConfig;
import mg.razherana.framework.configs.Config;

@AppConfig
public class JspUtilConfigs {
  @Config
  public String jspUtils() {
    return AssetUtil.class.getName();
  }
}
