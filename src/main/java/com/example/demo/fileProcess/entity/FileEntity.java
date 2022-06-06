package com.example.demo.fileProcess.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@Table
public class FileEntity {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private String path;
    private String name;
    private Long size;





}
