package com.Dongo.GodLife.indexer;

import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.transport.TransportUtils;

import javax.net.ssl.SSLContext;

@Component
@RequiredArgsConstructor
public class ElasticsearchFactory {
    private final String serverUrl = "https://localhost:9200";
    private final String apiKey = "aENxbVJaWUJmV1U1enpXT3dIVXY6dVJHY19SRFFmazdvYnhkVTJZaVowdw==";
    private final String username = "elastic";
    private final String password = "DEmsYic*UEDdz3O7kW1V";
    private final String fingerprint = "b14e85fff4cc6fecb35d1d58a3cce6d5d7a45d28a867485ab87869686c302961";

    ElasticsearchClient getElasticsearchClient() {
        RestClient restClient = getRestClient();
        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }

    public RestClient getRestClient() {
        SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(fingerprint);
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(AuthScope.ANY, 
            new UsernamePasswordCredentials(username, password));

        return RestClient
            .builder(HttpHost.create(serverUrl))
            .setDefaultHeaders(new Header[]{
                new BasicHeader("Authorization", "ApiKey " + apiKey)
            })
            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                .setSSLContext(sslContext)
                .setDefaultCredentialsProvider(credsProv)
            )
            .build();
    }
}