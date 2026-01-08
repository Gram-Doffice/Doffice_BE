package gram11.doffice.global.webhook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "discordWebhookClient",
        url = "${discord.webhook.url}"
)
public interface DiscordWebhookClient {

    @PostMapping
    void send(@RequestBody DiscordWebhookRequest request);
}
