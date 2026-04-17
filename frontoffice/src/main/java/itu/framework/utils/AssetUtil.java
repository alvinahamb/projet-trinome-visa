package itu.framework.utils;

import jakarta.servlet.http.HttpServletRequest;
import mg.razherana.framework.App;
import mg.razherana.framework.web.routing.WebMapper;
import mg.razherana.framework.web.utils.jsp.JspUtil;

public class AssetUtil extends JspUtil {

  @Override
  public String getViewName() {
    return "$assets";
  }

  @Override
  public Object run(Object... args) {
    App app = (App) getData().get("app");
    HttpServletRequest request = (HttpServletRequest) getData().get("request");

    if (request == null) {
      throw new RuntimeException("HttpServletRequest is required in AssetUtil");
    }

    if (app == null) {
      throw new RuntimeException("App instance is required in AssetUtil");
    }

    // Check args
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("Asset name is required as argument");
    }

    if (!(args[0] instanceof String)) {
      throw new IllegalArgumentException("First argument must be a String representing the asset name");
    }

    String assetName = (String) args[0];

    // Append context path to asset URL
    String contextPath = request.getContextPath();

    String combined = WebMapper.combineAndNormalizePaths(contextPath, assetName);

    combined = "/" + combined;

    if (combined.equals("//"))
      combined = "/";

    return combined;
  }

}
