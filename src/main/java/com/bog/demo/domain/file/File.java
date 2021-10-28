package com.bog.demo.domain.file;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @NotBlank
    @Column(name = "mime_type")
    private String mimeType;

    @NotBlank
    @Column(name = "ext")
    private String ext;

    @NotNull
    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "state")
    private Integer state;

    public File() {
    }

    public File(Integer id) {
        this.id = id;
    }
}