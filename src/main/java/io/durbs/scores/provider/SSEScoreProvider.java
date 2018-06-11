package io.durbs.scores.provider;

import io.durbs.scores.domain.Score;
import io.durbs.scores.repo.ScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Slf4j
@Component
public class SSEScoreProvider implements CommandLineRunner {

    @Autowired
    private ScoreRepository scoreRepository;

    @Value("${scores.provider.host}")
    private String providerHost;

    @Value("${scores.provider.resource}")
    private String providerResource;

    @Override
    public void run(String... args) throws Exception {

        WebClient.create(providerHost)
                .get()
                .uri(providerResource)
                .accept(TEXT_EVENT_STREAM)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Score.class))
                .subscribe(score -> scoreRepository.saveScore(score));
    }
}
