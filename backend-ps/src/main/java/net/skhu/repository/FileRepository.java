package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.domain.File;

public interface FileRepository extends JpaRepository<File, Integer> {

}
