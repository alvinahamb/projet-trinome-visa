package itu.framework.utils;

public enum ConfigKeys {
  DB_CONN_FILE_PATH("dbConnFilePath");

  private final String key;

  ConfigKeys(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
