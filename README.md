# know-engine

know-engine 是一个独立的 Spring Boot 知识引擎。它把文档整理进知识库，并在回答问题时从多个数据源检索相关内容。

## 主要能力

- **文档接入**：支持 PDF、Word、Excel、Markdown、TXT 和 CSV。文件经过解析、切片和向量化后写入检索索引。
- **多源检索**：按问题在 Elasticsearch 向量检索、关系数据库（Text-to-SQL）和图数据库（Text-to-Cypher）之间选择数据源。
- **流式对话**：通过 SSE 返回回答，并带上处理进度和引用来源。配置凭证后，同一对话流程也可以接入钉钉机器人。
- **知识库管理**：维护文档、分段、版本和访问范围，原始文件保存在对象存储中。
- **问答缓存**：对已经人工审核、可以公开复用的问答，可以直接返回缓存答案。该功能默认关闭。

项目使用 Java 21 和 Spring Boot 3.5.6。运行配置见 `src/main/resources/application.yml`，模型密钥通过环境变量 `DASHSCOPE_API_KEY` 传入。

```bash
mvn test
mvn spring-boot:run
```

服务默认端口为 8009。
