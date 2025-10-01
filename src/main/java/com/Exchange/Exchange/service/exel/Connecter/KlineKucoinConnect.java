package com.Exchange.Exchange.service.exel.Connecter;

import com.Exchange.Exchange.api.kucoin.KlineKucoin_api;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class KlineKucoinConnect {
    @Getter
    private Map<Long, List<String>> kucoinData = new ConcurrentHashMap<>();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final int maxThreads = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 8));
    private final int maxConcurrentRequests = 6;

    public void KucoinDataAll(String symbol, List<Long> timePoints, String interval) {
        if (timePoints.isEmpty()) {
            return;
        }

        kucoinData.clear();

        List<TimeRange> batches = createBatches(timePoints, interval);

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        KlineKucoin_api kucoinClass = new KlineKucoin_api();

        try {
            List<List<TimeRange>> chunks = partitionList(batches, maxConcurrentRequests);

            for (List<TimeRange> chunk : chunks) {
                List<CompletableFuture<Map<Long, List<String>>>> futures = chunk.stream()
                        .map(batch -> CompletableFuture.supplyAsync(() -> {
                            try {
                                return kucoinClass.klineKucoin(httpClient, symbol, batch.start, batch.end, interval);
                            } catch (Exception e) {
                                return new HashMap<Long, List<String>>();
                            }
                        }, executor))
                        .collect(Collectors.toList());

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenApply(v -> futures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList()))
                        .thenAccept(results -> {
                            results.forEach(kucoinData::putAll);
                        }).join();

                if (chunks.indexOf(chunk) < chunks.size() - 1) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } finally {
            executor.shutdown();
        }
    }

    private List<TimeRange> createBatches(List<Long> timePoints, String interval) {
        List<TimeRange> batches = new ArrayList<>();
        int batchSize = 1500;

        for (int i = 0; i < timePoints.size(); i += batchSize) {
            int end = Math.min(i + batchSize, timePoints.size());
            long startTime = timePoints.get(i);
            long endTime = timePoints.get(end - 1);

            batches.add(new TimeRange(startTime, endTime));
        }

        return batches;
    }

    private <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    public void clearKucoin() {
        kucoinData.clear();
    }

    private static class TimeRange {
        final long start;
        final long end;

        TimeRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}

