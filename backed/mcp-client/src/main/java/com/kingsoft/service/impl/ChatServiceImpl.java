package com.kingsoft.service.impl;

import cn.hutool.json.JSONUtil;
import com.kingsoft.bean.ChatEntity;
import com.kingsoft.bean.ChatResponseEntity;
import com.kingsoft.bean.SearchResultXng;
import com.kingsoft.enums.SSEMsgType;
import com.kingsoft.service.ChatService;
import com.kingsoft.service.SearXngService;
import com.kingsoft.utils.SSEService;

import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import reactor.core.Disposable;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 12:26
 * @PackageName:com.kingsoft.service.impl
 * @ClassName: ChatServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Resource
    private SearXngService searXngService;

    private ChatMemory chatMemory;

    private ChatClient chatClient;

    private String systemPrompt = """
            你是一个非常聪明的人工智能助手，可以帮我解决很多问题，我为你取一个名字，你的名字叫'AI智能体'。
            
            你可以使用Markdown格式来美化回复内容，让信息更清晰易读。
            
            【重要】关于时间和数字的显示规则：
            
            1. 当你使用工具获取时间信息时，必须将时间用反引号包裹：
               例如："当前时间是 `2025-12-25 17:14:28`"
            
            2. 当你展示包含时间的表格数据时，表格中的时间列必须用反引号包裹每个时间值：
               
               正确示例：
               | 员工ID | 账号 | 打卡时间 | 打卡日期 | 地点 |
               |--------|------|----------|----------|------|
               | EMP001 | zhangsan | `2025-12-25 09:00:00` | `2025-12-25` | 办公室 |
               | EMP002 | lisi | `2025-12-25 09:15:30` | `2025-12-25` | 研发部 |
               
               错误示例（不要这样做）：
               | 员工ID | 账号 | 打卡时间 | 地点 |
               |--------|------|----------|------|
               | EMP001 | zhangsan | 2025-12-25 09:00:00 | 办公室 |
            
            3. 为什么必须用反引号：
               - 反引号会将内容标记为代码，保护其中的数字不被处理
               - 这样可以确保时间在流式输出时完整显示
               - 用户会看到带灰色背景的时间，更加醒目
            
            4. 适用范围：
               - 所有时间字段（checkTime, checkDate, createTime等）
               - 所有日期字段
               - 表格中的数字ID（如果很长的话）
            
            【图表生成规则】
            
            当用户要求生成图表时，你必须使用以下完整的ECharts配置格式。注意：必须包含完整的坐标轴配置、标题、图例等信息。
            
            ## 1. 柱状图（Bar Chart）示例：
            
            ```chart
            {
              "title": {
                "text": "产品销量统计",
                "left": "center"
              },
              "tooltip": {
                "trigger": "axis",
                "axisPointer": {
                  "type": "shadow"
                }
              },
              "legend": {
                "data": ["销量"],
                "top": "30"
              },
              "grid": {
                "left": "3%",
                "right": "4%",
                "bottom": "3%",
                "containLabel": true
              },
              "xAxis": {
                "type": "category",
                "data": ["产品A", "产品B", "产品C", "产品D", "产品E"],
                "axisLabel": {
                  "interval": 0,
                  "rotate": 0
                }
              },
              "yAxis": {
                "type": "value",
                "name": "销量（件）",
                "axisLabel": {
                  "formatter": "{value}"
                }
              },
              "series": [{
                "name": "销量",
                "type": "bar",
                "data": [120, 200, 150, 80, 170],
                "itemStyle": {
                  "color": "#5470c6"
                },
                "label": {
                  "show": true,
                  "position": "top"
                }
              }]
            }
            ```
            
            ## 2. 折线图（Line Chart）示例：
            
            ```chart
            {
              "title": {
                "text": "月度销售趋势",
                "left": "center"
              },
              "tooltip": {
                "trigger": "axis"
              },
              "legend": {
                "data": ["销售额", "利润"],
                "top": "30"
              },
              "grid": {
                "left": "3%",
                "right": "4%",
                "bottom": "3%",
                "containLabel": true
              },
              "xAxis": {
                "type": "category",
                "boundaryGap": false,
                "data": ["1月", "2月", "3月", "4月", "5月", "6月"],
                "axisLabel": {
                  "interval": 0
                }
              },
              "yAxis": {
                "type": "value",
                "name": "金额（万元）",
                "axisLabel": {
                  "formatter": "{value}"
                }
              },
              "series": [
                {
                  "name": "销售额",
                  "type": "line",
                  "data": [120, 132, 101, 134, 90, 230],
                  "smooth": true,
                  "itemStyle": {
                    "color": "#5470c6"
                  }
                },
                {
                  "name": "利润",
                  "type": "line",
                  "data": [220, 182, 191, 234, 290, 330],
                  "smooth": true,
                  "itemStyle": {
                    "color": "#91cc75"
                  }
                }
              ]
            }
            ```
            
            ## 3. 饼图（Pie Chart）示例：
            
            ```chart
            {
              "title": {
                "text": "产品类别占比",
                "left": "center"
              },
              "tooltip": {
                "trigger": "item",
                "formatter": "{a} <br/>{b}: {c} ({d}%)"
              },
              "legend": {
                "orient": "vertical",
                "left": "left",
                "top": "middle"
              },
              "series": [
                {
                  "name": "产品类别",
                  "type": "pie",
                  "radius": "60%",
                  "center": ["50%", "50%"],
                  "data": [
                    {"value": 335, "name": "电子产品"},
                    {"value": 310, "name": "服装"},
                    {"value": 234, "name": "食品"},
                    {"value": 135, "name": "图书"},
                    {"value": 148, "name": "其他"}
                  ],
                  "emphasis": {
                    "itemStyle": {
                      "shadowBlur": 10,
                      "shadowOffsetX": 0,
                      "shadowColor": "rgba(0, 0, 0, 0.5)"
                    }
                  },
                  "label": {
                    "show": true,
                    "formatter": "{b}: {c} ({d}%)"
                  }
                }
              ]
            }
            ```
            
            ## 4. 散点图（Scatter Chart）示例：
            
            ```chart
            {
              "title": {
                "text": "身高体重分布",
                "left": "center"
              },
              "tooltip": {
                "trigger": "item",
                "formatter": "身高: {value[0]}cm<br/>体重: {value[1]}kg"
              },
              "grid": {
                "left": "3%",
                "right": "7%",
                "bottom": "3%",
                "containLabel": true
              },
              "xAxis": {
                "type": "value",
                "name": "身高（cm）",
                "nameLocation": "middle",
                "nameGap": 30,
                "axisLabel": {
                  "formatter": "{value}"
                }
              },
              "yAxis": {
                "type": "value",
                "name": "体重（kg）",
                "nameLocation": "middle",
                "nameGap": 40,
                "axisLabel": {
                  "formatter": "{value}"
                }
              },
              "series": [{
                "name": "样本数据",
                "type": "scatter",
                "data": [[161.2, 51.6], [167.5, 59.0], [159.5, 49.2], [157.0, 63.0], [155.8, 53.6]],
                "symbolSize": 10,
                "itemStyle": {
                  "color": "#5470c6"
                }
              }]
            }
            ```
            
            ## 5. 多系列柱状图示例：
            
            ```chart
            {
              "title": {
                "text": "季度销售对比",
                "left": "center"
              },
              "tooltip": {
                "trigger": "axis",
                "axisPointer": {
                  "type": "shadow"
                }
              },
              "legend": {
                "data": ["2023年", "2024年"],
                "top": "30"
              },
              "grid": {
                "left": "3%",
                "right": "4%",
                "bottom": "3%",
                "containLabel": true
              },
              "xAxis": {
                "type": "category",
                "data": ["Q1", "Q2", "Q3", "Q4"],
                "axisLabel": {
                  "interval": 0
                }
              },
              "yAxis": {
                "type": "value",
                "name": "销售额（万元）",
                "axisLabel": {
                  "formatter": "{value}"
                }
              },
              "series": [
                {
                  "name": "2023年",
                  "type": "bar",
                  "data": [320, 332, 301, 334],
                  "itemStyle": {
                    "color": "#5470c6"
                  }
                },
                {
                  "name": "2024年",
                  "type": "bar",
                  "data": [420, 482, 491, 534],
                  "itemStyle": {
                    "color": "#91cc75"
                  }
                }
              ]
            }
            ```
            
            【重要提示】
            1. 必须包含 title（标题）配置
            2. 必须包含完整的 xAxis 和 yAxis 配置（饼图除外）
            3. xAxis 和 yAxis 必须包含 name（坐标轴名称）
            4. 必须包含 tooltip（提示框）配置
            5. 如果有多个数据系列，必须包含 legend（图例）配置
            6. 饼图必须使用 data 数组，每个元素包含 name 和 value
            7. 所有数据必须是真实的、有意义的数值
            8. 图表配置必须放在 ```chart 代码块中
            9. 配置必须是有效的JSON格式
            
            当用户请求生成图表时，请：
            1. 先分析数据
            2. 选择合适的图表类型
            3. 使用上述完整的配置模板
            4. 填入真实数据
            5. 在图表前后添加文字说明
            """;

    /**
     * AI提示词三大类型
     *  1.system: 系统提示词，用于描述AI助手的角色，以及AI助手的技能和知识。
     *  2.user: 用户输入，用于描述用户输入的指令和内容。
     *  3.assistant: AI助手输出，用于描述AI助手的输出结果。
     *
     */



    // 构造器注入，自动配置方式 引入大模型的依赖
    public ChatServiceImpl(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools,ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(tools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(systemPrompt)
                .build();
        
        // 将ChatMemory注入到SSEService
        SSEService.setChatMemory(chatMemory);
    }
    @Override
    public String chatTest(String prompt) {
        return  chatClient.prompt(prompt).call().content();

    }

    @Override
    public Flux<ChatResponse> streamResponse(String prompt) {
        return chatClient.prompt(prompt).stream().chatResponse();
    }

    @Override
    public Flux<String> streamStr(String prompt) {
        return chatClient.prompt(prompt).stream().content();
    }

    @Override
    public void doChat(ChatEntity chatEntity) {
        String userId = chatEntity.getCurrentUserName();
        String prompt = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();
        
        // 不再每次都清除记忆，而是按conversationId管理
        // 只在停止生成时清除（在SSEService.cancelSubscription中处理）
        
        StringBuilder fullContentBuilder = new StringBuilder();
        AtomicBoolean stopped = new AtomicBoolean(false);
        
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();
        
        // 使用响应式编程，而不是阻塞流
        Disposable subscription = stringFlux
            .takeWhile(content -> {
                // 检查SSE连接是否还存在
                if (!SSEService.isConnected(userId)) {
                    log.warn("用户 {} 的SSE连接已断开，停止生成", userId);
                    stopped.set(true);
                    return false;
                }
                return true;
            })
            .doOnNext(content -> {
                SSEService.sendMsg(userId, content, SSEMsgType.ADD);
                fullContentBuilder.append(content);
                log.info("content:{}", content);
            })
            .doOnComplete(() -> {
                if (!stopped.get() && SSEService.isConnected(userId)) {
                    String fullContent = fullContentBuilder.toString();
                    ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);
                    SSEService.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
                    log.info("消息生成完成，用户: {}", userId);
                } else {
                    log.info("消息生成已停止，用户: {}", userId);
                }
            })
            .doOnError(error -> {
                log.error("消息生成出错，用户: {}, 错误: {}", userId, error.getMessage());
            })
            .subscribe();
        
        // 保存订阅引用和conversationId，以便后续可以取消
        SSEService.saveSubscription(userId, botMsgId, subscription);
    }

    private static final String ragPROMPT = """
                                              基于上下文的知识库内容回答问题：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：未找到相关信息。
                                              如果查到，请回复具体的内容。不相关的近似内容不必提到。
                                              """;

    @Override
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> documents) {
        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();
        
        // 不再每次都清除记忆，按conversationId管理
        
        //构建提示词
        String context = null;
        if (documents!= null && documents.size()>0){
            context = documents.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }
        Prompt prompt = new Prompt(ragPROMPT
                .replace("{context}", context)
                .replace("{question}", question));

        StringBuilder fullContentBuilder = new StringBuilder();
        AtomicBoolean stopped = new AtomicBoolean(false);
        
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();
        
        // 使用响应式编程
        Disposable subscription = stringFlux
            .takeWhile(content -> {
                if (!SSEService.isConnected(userId)) {
                    log.warn("用户 {} 的SSE连接已断开，停止生成", userId);
                    stopped.set(true);
                    return false;
                }
                return true;
            })
            .doOnNext(content -> {
                SSEService.sendMsg(userId, content, SSEMsgType.ADD);
                fullContentBuilder.append(content);
                log.info("content:{}", content);
            })
            .doOnComplete(() -> {
                if (!stopped.get() && SSEService.isConnected(userId)) {
                    String fullContent = fullContentBuilder.toString();
                    ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);
                    SSEService.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
                    log.info("RAG消息生成完成，用户: {}", userId);
                } else {
                    log.info("RAG消息生成已停止，用户: {}", userId);
                }
            })
            .doOnError(error -> {
                log.error("RAG消息生成出错，用户: {}, 错误: {}", userId, error.getMessage());
            })
            .subscribe();
        
        // 保存订阅引用和conversationId
        SSEService.saveSubscription(userId, botMsgId, subscription);
    }


    private static final String searXngPROMPT = """
                                              你是一个互联网搜索大师，请基于以下互联网返回的结果作为上下文，根据你的理解结合用户的提问综合后，生成并且输出专业的回答：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：未找到相关信息。
                                              如果查到，请回复具体的内容。
                                              """;

    @Override
    public void doInternetSearch(ChatEntity chatEntity) {
        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();//联网搜索的问题
        String botMsgId = chatEntity.getBotMsgId();
        
        // 不再每次都清除记忆，按conversationId管理
        
        List<SearchResultXng> searchResult = searXngService.search(question);//联网搜索的内容
        String searXngPrompt = buildSearXngPrompt(question, searchResult);
        
        StringBuilder fullContentBuilder = new StringBuilder();
        AtomicBoolean stopped = new AtomicBoolean(false);
        
        Flux<String> stringFlux = chatClient.prompt(searXngPrompt).stream().content();
        
        // 使用响应式编程
        Disposable subscription = stringFlux
            .takeWhile(content -> {
                if (!SSEService.isConnected(userId)) {
                    log.warn("用户 {} 的SSE连接已断开，停止生成", userId);
                    stopped.set(true);
                    return false;
                }
                return true;
            })
            .doOnNext(content -> {
                SSEService.sendMsg(userId, content, SSEMsgType.ADD);
                fullContentBuilder.append(content);
                log.info("content:{}", content);
            })
            .doOnComplete(() -> {
                if (!stopped.get() && SSEService.isConnected(userId)) {
                    String fullContent = fullContentBuilder.toString();
                    ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);
                    SSEService.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
                    log.info("联网搜索消息生成完成，用户: {}", userId);
                } else {
                    log.info("联网搜索消息生成已停止，用户: {}", userId);
                }
            })
            .doOnError(error -> {
                log.error("联网搜索消息生成出错，用户: {}, 错误: {}", userId, error.getMessage());
            })
            .subscribe();
        
        // 保存订阅引用和conversationId
        SSEService.saveSubscription(userId, botMsgId, subscription);
    }

    private static String buildSearXngPrompt(String question, List<SearchResultXng> searchResultXngs) {
        StringBuilder context = new StringBuilder();
        searchResultXngs.forEach(searchResultXng -> {
            context.append(String.format("<context>\n[来源] %s \n [摘要] %s \n</context>\n",
                            searchResultXng.getUrl(),
                            searchResultXng.getContent()));
        });
        return searXngPROMPT
                .replace("{context}", context)
                .replace("{question}", question);
    }
}
