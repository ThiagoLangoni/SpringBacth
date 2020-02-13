package br.com.fiap.librarybatch;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class LibraryBatchApplication {

	Logger logger = LoggerFactory.getLogger(LibraryBatchApplication.class.getName());

	@Bean
	public Tasklet deleteFile(@Value("@{file.path}") String path){
		return (contribution, chunckContext) -> {

			File file = Paths.get(path).toFile();

			if(file.delete()) {
				logger.info("Arquivo deletado");
			}

			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public Job deleteJob(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("delete job")
								.start(step)
								.build();
	}

	@Bean
	@Scope("prototype")
	public Step deleteStep(StepBuilderFactory stepBuilderFactory, Tasklet tasklet) {
		return stepBuilderFactory.get("delete file step")
								 .tasklet(tasklet)
								 .build();
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryBatchApplication.class, args);
	}

}
