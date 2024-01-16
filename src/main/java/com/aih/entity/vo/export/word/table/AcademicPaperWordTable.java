package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.AcademicPaperAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicPaperWordTable {
    private String title;
    private String academicPaperType;
    private String publishJournal;
    private LocalDate publishDate;
    private String doi;

    public AcademicPaperWordTable(AcademicPaperAudit academicPaperAudit){
        this.title = academicPaperAudit.getTitle();
        this.academicPaperType = academicPaperAudit.getAcademicPaperType();
        this.publishJournal = academicPaperAudit.getPublishJournal();
        this.publishDate = academicPaperAudit.getPublishDate();
        this.doi = academicPaperAudit.getDoi();
    }
}
