package com.bog.demo.repository.file;

import com.bog.demo.domain.file.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FileRepository extends CrudRepository<File, Integer> {

    @Query("select a from File a where a.state=1 and a.id in (:fileIds)")
    List<File> getFileByFileIds(@Param("fileIds") Set<Integer> fileIds);
}