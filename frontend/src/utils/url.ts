import { readXml } from "./xml";

export function urlPrestashopApi(path: string): string {
  const baseUrl =
    import.meta.env.VITE_PRESTASHOP_URL_API || "http://notsetenvforprestashop/";
  return `${baseUrl}${path}`;
}

export async function fetchFromPrestashopApi<T = unknown>(
  path: string,
  fetchOptions: RequestInit,
) {
  if (!fetchOptions.headers)
    fetchOptions.headers = {} as Record<string, string>;

  (fetchOptions.headers as Record<string, string>)["Authorization"] =
    "Basic " + btoa(import.meta.env.VITE_PRESTASHOP_API_KEY + ":");
  const response = await fetch(urlPrestashopApi(path), fetchOptions);

  if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

  const data = await response.text();
  return readXml<T>(data);
}

export const API_QUERY = new URLSearchParams({
  ws_key: import.meta.env.VITE_PRESTASHOP_API_KEY,
}).toString();
