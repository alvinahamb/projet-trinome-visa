import type { RouteObject } from "react-router-dom";

export const mainRoutes : RouteObject[] = [
  {
    path: "/",
    lazy: () => import("@/pages/home/HomePage.tsx").then(module => ({ Component: module.HomePage }))
  }
];