package itu.framework.middleware;

import jakarta.servlet.http.HttpServletResponse;
import mg.razherana.framework.web.middlewares.Middleware;
import mg.razherana.framework.web.utils.proxies.annotations.Impl;

public abstract class CorsMiddleware extends Middleware {
  @Impl
  public Object before(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

    return null;
  }

  @Impl
  public Object after(HttpServletResponse response) {
    return null;
  }
}
