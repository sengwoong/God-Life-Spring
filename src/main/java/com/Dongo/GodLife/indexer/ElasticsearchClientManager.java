package com.Dongo.GodLife.indexer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchClientManager {
    private final ElasticsearchFactory elasticsearchFactory;
    private final Map<String, ElasticsearchClient> clientMap = new ConcurrentHashMap<>();
    private final Map<String, RestClient> restClientMap = new ConcurrentHashMap<>();

    public ElasticsearchClient getElasticsearchClient(String clusterName) {
        if (clientMap.containsKey(clusterName)) {
            return clientMap.get(clusterName);
        }
        ElasticsearchClient elasticsearchClient = elasticsearchFactory.getElasticsearchClient();
        clientMap.put(clusterName, elasticsearchClient);
        return elasticsearchClient;
    }

    public RestClient getRestClient(String clusterName) {
        if (restClientMap.containsKey(clusterName)) {
            return restClientMap.get(clusterName);
        }
        RestClient restClient = elasticsearchFactory.getRestClient();
        restClientMap.put(clusterName, restClient);
        return restClient;
    }
}
