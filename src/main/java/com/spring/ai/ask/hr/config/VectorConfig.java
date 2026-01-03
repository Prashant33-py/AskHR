package com.spring.ai.ask.hr.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorConfig {

    @Bean
    public VectorStore getPgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel){
        jdbcTemplate.execute("""
                CREATE EXTENSION IF NOT EXISTS vector;
                CREATE EXTENSION IF NOT EXISTS hstore;
                
                CREATE TABLE IF NOT EXISTS vector_store (
                                                            id TEXT PRIMARY KEY,
                                                            content TEXT,
                                                            metadata JSONB,
                                                            embedding VECTOR(1024)
                    );
                
                CREATE INDEX IF NOT EXISTS vector_store_embedding_idx ON vector_store USING HNSW (embedding vector_cosine_ops);
                """);
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(1024)
                .build();
    }

}
