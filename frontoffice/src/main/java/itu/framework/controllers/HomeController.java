package itu.framework.controllers;

import mg.razherana.framework.web.annotations.Controller;
import mg.razherana.framework.web.annotations.Url;
import mg.razherana.framework.web.utils.ModelView;

@Controller(alias = "home")
public class HomeController {
  @Url
  public Object home(ModelView mv) {
    return mv.view("/WEB-INF/views/menu.jsp");
  }
}
