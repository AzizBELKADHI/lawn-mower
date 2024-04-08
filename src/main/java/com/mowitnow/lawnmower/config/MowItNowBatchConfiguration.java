package com.mowitnow.lawnmower.config;


import com.mowitnow.lawnmower.batch.MowInstructionsWriter;
import com.mowitnow.lawnmower.batch.MowInstructionsProcessor;
import com.mowitnow.lawnmower.mower.Lawn;
import com.mowitnow.lawnmower.mower.MowerInstructions;
import com.mowitnow.lawnmower.mower.Position;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MowItNowBatchConfiguration {

    @Value("${input.file.path}")
    private String inputFilePath;

    @Autowired
    Lawn lawn;

    @Bean
    public Job mowItNowJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("mowItNowJob", jobRepository)
                .flow(processFileStep(jobRepository, transactionManager, lawn))
                .end()
                .build();
    }

    @Bean
    public Step processFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Lawn lawn) {
        return new StepBuilder("processFileStep", jobRepository)
                .<MowerInstructions, MowerInstructions>chunk(1,transactionManager)
                .reader(mowInstructionsReader())
                .processor(new MowInstructionsProcessor(lawn))
                .writer(new MowInstructionsWriter())
                .build();
    }

    @Bean
    public ItemReader<MowerInstructions> mowInstructionsReader() {
        return new FlatFileItemReaderBuilder<MowerInstructions>()
                .name("mowInstructionsReader")
                .resource(new FileSystemResource(inputFilePath))
                .lineMapper(mowInstructionsLineMapper())
                .build();
    }

    @Bean
    public LineMapper<MowerInstructions> mowInstructionsLineMapper() {
        DefaultLineMapper<MowerInstructions> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(mowInstructionsLineTokenizer());
        lineMapper.setFieldSetMapper(fieldSet -> {
            String[] fields = fieldSet.readString(0).split(" ");
            int x = Integer.parseInt(fields[0]);
            int y = Integer.parseInt(fields[1]);
            Position position = new Position(x,y);
            if (fields.length == 2) { // Si la ligne contient deux champs, il s'agit des dimensions de la pelouse
                lawn.setPosition(position);
                return new MowerInstructions(position);
            } else { // Sinon, il s'agit des instructions d'une tondeuse
                return new MowerInstructions(position, fields[2].charAt(0), fields[3]);
            }
        });
        return lineMapper;
    }

    @Bean
    public LineTokenizer mowInstructionsLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"instructions"});
        return tokenizer;
    }

}
