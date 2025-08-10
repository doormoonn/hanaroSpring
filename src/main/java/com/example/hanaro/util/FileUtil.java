package com.example.hanaro.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUtil {
	@Value("${upload.path}")
	private String uploadPath;

	public String uploadImage(MultipartFile file) {
		log.info("파일을 업로드합니다...");

		try {
			String originalFilename = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String savedFname = uuid + "_" + originalFilename;

			log.info("파일명: {}", originalFilename + "::" + file.getSize() / 1024);

			String savedir = getTodayPath();
			Path uploadDir = Paths.get(uploadPath + File.separator + savedir);
			Path upfilePath = Paths.get(uploadPath + File.separator + savedir + File.separator + savedFname);
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
			}
			file.transferTo(upfilePath);

			return savedir + File.separator + savedFname;
		} catch (IOException e) {
			throw new RuntimeException();  // todo
		}
	}


	private String getTodayPath() {
		LocalDateTime now = LocalDateTime.now();
		return String.format("%4d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
	}

}
