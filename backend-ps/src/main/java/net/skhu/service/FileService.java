package net.skhu.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.skhu.domain.File;
import net.skhu.repository.FileRepository;
import net.skhu.repository.PostRepository;
import net.skhu.repository.ProjectRepository;

@Service
public class FileService {
	@Autowired 
	FileRepository fileRepository;
	@Autowired 
	PostRepository postRepository;
	@Autowired 
	ProjectRepository projectRepository;

    // 파일 목록 조회
    public List<File> findAll() {
        return fileRepository.findAll(); // uploadedFile 테이블의 모든 레코드를 조회
    }

    // 파일 저장
    @Transactional
    public void save(MultipartFile multipartFile,int postId,String projectId) throws IOException {
        System.out.println("save");
    	// 업로드된 파일의 이름을 구한다.
        String fileName = Paths.get(multipartFile.getOriginalFilename()).getFileName().toString();

        File uploadedFile = new File(); // 엔터티 객체 생성
        uploadedFile.setName(fileName); // 파일명 설정
        uploadedFile.setSize((int) multipartFile.getSize()); // 파일크 기 설정
        uploadedFile.setSubmitTime(LocalDateTime.now()); // 현재 시각 설정
        uploadedFile.setData(multipartFile.getBytes()); // 파일의 내용을 data 속성에 저장
        uploadedFile.setPost(postRepository.findById(postId).get());
        //        uploadedFile.setPost(postRepository.findById(postId).get());
//        if(projectId==null) {
//        	 uploadedFile.setProject(null);
//        } else {
//        	 uploadedFile.setProject(projectRepository.findById(Integer.parseInt(projectId)).get());
//        }
        fileRepository.save(uploadedFile); // uploadedFile 테이블에 저장
    }

    // 파일 삭제
    public void delete(int id) throws IOException {
    	fileRepository.deleteById(id); // uploadedFile 테이블에서 레코드 삭제
    }

    // 다운로드하기 위해 파일을 조회하여 리턴
    public File getUploadedFile(int id) throws IOException {
        return fileRepository.findById(id).get(); // uploadedFile 테이블에서 레코드 조회
    }

}
