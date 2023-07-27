package site.bleem.boot.conditional.client;

import feign.Headers;
import feign.RequestLine;
import feign.Response;

public interface OpenFeginClient {
    @RequestLine("GET /get/file")
    @Headers("Content-Type: application/octet-stream")
    Response getFile();
}
