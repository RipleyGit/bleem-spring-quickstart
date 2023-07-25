package site.bleem.boot.disruptor.client;

import feign.Headers;
import feign.RequestLine;
import feign.Response;

public interface OpenFeginClient {
    @RequestLine("GET /get/file")
    @Headers("Content-Type: application/octet-stream")
    Response getFile();
}
