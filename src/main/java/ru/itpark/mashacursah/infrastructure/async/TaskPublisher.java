package ru.itpark.mashacursah.infrastructure.async;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TaskPublisher {
    public void submit(String command, long timeoutMillis) throws Exception {
        Process process = Runtime.getRuntime().exec(command.split(" "));

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(() -> {
            try {
                return process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        });

        long startTime = System.currentTimeMillis();

        while (!future.isDone()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > timeoutMillis) {
                future.cancel(true);
                System.out.println("too much");
            }
            Thread.sleep(1000);
        }

    }


}