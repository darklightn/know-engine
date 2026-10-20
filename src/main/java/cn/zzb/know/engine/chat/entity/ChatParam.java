package cn.zzb.know.engine.chat.entity;

import cn.zzb.know.engine.ai.model.IntentRecognitionResult;
import cn.zzb.know.engine.chat.constant.ChatSource;

public record ChatParam(String userId, String conversationId, String messageId, String content, String assistantMessageId,
                        IntentRecognitionResult intentRecognitionResult, ChatSource chatSource) {
}
