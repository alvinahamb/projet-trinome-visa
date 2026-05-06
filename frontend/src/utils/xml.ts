import { XMLParser } from "fast-xml-parser";

export function readXml<T = unknown>(xmlString: string): T {
  const parser = new XMLParser({
    ignoreAttributes: false,
  });
  
  const jsonObj = parser.parse(xmlString).prestashop;

  return jsonObj as T;
}
