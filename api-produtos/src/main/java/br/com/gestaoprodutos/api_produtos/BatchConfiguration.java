package br.com.gestaoprodutos.api_produtos;

import br.com.gestaoprodutos.api_produtos.domain.model.Produto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public Job processarProduto(JobRepository jobRepository, Step step) {
        return new JobBuilder("processarProdutos", jobRepository)
                .incrementer((new RunIdIncrementer()))
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                     ItemReader<Produto> itemReader, ItemWriter<Produto> itemWriter,
                     ItemProcessor<Produto, Produto> itemProcessor
    ) {
        return new StepBuilder("step", jobRepository)
                .<Produto, Produto>chunk(20, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public ItemReader<Produto> itemReader() {
        BeanWrapperFieldSetMapper<Produto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Produto.class);
        return new FlatFileItemReaderBuilder<Produto>()
                .name("produtoItemReader")
                .resource(new ClassPathResource("produto.csv"))
                .delimited()
                .names("id", "nome", "quantidade")
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public ItemWriter<Produto> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Produto>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .sql(
                        "INSERT INTO produto (id, nome, quantidade) " +
                                "VALUES (:id, :nome, :quantidade)"
                )
                .build();
    }

    @Bean
    public ItemProcessor<Produto, Produto> itemProcessor() {
        return new ProdutoProcessor();
    }
}
