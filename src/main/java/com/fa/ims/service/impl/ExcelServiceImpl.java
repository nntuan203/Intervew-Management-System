package com.fa.ims.service.impl;

import com.fa.ims.dto.JobDto;
import com.fa.ims.dto.OfferRecordDto;
import com.fa.ims.entity.Job;
import com.fa.ims.entity.Skill;
import com.fa.ims.enums.JobStatus;
import com.fa.ims.repository.JobRepository;
import com.fa.ims.repository.SkillsRepository;
import com.fa.ims.service.ExcelService;
import com.fa.ims.service.JobService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private  SkillsRepository skillsRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobService jobService;
    @Autowired
    private Validator validator;

    @Override
    public void exportOffersToExcel(List<OfferRecordDto> offers, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Offers-Data");

        int rowNum = 0;

        // Tạo đối tượng CellStyle để định dạng dòng tiêu đề
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);

        // Đặt giá trị cho các ô trong dòng tiêu đề
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Candidate Name");
        headerRow.createCell(1).setCellValue("Email");
        headerRow.createCell(2).setCellValue("Approve");
        headerRow.createCell(3).setCellValue("Department");
        headerRow.createCell(4).setCellValue("Note");
        headerRow.createCell(5).setCellValue("Status");

        // Áp dụng định dạng cho các ô trong dòng tiêu đề
        for (int i = 0; i < 6; i++) {
            headerRow.getCell(i).setCellStyle(headerCellStyle);
        }

        for (OfferRecordDto offer : offers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(offer.getCandidateName());
            row.createCell(1).setCellValue(offer.getEmail());
            row.createCell(2).setCellValue(offer.getApprove());
            row.createCell(3).setCellValue(offer.getDepartment());
            row.createCell(4).setCellValue(offer.getNote());
            row.createCell(5).setCellValue(offer.getStatus());
        }

        // Tự động co dãn các cột theo nội dung của các ô
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        workbook.close();
    }

    public List<Job> readJobsFromExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<Job> jobList = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // skip header row
            }

            if (row.getCell(0).getCellType() == CellType.BLANK) {
                break; // exit the loop when the Column No. is blank
            }

            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);
            Cell cell2 = row.getCell(2);
            Cell cell3 = row.getCell(3);
            Cell cell4 = row.getCell(4);
            Cell cell5 = row.getCell(5);
            Cell cell6 = row.getCell(6);
            Cell cell7 = row.getCell(7);
            Cell cell8 = row.getCell(8);

            if (cell0.getCellType() != CellType.NUMERIC ||
                    cell1.getCellType() != CellType.STRING ||
                    cell2.getCellType() != CellType.STRING ||
                    cell3.getCellType() != CellType.NUMERIC ||
                    cell4.getCellType() != CellType.NUMERIC ||
                    cell5.getCellType() != CellType.NUMERIC ||
                    cell6.getCellType() != CellType.NUMERIC ||
                    cell7.getCellType() != CellType.STRING ||
                    cell8.getCellType() != CellType.STRING) {
                int rowNum = row.getRowNum() + 1;
                String errorMessage = "Invalid cell type on row " + rowNum;
                throw new IllegalArgumentException(errorMessage);
            }

            String title = cell1.getStringCellValue();
            List<Skill> skills = readSkillsFromCell(cell2);
            LocalDate startDate = cell3.getLocalDateTimeCellValue().toLocalDate();
            LocalDate endDate = cell4.getLocalDateTimeCellValue().toLocalDate();
            double salaryFrom = cell5.getNumericCellValue();
            double salaryTo = cell6.getNumericCellValue();
            String workingAddress = cell7.getStringCellValue();
            String description = cell8.getStringCellValue();

            JobDto jobDto = JobDto.builder()
                    .jobTitle(title)
                    .skills(skills)
                    .jobStart(startDate)
                    .jobEnd(endDate)
                    .jobSalaryFrom(String.valueOf(salaryFrom))
                    .jobSalaryTo(String.valueOf(salaryTo))
                    .jobAddress(workingAddress)
                    .jobDescription(description)
                    .jobStatus(JobStatus.OPEN)
                    .build();

            Set<ConstraintViolation<JobDto>> violations = validator.validate(jobDto);

            if (!violations.isEmpty()) {
                ConstraintViolation<JobDto> firstViolation = violations.iterator().next();
                int rowNum = row.getRowNum() + 1;
                String errorMessage = firstViolation.getMessage() + " on row " + rowNum;
                throw new IllegalArgumentException(errorMessage);
            }

            jobList.add(jobService.mapperJobDtoToJob(jobDto));
        }
        workbook.close();
        return jobList;
    }

    private List<Skill> readSkillsFromCell(Cell cell) {
        String skillsString = cell.getStringCellValue();
        List<Skill> skills = new ArrayList<>();

        for (String skillString : skillsString.split(",")) {
            String skillName = skillString.trim();
            Skill skill = skillsRepository.findSkillBySkillsDescLike(skillName);
            if (skill != null) {
                skills.add(skill);
            }
        }
        return skills;
    }

    @Override
    @Transactional
    public void importJobsFromExcel(MultipartFile file) throws IOException {
        List<Job> jobs = readJobsFromExcel(file.getInputStream());
        jobRepository.saveAll(jobs);
    }
}

