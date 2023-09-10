package com.ff.intg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import io.temporal.spring.boot.ActivityImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;


@Component
@ActivityImpl
public class InvoicePdfActivityImpl implements InvoicePdfActivity {
    @Override
    public void jsonToPDF(String filename) throws Exception {
        Logger logger = LoggerFactory.getLogger(InvoicePdfActivityImpl.class);

        // https://stackoverflow.com/questions/55315164/easiest-method-to-convert-json-to-pdf-in-java
        File jsonFile = new File(filename).getAbsoluteFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);

        try {
            Map<String, Object> map = mapper.readValue(jsonFile, mapType);
            String json = mapper.writeValueAsString(map);
            String[] strings = json.split(System.lineSeparator());
            String newFileName = this.writePdf(filename, strings);
            logger.info("pdf write success {}", newFileName);
        } catch (IOException e) {
            logger.error("unable to read file", e);
        }
    }


    private String writePdf(String filename, String[] content) throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 725);
        for (String cline : content) {
            contentStream.showText(cline);
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();

        String newFilename = filename.replaceAll("\\.json$", ".pdf");

        document.save(newFilename);
        document.close();
        return newFilename;
    }

}
