package cn.zzb.know.engine.dingtalk.config;

import cn.zzb.know.engine.dingtalk.callback.chatbot.ChatBotCallbackListener;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.callback.DingTalkStreamTopics;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DingTalkStreamClientConfiguration {

    @Value("${dingtalk.appKey:}")
    private String clientId;
    @Value("${dingtalk.appSecret:}")
    private String clientSecret;

    /**
     * 配置 OpenDingTalkClient 并在初始化时 start。
     * 未设置 DINGTALK_APP_KEY 与 DINGTALK_APP_SECRET 时不建立长连接。
     */
    @Bean(initMethod = "start")
    @ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${dingtalk.appKey:}') && T(org.springframework.util.StringUtils).hasText('${dingtalk.appSecret:}')")
    public OpenDingTalkClient configureStreamClient(@Autowired ChatBotCallbackListener chatBotCallbackListener) throws Exception {
        // init stream client
        return OpenDingTalkStreamClientBuilder.custom()
                //配置应用的身份信息, 企业内部应用分别为appKey和appSecret, 三方应用为suiteKey和suiteSecret
                .credential(new AuthClientCredential(clientId, clientSecret))
                //注册机器人回调
                .registerCallbackListener(DingTalkStreamTopics.BOT_MESSAGE_TOPIC, chatBotCallbackListener).build();
    }
}