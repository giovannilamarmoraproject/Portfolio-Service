package com.giovannilamarmora.project.portfolio.PortfolioService.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.model.PortfolioData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheConfig {

  @Bean
  public ReactiveRedisTemplate<String, PortfolioData> portfolioDataTemplate(
      ReactiveRedisConnectionFactory factory, ObjectMapper mapper) {
    StringRedisSerializer keySerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer<PortfolioData> valueSerializer =
        new Jackson2JsonRedisSerializer<>(mapper, PortfolioData.class);
    RedisSerializationContext.RedisSerializationContextBuilder<String, PortfolioData> builder =
        RedisSerializationContext.newSerializationContext(keySerializer);
    RedisSerializationContext<String, PortfolioData> context =
        builder.value(valueSerializer).build();
    return new ReactiveRedisTemplate<>(factory, context);
  }
}
