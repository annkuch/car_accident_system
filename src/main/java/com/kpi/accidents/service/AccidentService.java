package com.kpi.accidents.service;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.kpi.accidents.model.Accident;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kpi.accidents.model.Rule;
import com.kpi.accidents.repository.AccidentData;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class AccidentService {
    private final AccidentData data;
    private static final Logger LOG = LoggerFactory.getLogger(AccidentService.class);

    @Autowired
    public AccidentService(@Qualifier("accidentData") AccidentData data) {
        this.data = data;
    }

    public Map<Integer, Accident> getValidateAccidents() {
        return data.findAll().stream().collect(Collectors.toMap(Accident::getId, accident -> accident));
    }
    public void createValidateAccident(Accident accident) {
        this.setNameOfType(accident);
        this.setRule(accident);
        data.save(accident);
    }

    public void updateValidateAccident(Accident accident) {
        this.setNameOfType(accident);
        this.setRule(accident);
        data.save(accident);
    }

    public void deleteValidateAccident(Accident accident) {
        data.delete(accident);
    }

    private void setNameOfType(Accident accident) {
        if (accident.getAccidentType().getId() == 1) {
            accident.getAccidentType().setName("Два транспортні засоби");
        } else if (accident.getAccidentType().getId() == 2) {
            accident.getAccidentType().setName("Транспортний засіб і людина");
        } else if (accident.getAccidentType().getId() == 3) {
            accident.getAccidentType().setName("Транспортний засіб й інший транспорт");
        } else if (accident.getAccidentType().getId() == 4) {
            accident.getAccidentType().setName("Громадський транспорт");
        } else if (accident.getAccidentType().getId() == 5) {
            accident.getAccidentType().setName("Інше");
        }
    }

    private void setRule(Accident accident) {
        Rule rule = new Rule();
        int ruleId = accident.getAccidentRule().getId();
        rule.setId(ruleId);

        if (ruleId == 1) {
            rule.setName("Стаття 286 ККУ – Порушення правил безпеки дорожнього руху");
        } else if (ruleId == 2) {
            rule.setName("Стаття 130 КпАП – Водіння в нетверезому стані");
        } else if (ruleId == 3) {
            rule.setName("Стаття 132 КпАП – Водіння без водійського посвідчення");
        } else if (ruleId == 4) {
            rule.setName("Стаття 124 КпАП – Порушення правил дорожнього руху, що спричинило матеріальну шкоду");
        } else if (ruleId == 5) {
            rule.setName("Стаття 117 КпАП – Недотримання дистанції");
        } else if (ruleId == 6) {
            rule.setName("Стаття 122 КпАП – Порушення правил обгону");
        } else if (ruleId == 7) {
            rule.setName("Стаття 152 КпАП – Порушення правил паркування");
        }

        accident.setAccidentRule(rule);
    }

    public void generatePdf(Accident accident) {
        try {
            // Вкажіть шлях до папки для збереження звітів
            String folderPath = "reports/";
            File folder = new File(folderPath);

            // Створіть папку, якщо вона не існує
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // Set the output file location
            String fileName = "accident_report_" + accident.getId() + ".pdf";
            File file = new File(folder, fileName);

            // Ініціалізуйте FileOutputStream для запису файлу
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            // Initialize PdfDocument and Document objects
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileOutputStream));
            Document document = new Document(pdfDoc);

            // Load a font that supports Cyrillic characters
            String fontPath = "src/main/resources/fonts/arial.ttf"; // Replace with your font file path
            PdfFont font = PdfFontFactory.createFont(fontPath, "CP1251", true);

            // Add title with Ukrainian text
            document.add(new Paragraph("Звіт про ДТП")
                    .setFont(font)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));

            // Create a table with 2 columns (accident details)
            Table table = new Table(2);
            table.setWidth(400);

            table.addCell(new Cell().add(new Paragraph("Поле").setFont(font)));
            table.addCell(new Cell().add(new Paragraph("Деталі").setFont(font)));

            // Add accident data to the table
            table.addCell(new Paragraph("ID").setFont(font));
            table.addCell(new Paragraph(String.valueOf(accident.getId())).setFont(font));

            table.addCell(new Paragraph("Ім'я водія").setFont(font));
            table.addCell(new Paragraph(accident.getName()).setFont(font));

            table.addCell(new Paragraph("Тип ДТП").setFont(font));
            table.addCell(new Paragraph(accident.getAccidentType().getName()).setFont(font));

            table.addCell(new Paragraph("Стаття").setFont(font));
            table.addCell(new Paragraph(accident.getAccidentRule().getName()).setFont(font));

            table.addCell(new Paragraph("Опис").setFont(font));
            table.addCell(new Paragraph(accident.getText()).setFont(font));

            table.addCell(new Paragraph("Адреса").setFont(font));
            table.addCell(new Paragraph(accident.getAddress()).setFont(font));

            table.addCell(new Paragraph("Дата").setFont(font));
            table.addCell(new Paragraph(accident.getDateAccident().toString()).setFont(font));

            table.addCell(new Paragraph("Час").setFont(font));
            table.addCell(new Paragraph(accident.getTimeAccident().toString()).setFont(font));

            document.add(table);

            // Close the document
            document.close();

            System.out.println("PDF generated successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }}
}
