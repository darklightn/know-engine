# know-engine

Standalone Spring Boot service for knowledge ingestion and retrieval-augmented chat.

Documents are stored, converted, split, embedded, and indexed. A question is classified, rewritten, and routed to Elasticsearch, relational SQL, or Neo4j. The model answer is streamed back over server-sent events. The same chat path can serve a DingTalk bot when credentials are set. An optional reviewed question-and-answer cache can return an approved public answer without running retrieval again.

## Capabilities

- Ingest PDF, Word, Excel, Markdown, TXT, and CSV. PDF conversion calls an external parse service. Word is converted toward Markdown. Excel is split as key-value text or HTML tables.
- Split Markdown by heading, length, regex, or separator. Parent and sibling segment ids let retrieval expand a hit back to a larger passage.
- Embed segments and store them in Elasticsearch. A local ONNX BGE reranker reorders hits inside the process.
- Route a question to vector search, text-to-SQL, or text-to-Cypher. A failed graph query falls back to Elasticsearch.
- Keep conversations, messages, document versions, and access scope in MySQL.
- Stream progress, citations, and optional structured cards to the web UI.
- Cache approved public answers after human review. The cache is off until `QA_CACHE_ENABLED` is set.

Markdown heading splitters follow the approach described by andyflury for LangChain4j ([langchain4j#574](https://github.com/langchain4j/langchain4j/issues/574)), with parent and sibling segment tracking added in this project.

## Requirements

- JDK 21
- MySQL 8
- Redis
- Elasticsearch 8 with vector search
- Neo4j 5 with APOC, when graph retrieval is used
- MinIO
- XXL-Job admin, for compensation and cache jobs
- A DashScope-compatible API key for the chat and embedding models

## Configuration

Runtime secrets are environment variables. `src/main/resources/application.yml` is not filtered by Maven, so Spring resolves the placeholders when the process starts.

| Variable | Used for |
| --- | --- |
| `DASHSCOPE_API_KEY` | Chat, streaming chat, and embedding calls |
| `DINGTALK_APP_KEY` | DingTalk app key. Leave unset to skip the stream client |
| `DINGTALK_APP_SECRET` | DingTalk app secret |
| `DINGTALK_ROBOT_CODE` | DingTalk robot code |
| `FILE_PARSE_API_URL` | Document parse service. Defaults to `http://127.0.0.1:8000` |
| `QA_CACHE_ENABLED` | Set to `true` to enable the reviewed answer cache |
| `QA_CACHE_REVIEWER_IDS` | Comma-separated staff ids allowed to review cache entries |

Local defaults in `application.yml` point at MySQL database `know_engine` (user and password `know_engine`), Redis on `localhost:6379` (password `knowengine`), Neo4j `neo4j` / `neo4j666`, MinIO `minioadmin` / `minioadmin`, and Elasticsearch at `http://127.0.0.1:9200`. Change those before exposing the process.

Create the schema from `src/main/resources/sql/tables.sql` before the first start. Sample graph statements are in `src/main/resources/neo4j/test-data.cypher`.

## Build and run

```bash
mvn test
mvn spring-boot:run
```

The HTTP port is `8009`. Static pages under `src/main/resources/static` cover login, chat, upload, documents, and cache review.

## Layout

Java sources live under `cn.zzb.know.engine`:

| Package | Role |
| --- | --- |
| `ai` | Intent recognition, prompts, title summary, general chat |
| `auth` | Staff and visitor login |
| `business` | Sample vehicle, order, user, and staff data used by text-to-SQL |
| `cache` | Reviewed question-and-answer cache |
| `chat` | Conversations, messages, and the streaming chat pipeline |
| `config` | Async execution, MyBatis-Plus, XXL-Job |
| `dingtalk` | Stream client and robot replies |
| `document` | Upload, conversion, segments, versions, object storage |
| `infra` | JSON repair, distributed lock, snowflake ids |
| `rag` | Query rewrite, routing, Elasticsearch, SQL, and Neo4j retrievers, splitters, reranker |

## Tests

`mvn test` runs unit tests with mocks or an in-memory H2 database. Intent recognition, the chat-model benchmark, and the live query-rewrite integration test are disabled because they call a model and need the surrounding services. Enable them locally only after `DASHSCOPE_API_KEY` and the infrastructure are configured.
