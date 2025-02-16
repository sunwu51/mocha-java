package com.xiaogenban1993.mocha.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaogenban1993.mocha.element.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sdk {
    static HttpClient httpClient = HttpClient.newHttpClient();
    public static Context getBuildInContext() {
        Context ctx =  new Context(null);

        // Math
        ProtoElement mathProto = new ProtoElement("Math");
        mathProto.set("random", new NativeFunctionElement((list) -> {
            if (list.size() != 1) {
                throw new RuntimeException("参数不对");
            }
            return new NumberElement(Math.random());
        }));
        mathProto.set("abs", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            NumberElement numberElement = (NumberElement) list.get(1);
            return new NumberElement(Math.abs(numberElement.value));
        }));
        mathProto.set("ceil", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            NumberElement numberElement = (NumberElement) list.get(1);
            return new NumberElement(Math.ceil(numberElement.value));
        }));
        mathProto.set("floor", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            NumberElement numberElement = (NumberElement) list.get(1);
            return new NumberElement(Math.floor(numberElement.value));
        }));
        ctx.set("Math", mathProto);
        // File
        ProtoElement fileProto = new ProtoElement("File");
        fileProto.set("readFile", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            String fileName = list.get(1).toString();
            try {
                return new StringElement(Files.readString(Path.of(fileName)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        fileProto.set("writeFile", new NativeFunctionElement((list) -> {
            if (list.size() != 3) {
                throw new RuntimeException("参数不对");
            }
            String fileName = list.get(1).toString();
            String content = list.get(2).toString();
            try {
                Files.writeString(Path.of(fileName), content, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return Constants.NULL;
        }));
        fileProto.set("appendFile", new NativeFunctionElement((list) -> {
            if (list.size() != 3) {
                throw new RuntimeException("参数不对");
            }
            String fileName = list.get(1).toString();
            String content = list.get(2).toString();
            try {
                Files.writeString(Path.of(fileName), content, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return Constants.NULL;
        }));
        ctx.set("File", fileProto);
        //Json
        ProtoElement jsonProto = new ProtoElement("Json");
        jsonProto.set("stringify", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            Element obj = list.get(1);
            return new StringElement(toJson(obj));
        }));
        jsonProto.set("parse", new NativeFunctionElement((list) -> {
            if (list.size() != 2) {
                throw new RuntimeException("参数不对");
            }
            String json = list.get(1).toString();
            return fromJson(json);
        }));
        ctx.set("Json", jsonProto);

        // Http
        ProtoElement httpProto = new ProtoElement("Http");
        httpProto.set("fetch", new NativeFunctionElement((list) -> {
            if (list.size() != 3) {
                throw new RuntimeException("参数不对");
            }
            String url = list.get(1).toString();
            Element option = list.get(2);
            HttpRequest.Builder rb = HttpRequest.newBuilder()
                    .uri(URI.create(url));
            if (option.get("headers") != Constants.NULL) {
                for (Map.Entry<String, Element> kv : option.get("headers").map.entrySet()) {
                    rb = rb.header(kv.getKey(), kv.getValue().toString());
                }
            }
            if (option.get("method") != Constants.NULL) {
                if (option.get("method").toString().equalsIgnoreCase("GET")) {
                    rb = rb.GET();
                } else if (option.get("method").toString().equalsIgnoreCase("POST")) {
                    if (option.get("body") == Constants.NULL) {
                        rb = rb.POST(HttpRequest.BodyPublishers.ofString(""));
                    } else {
                        rb = rb.POST(HttpRequest.BodyPublishers.ofString(option.get("body").toString()));
                    }
                }
                // 其他method暂时不支持
            }
            HttpRequest request = rb.build();
            try {
                Element result = new Element();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                result.map.put("status", new NumberElement(response.statusCode()));
                result.map.put("body", new StringElement(response.body()));
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
        ctx.set("Http", httpProto);

        return ctx;
    }

    public static String toJson(Element obj) {
        String res;
        if (obj instanceof StringElement) {
            res = "\"" + StringEscapeUtils.escapeJava(obj.toString()) + "\"";
        } else if (obj instanceof NumberElement) {
            res = obj.toString();
        } else if (obj instanceof BooleanElement) {
            res = obj.toString();
        } else if (obj instanceof ArrayElement) {
            res = "[" + ((ArrayElement) obj).array.stream().map(Sdk::toJson).collect(Collectors.joining(", ")) + "]";
        } else if (obj instanceof NullElement) {
            res = "null";
        } else if (obj instanceof ProtoElement) {
            res = "null"; // 原型不可见吧
        } else {
            Map<String, Element> map = obj.map;
            res = "{" + map.entrySet().stream().map(e -> "\"" + e.getKey() + "\":" + toJson(e.getValue())).collect(Collectors.joining(", ")) + "}";
        }
        return res;
    }

    public static Element fromJson(String json) {
        json = json.trim();
        if (json.startsWith("[")) {
            return fastJsonToElement(JSON.parseArray(json));
        }
        return fastJsonToElement(JSON.parseObject(json));
    }

    private static Element fastJsonToElement(Object obj) {
        if (obj == null) {
            return Constants.NULL;
        }
        if (obj instanceof Number) {
            return new NumberElement(((Number) obj).doubleValue());
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj ? Constants.TRUE : Constants.FALSE;
        }
        if (obj instanceof String) {
            return new StringElement((String) obj);
        }
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            Element element = new Element();
            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                element.map.put(key, fastJsonToElement(value));
            }
            return element;
        }
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            List<Element> list = new ArrayList<>();
            ArrayElement arrayElement = new ArrayElement(list);
            for (int i = 0; i < jsonArray.size(); i++) {
                Object item = jsonArray.get(i);
                list.add(fastJsonToElement(item));
            }
            return arrayElement;
        }
        return Constants.NULL;
    }




    public static void main(String[] args) {
        Element element = fastJsonToElement(JSON.parseObject("{\"a\":1,\"b\":{\"c\":[1,2,\"ha\"]}}"));
        System.out.println(element);
    }
}
